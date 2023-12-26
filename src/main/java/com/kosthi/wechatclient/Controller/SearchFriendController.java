package com.kosthi.wechatclient.Controller;

import com.kosthi.wechatclient.Entity.*;
import com.kosthi.wechatclient.Util.OkHttpUtil;
import com.kosthi.wechatclient.View.Alert;
import com.kosthi.wechatclient.View.ListItem;
import com.kosthi.wechatclient.View.MainWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Vector;

public class SearchFriendController implements Initializable {
    public static Vector<String> friendVector = new Vector<>();
    private final Vector<ListItem> items = new Vector<>();
    Alert alert = new Alert();
    @FXML
    private TextField textInput;
    @FXML
    private ListView friendList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        textInput.setOnKeyPressed(e -> {
            if (e.getCode() == KeyCode.ENTER && checkAccount()) {
                User user = queryUserByAccount(textInput.getText());
                if (user == null) {
                    alert.setInformation("用户未找到!");
                    alert.exec();
                    return;
                }
                // 显示查找到的用户
                // ((SearchFriend) ViewFactory.createUI(ViewType.SEARCH_FRIEND)).add(user.getHead(), user.getAccount(), (MainWindow) ViewFactory.createUI(ViewType.MAIN));
                add(user.getHead(), user.getAccount(), (MainWindow) ViewFactory.createUI(ViewType.MAIN));
            }
        });
    }

    private boolean checkAccount() {
        String account = textInput.getText();
        if (account.isEmpty()) {
            alert.setInformation("未输入账号!");
            alert.exec();
            return false;
        } else if (account.equals(UserData.currentUser.getAccount())) {
            alert.setInformation("不能输入自己的账号!");
            alert.exec();
            return false;
        }
        return true;
    }

    private User queryUserByAccount(String account) {
        String userJson = HttpRequest.queryUserByAccount(account);
        if (userJson == null) {
            return null;
        }
        return OkHttpUtil.GSON.fromJson(userJson, User.class);
    }

    public void add(String head, String account, MainWindow mainWindow) {
        // 存储每个查找结果 ListItem
        items.add(new ListItem(head, account));
        int index = items.size() - 1;
        // 为每个item设置 如果点击添加好友则添加入主窗口 事件响应
        items.get(index).setActionForAdd(mainWindow);

        // 加入全局好友列表
        friendVector.add(items.get(index).getText());

        // 显示查找结果
        friendList.getItems().add(items.get(index).getPane());
    }
}
