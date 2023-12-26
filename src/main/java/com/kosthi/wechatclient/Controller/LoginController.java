package com.kosthi.wechatclient.Controller;

import com.google.gson.reflect.TypeToken;
import com.kosthi.wechatclient.Entity.*;
import com.kosthi.wechatclient.Model.ChatManager;
import com.kosthi.wechatclient.Model.MsgData;
import com.kosthi.wechatclient.Util.OkHttpUtil;
import com.kosthi.wechatclient.View.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Vector;
import java.util.regex.Pattern;

public class LoginController implements Initializable {
    @FXML
    private TextField account;

    @FXML
    private PasswordField password;

    @FXML
    private Button loginButton;

    @FXML
    private Label accountError;

    @FXML
    private Label passwordError;

    @FXML
    private Button registerButton;

    private Login loginView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setOnMouseClicked(e -> {
            ((Register) ViewFactory.createUI(ViewType.REGISTER)).show();
        });

        loginButton.setOnAction(event -> {
            if (checkUserInfo()) {
                String userJson = HttpRequest.login(account.getText(), password.getText());
                if (userJson == null) {
                    passwordError.setText("！密码不匹配");
                    return;
                }

                User user = OkHttpUtil.GSON.fromJson(userJson, User.class);
                // System.out.println(user.getUsername());

                // 记录当前登录用户
                UserData.currentUser = user;

                // 服务器端自动更新在线信息
                // HttpRequest.updateUserStatus(UserData.currentUser.getAccount(), true);

                Homepage homepage = (Homepage) ViewFactory.createUI(ViewType.HOMEPAGE);
                homepage.setUserData(user);

                MainWindow mainWindow = (MainWindow) ViewFactory.createUI(ViewType.MAIN);
                // 设置主界面头像
                mainWindow.setHead(user.getHead());
                // 设置主界面信息
                mainWindow.setPersonalInfo(user.getAccount(), user.getUsername(), user.getAddress(), user.getTelephone());

                // 聊天助手
                mainWindow.addFriend("system", "WeChat聊天助手", "聊天助手");
                // ((Label) $(mainWindow, "Y_account")).setText("WeChat聊天助手");
                MsgData.msg.add(new Vector<>());
                MsgData.accountList.add("WeChat聊天助手");
                MsgData.msgTip.put("WeChat聊天助手", 0);

                // 添加所有好友
                String friendListJson = HttpRequest.queryAllFriends(user.getAccount());
                List<User> userList = OkHttpUtil.GSON.fromJson(friendListJson, new TypeToken<List<User>>() {
                }.getType());
                if (userList != null) {
                    for (User user0 : userList) {
                        MsgData.msg.add(new Vector<>());
                        String temp = user0.getAccount();
                        // 账号列表
                        MsgData.accountList.add(temp);
                        // 0 条消息
                        MsgData.msgTip.put(temp, 0);
                        mainWindow.addFriend(user0.getHead(), user0.getAccount(), user0.getLabel(), (FriendPage) ViewFactory.createUI(ViewType.FRIEND_PAGE));

                        // 列表里第几个好友
                        int index = MsgData.accountList.size() - 1;
                        // 更新好友登录状态信息
                        if (HttpRequest.checkIfUserOnline(user0.getAccount())) {
                            // 已登入就设置为登入状态
                            mainWindow.getFriendVector().get(index).setOnline();
                        }
                    }
                }

                mainWindow.addLeft("system", "欢迎使用WeChat,赶快找好友聊天吧!");
                MsgData.msg.get(0).add("WeChat聊天助手 欢迎使用WeChat,赶快找好友聊天吧!");
                //输入框禁用
                // ((TextField) $(mainWindow, "input")).setDisable(true);
                //  ((Button) $(mainWindow, "send")).setDisable(true);
                // 开始选择聊天助手
                mainWindow.getFriendList().getSelectionModel().select(0);
                mainWindow.getFriendVector().get(0).setOnline(); //否则未登入状态

                // 链接服务器
                ChatManager.getInstance().connect("127.0.0.1", user.getAccount());
                //设置背景
                //setHeadPortrait(((Button)$(mainWindow,"background")),"background",resultSet.getString("background"));
                ViewFactory.close(ViewType.LOGIN);
                mainWindow.show();
            }
            // 加入登录检查
            //
            // 设置用户数据
        });
    }

    private void clearAllError() {
        accountError.setText("");
        passwordError.setText("");
    }

    private boolean checkUserInfo() {
        clearAllError();

        String Account = account.getText();
        String Password = password.getText();
        if (Account.isEmpty() || Password.isEmpty()) {
            if (Account.isEmpty()) {
                accountError.setText("！未输入账号");
            }
            if (Password.isEmpty()) {
                passwordError.setText("！未输入密码");
            }
            return false;
        }
        String accountRegExp = "^[0-9a-zA-Z,\\u4e00-\\u9fa5]{1,15}$";
        String passwordReExp = "^[a-zA-Z0-9]{6,20}$";
        if (!Pattern.matches(accountRegExp, Account) || !Pattern.matches(passwordReExp, Password)) {
            if (!Pattern.matches(accountRegExp, Account)) {
                accountError.setText("！错误,账号是长度不超过15位的中文和英文和数字");
            }
            if (!Pattern.matches(passwordReExp, Password)) {
                passwordError.setText("！错误,密码是长度在6-20位的英文字母和数字");
            }
            return false;
        }
        if (!HttpRequest.checkIfUserExists(Account)) {
            accountError.setText("！错误,账号不存在");
            return false;
        }
        return true;
    }
}
