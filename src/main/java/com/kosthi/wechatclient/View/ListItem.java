package com.kosthi.wechatclient.View;

import com.kosthi.wechatclient.Entity.HttpRequest;
import com.kosthi.wechatclient.Entity.UserData;
import com.kosthi.wechatclient.Entity.ViewFactory;
import com.kosthi.wechatclient.Entity.ViewType;
import com.kosthi.wechatclient.Model.ChatManager;
import com.kosthi.wechatclient.Model.MsgData;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.Pane;
import lombok.Getter;

import java.io.IOException;
import java.util.Vector;

/**
 * 查找好友的列表项
 */
public class ListItem {
    private final Button headButton;
    private final Label information;
    @Getter
    private final Button addFriendButton;
    @Getter
    private final Pane pane;
    @Getter
    private String friendHead;

    public ListItem(String ihead, String iaccount) {
        headButton = new Button();
        addFriendButton = new Button();
        information = new Label();
        pane = new Pane();
        pane.getChildren().add(headButton);
        pane.getChildren().add(information);
        pane.setPrefSize(535, 60);
        headButton.setPrefSize(56, 56);
        addFriendButton.setPrefSize(32, 32);
        addFriendButton.getStyleClass().add("add");
        addFriendButton.setLayoutX(503);
        addFriendButton.setLayoutY(14);
        addFriendButton.setTooltip(new Tooltip("添加好友"));
        pane.getChildren().add(addFriendButton);
        headButton.setLayoutX(2);
        headButton.setLayoutY(2);
        information.setPrefSize(200, 32);
        information.setLayoutX(65);
        information.setLayoutY(5);
        headButton.getStyleClass().add("head");
        information.getStyleClass().add("information");
        pane.getStyleClass().add("ListItem");
        setText(iaccount);
        setHead(ihead);
    }

    public String getText() {
        return information.getText();
    }

    public void setText(String text) {
        information.setText(text);
    }

    public void setHead(String head) {
        setHeadPortrait(this.headButton, head);
        friendHead = head;
    }

    public void setHeadPortrait(Button button, String head) {
        // button.setStyle(String.format("-fx-background-image: url('Fxml/CSS/Image/head/%s.jpg')", head));
    }

    /**
     * 添加好友到主页面
     *
     * @param mainWindow
     */
    public void setActionForAdd(MainWindow mainWindow) {
        addFriendButton.setOnAction(event -> {
            String friendAccount = information.getText();

            // 先检查是否已经是好友
            if (HttpRequest.checkIfFriend(UserData.currentUser.getAccount(), friendAccount)) {
                Alert alert = new Alert();
                alert.setInformation("已经是好友了");
                alert.exec();
                return;
            }

            // 从好友搜索结果中移除
            int index = SearchFriend.friendVector.indexOf(friendAccount);
            if (index != -1) {
                ((SearchFriend) ViewFactory.createUI(ViewType.SEARCH_FRIEND)).getFriendList().getItems().remove(index);
                SearchFriend.friendVector.remove(index);
            }

            // 加消息
            MsgData.msg.add(new Vector<>());
            MsgData.accountList.add(friendAccount);
            MsgData.msgTip.put(friendAccount, 0);

            // 如果好友用户已经登录，则推送消息
            try {
                ChatManager.getInstance().send("###@ " + UserData.currentUser.getAccount() + " " + friendAccount);
            } catch (IOException e) {
                Alert alert = new Alert();
                alert.setInformation("! 添加好友失败");
                alert.exec();
                e.printStackTrace();
            }

            // 添加好友关系
            if (HttpRequest.addFriend(UserData.currentUser.getAccount(), friendAccount) == null) {
                System.out.println("添加好友失败");
            }

            // 主窗口显示新好友
            index = mainWindow.addFriend(friendHead, friendAccount, friendAccount, (FriendPage) ViewFactory.createUI(ViewType.FRIEND_PAGE));

            // 是否在线
            boolean isOnline = HttpRequest.checkIfUserOnline(friendAccount);
            System.out.println(friendAccount + " " + isOnline);
            System.out.println(MsgData.accountList.size());

            if (isOnline) {
                mainWindow.getFriendVector().get(index).setOnline();
            } else {
                mainWindow.getFriendVector().get(index).setOutline();
            }

//            if (isOnline) {
//                mainWindow.getFriendVector().get(MsgData.accountList.size() - 1).setOnline();
//            } else {
//                mainWindow.getFriendVector().get(MsgData.accountList.size() - 1).setOutline();
//            }
        });
    }
}
