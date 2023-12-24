package com.kosthi.wechatclient.Controller;

import com.kosthi.wechatclient.Model.DatabaseModel;
import com.kosthi.wechatclient.Util.MD5Util;
import com.kosthi.wechatclient.Util.OkHttpUtil;
import com.kosthi.wechatclient.View.Login;
import com.kosthi.wechatclient.View.MainWindow;
import com.kosthi.wechatclient.View.Register;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
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

    private DatabaseModel database;

    private RestTemplate restTemplate;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setOnMouseClicked(e -> {
                    try {
                        new Register().show();
                    } catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
        );

        loginButton.setOnAction(event -> {
            // loginView.resetErrorTip();
            if (checkUserInfo()) {
                String loginStatus = login(account.getText(), password.getText());
                if (loginStatus.equals("密码不匹配")) {
                    passwordError.setText(loginStatus);
                } else if (loginStatus.equals("登录成功")) {
                    try {
                        new MainWindow().show();
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                } else {
                    accountError.setText("！未知错误，登录失败");
                }
            }
//            else {
//                ResultSet resultSet = null;
//                try {
//                    resultSet = database.execResult("SELECT * FROM user WHERE account=?", Account);
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//                try {
//                    if (resultSet.next()) {
//                        if (resultSet.getString(3).equals(Password)) {
//                            ResultSet set = database.execResult("SELECT * FROM dialog WHERE account = ?", Account);
//                            if (set.next()) {
//                                loginView.setErrorTip("accountError", "该账号已经登入，不能重复登入!");
//                            } else {
            // database.exec("INSERT INTO dialog VALUES(?)", Account);//登入记录
            //设置用户数据
//                                userdata.setUserdata(resultSet);
//                                userdata.setData(resultSet);
//                                //个人主页数据
//                                homepage.setUserData(userdata.getUserdata());
//                                dialog.close();
//                                //主窗口
//                                mainWindow.setHead(userdata.getHead());
//                                mainWindow.setPersonalInfo(userdata.getAccount(), userdata.getName(), userdata.getAddress(), userdata.getPhone());
//
//                                ResultSet resultSet1 = database.execResult("SELECT head,account,remark FROM user,companion WHERE account = Y_account AND I_account=?", UserName);
//
//                                //聊天助手
//                                mainWindow.addFriend("system", "WeChat聊天助手", "聊天助手");
//                                ((Label) $(mainWindow, "Y_account")).setText("WeChat聊天助手");
//                                MsgData.msg.add(new Vector<>());
//                                MsgData.accountList.add("WeChat聊天助手");
//                                MsgData.msgTip.put("WeChat聊天助手", 0);
//
//                                //所有好友
//                                while (resultSet1.next()) {
//                                    MsgData.msg.add(new Vector<>());
//                                    String temp = resultSet1.getString("account");
//                                    MsgData.accountList.add(temp);
//                                    MsgData.msgTip.put(temp, 0);
//                                    mainWindow.addFriend(resultSet1.getString("head"), resultSet1.getString("account"), resultSet1.getString("remark"), database, friendPage);
//                                }
//
//                                mainWindow.addLeft("system", "欢迎使用WeChat,赶快找好友聊天吧!");
//                                MsgData.msg.get(0).add("WeChat聊天助手 欢迎使用WeChat,赶快找好友聊天吧!");
//                                //输入框禁用
//                                ((TextField) $(mainWindow, "input")).setDisable(true);
//                                ((Button) $(mainWindow, "send")).setDisable(true);
//                                //开始选择聊天助手
//                                mainWindow.getFriendList().getSelectionModel().select(0);
//                                //获取已登入的好友
//                                ResultSet resultSet2 = database.execResult("SELECT Y_account FROM companion WHERE I_account=? AND Y_account in (SELECT account FROM dialog)", UserName);
//                                while (resultSet2.next()) {
//                                    int i = MsgData.accountList.indexOf(resultSet2.getString("Y_account"));
//                                    if (i != -1) {
//                                        mainWindow.getFriendVector().get(i).setOnline();//已登入就设置为登入状态
//                                    }
//                                }
//                                mainWindow.getFriendVector().get(0).setOnline();//否则未登入状态
//                                ChatManager.getInstance().connect("127.0.0.1", UserName);//链接服务器
//                                //设置背景
//                                //setHeadPortrait(((Button)$(mainWindow,"background")),"background",resultSet.getString("background"));
//                                mainWindow.show();
//                            }
//                        } else {
//                            loginView.setErrorTip("passwordError", "！您输入的密码有误");
//                        }
//                    } else {
//                        loginView.setErrorTip("accountError", "！账号未注册");
//                    }
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
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
        if (!checkIfUserExists(Account)) {
            accountError.setText("！错误,账号不存在");
            return false;
        }
        return true;
    }

    private boolean checkIfUserExists(String account) {
        String uri = "http://localhost:8080/exist?account=" + account;
        return Boolean.parseBoolean(OkHttpUtil.get(uri, null));
    }

    private String login(String account, String password) {
        String uri = "http://localhost:8080/login";

        // 创建请求体,并添加数据（body参数不需要时，可以省略，需要时添加到bodyParam中即可.）
        Map<String, Object> bodyParam = new HashMap<>(5);
        // 与json中的键值一致
        bodyParam.put("account", account);
        bodyParam.put("password", MD5Util.encrypt(password));

        return OkHttpUtil.postJson(uri, bodyParam);
    }
}
