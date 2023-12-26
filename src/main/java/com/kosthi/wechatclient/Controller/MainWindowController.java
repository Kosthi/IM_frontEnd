package com.kosthi.wechatclient.Controller;

import com.kosthi.wechatclient.Entity.HttpRequest;
import com.kosthi.wechatclient.Entity.UserData;
import com.kosthi.wechatclient.Entity.ViewFactory;
import com.kosthi.wechatclient.Entity.ViewType;
import com.kosthi.wechatclient.Model.ChatManager;
import com.kosthi.wechatclient.Model.MsgData;
import com.kosthi.wechatclient.View.Alert;
import com.kosthi.wechatclient.View.MainWindow;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainWindowController implements Initializable {
    @FXML
    private Button quit1;

    @FXML
    private Button moreButton;

    @FXML
    private Button addFriend;

    @FXML
    private Button send;

    @FXML
    private TextField input;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        moreButton.setOnMouseClicked(e -> ViewFactory.show(ViewType.HOMEPAGE));

        addFriend.setOnMouseClicked(e -> ViewFactory.show(ViewType.SEARCH_FRIEND));

        quit1.setOnMouseClicked(e -> {
            // close();
            // 更新下线信息
            System.out.println("exiting...");
            if (HttpRequest.updateUserStatus(UserData.currentUser.getAccount(), false) == null) {
                throw new RuntimeException();
            }
            // 广播下线信息
            try {
                ChatManager.getInstance().send("#### " + UserData.currentUser.getAccount() + " ####");
            } catch (IOException ee) {
                ee.printStackTrace();
            }
            System.exit(0);
        });

        // 按下发送消息
        send.setOnAction(event -> sendMsg());

        // 支持Enter发送消息
        input.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                sendMsg();
            }
        });
    }

    private void sendMsg() {

        MainWindow mainWindow = (MainWindow) ViewFactory.createUI(ViewType.MAIN);

        // 得到好友账号名字
        String toAccount = MsgData.accountList.get(mainWindow.getFriendList().getSelectionModel().getSelectedIndex());

        // 判断是否在线
        if (HttpRequest.checkIfUserOnline(toAccount)) {
            // 若在线则通过socket发送消息
            String message = input.getText();
            if (!message.isEmpty()) {
                String line = UserData.currentUser.getAccount() + " " + toAccount + " " + message;
                // 添加自己的消息
                mainWindow.addRight(UserData.currentUser.getHead(), message);
                try {
                    // 向服务器发消息
                    ChatManager.getInstance().send(line);
                    // 添加到消息集
                    int i = MsgData.accountList.indexOf(toAccount);
                    if (i != -1) {
                        MsgData.msg.get(i).add(UserData.currentUser.getAccount() + " " + message);
                    }
                    // 清空输入框
                    input.clear();
                } catch (IOException e) {
                    Alert alert = new Alert();
                    alert.setInformation("你断开了链接!");
                    alert.exec();
                    e.printStackTrace();
                }
            }
//                } else {
//                    alert.setInformation("对方暂时不在线，你发的消息对方无法接收!");
//                    alert.exec();
//                }
        }
    }
}
