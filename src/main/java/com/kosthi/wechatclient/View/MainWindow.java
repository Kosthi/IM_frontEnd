package com.kosthi.wechatclient.View;

import com.kosthi.wechatclient.Entity.UserData;
import com.kosthi.wechatclient.Util.Tool;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

// 主窗口
public class MainWindow extends Window {
    private final ListView friendList;
    private final ListView chatList;
    private final Vector<friendListItem> friendVector;
    private final ContextMenu contextMenu;
    private final Vector<MenuItem> rightItem;
    private Vector<ChatListItem> chatVector;
    private double xOffset;
    private double yOffset;

    public MainWindow() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/MainWindow.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 1400, 700);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("WeChat");
        friendList = ((ListView) $("FirendList"));
        chatList = ((ListView) $("ChatList"));
        friendVector = new Vector<>();
        contextMenu = new ContextMenu();
        rightItem = new Vector<>();
        rightItem.add(new MenuItem("  添加好友  "));
        rightItem.add(new MenuItem("  设置      "));
        rightItem.add(new MenuItem("  最小化    "));
        rightItem.add(new MenuItem("  退出      "));
//        BackgroundMenu();
        setIcon();
        move();
        quit();
        minimiser();
        initTooltip();
    }

    // 使用默认头像
    public static void setHeadPortrait(Button button, String head) {
//        String format = String.format("-fx-background-image: url(\"/View/Fxml/CSS/Image/head/%s.jpg\")", head);
//        button.setStyle(format);
//        System.out.println(button.getStyle());
    }

//    public static void setHeadPortrait(Button button, String background, String file) {
//        button.setStyle(String.format("-fx-background-image: url('Fxml/CSS/Image/%s/%s.jpg')", file, background));
//    }

    public void initTooltip() {
        ((Button) $("addFriend")).setTooltip(new Tooltip("添加好友"));
        ((Button) $("setting")).setTooltip(new Tooltip("设置"));
        ((Button) $("individual")).setTooltip(new Tooltip("个人资料"));
        ((Button) $("moreFriend")).setTooltip(new Tooltip("好友资料"));
        ((TextField) $("search")).setTooltip(new Tooltip("查找好友"));
        ((Button) $("send")).setTooltip(new Tooltip("发送"));
    }

    // 别人的消息
    public void addLeft(String head, String Msg) {
        chatList.getItems().add(new ChatListItem().Left(head, Msg, Tool.getWidth(Msg), Tool.getHight(Msg)));
    }

    // 自己的消息
    public void addRight(String head, String Msg) {
        chatList.getItems().add(new ChatListItem().Right(head, Msg, Tool.getWidth(Msg), Tool.getHight(Msg)));
    }

    public void setHead(String Head) {
        setHeadPortrait(((Button) $("individual")), Head);
    }

    @Override
    public void quit() {//退出
        // ((Button) $("quit1")).setTooltip(new Tooltip("退出"));
    }

    // 窗口最小化
    @Override
    public void minimiser() {
        ((Button) $("minimiser1")).setTooltip(new Tooltip("最小化"));
        ((Button) $("minimiser1")).setOnAction(event -> {
            setIconified(true);
        });
    }

    /**
     * 主窗口添加好友
     *
     * @param head
     * @param account
     * @param friendPage
     */
    public int addFriend(String head, String account, String remark, FriendPage friendPage) {
        friendVector.add(new friendListItem(head, account, remark));

        int index = friendVector.size() - 1;

        // 为新的好友item设置事件响应
        // 提示窗
        friendVector.get(index).setActionForMsgTip();
        // 个人资料
        friendVector.get(index).setActionForInfo(friendPage, account);
        // 发送消息
        friendVector.get(index).setActionForSendMsg(this, account, UserData.currentUser.getHead());
        // 清除聊天记录
        friendVector.get(index).setActionForClear(this);
        // 删除好友
        friendVector.get(index).setActionForDelete(this, UserData.currentUser.getAccount());

        friendList.getItems().add(friendVector.get(index).getPane());
        return index;
    }

    public void addFriend(String head, String account, String remark) {
        int index = friendVector.size();
        friendVector.add(new friendListItem(head, account, remark));
        friendVector.get(index).setActionForSendMsg(this, account, head);
        friendVector.get(index).setActionForMsgTip();
        friendList.getItems().add(friendVector.get(friendVector.size() - 1).getPane());
    }

    public Vector<friendListItem> getFriendVector() {
        return friendVector;
    }

    public ListView getFriendList() {
        return friendList;
    }

    public void setPersonalInfo(String account, String name, String address, String phone) {
        ((Label) $("myAccount")).setText(account);
        ((Label) $("myName")).setText(name);
        ((Label) $("myAddress")).setText(address);
        ((Label) $("myPhone")).setText(phone);
    }
}

// 聊天列表项类
class ChatListItem {
    private final Pane pane;
    private final Button head;
    private final TextArea text;
    private final Pane left;
    private final Pane right;
    private final Button arrow;

    public ChatListItem() {
        pane = new Pane();
        head = new Button();
        text = new TextArea();
        pane.setPrefSize(730, 150);
        left = new Pane();
        right = new Pane();
        arrow = new Button();
        arrow.setDisable(false);
        arrow.setPrefSize(32, 32);
        left.setPrefSize(580, 70);
        right.setPrefSize(580, 70);
        head.getStyleClass().add("head");
        pane.getStyleClass().add("pane");
        left.getStyleClass().add("pane");
        right.getStyleClass().add("pane");
        head.setPrefSize(50, 50);
        text.setPrefSize(480, 50);
        text.setWrapText(true);
        text.setEditable(false);
    }

    // 别人的消息
    public Pane Left(String ihead, String itext, double width, double hight) {
        text.getStyleClass().add("lefttext");
        arrow.getStyleClass().add("leftarrow");
        pane.setPrefHeight(110 + hight);
        left.setPrefHeight(30 + hight);
        head.setLayoutY(10);
        head.setLayoutX(10);
        text.setPrefSize(width, hight);
        text.setLayoutX(100);
        text.setLayoutY(30);
        arrow.setLayoutY(40);
        arrow.setLayoutX(85);
        text.setText(itext);
        MainWindow.setHeadPortrait(head, ihead);

        left.getChildren().add(head);
        left.getChildren().add(text);
        left.getChildren().add(arrow);
        pane.getChildren().add(left);
        return pane;
    }

    // 自己的消息
    public Pane Right(String ihead, String itext, double width, double hight) {
        text.getStyleClass().add("righttext");
        arrow.getStyleClass().add("rightarrow");
        head.setLayoutY(10);
        head.setLayoutX(510);
        pane.setPrefHeight(110 + hight);
        right.setPrefHeight(30 + hight);
        text.setPrefSize(width, hight);
        //text=480 0 text = 470 10 460 20 480-width
        text.setLayoutY(30);
        text.setLayoutX(480 - width);
        arrow.setLayoutY(40);
        arrow.setLayoutX(475);
        text.setText(itext);
        MainWindow.setHeadPortrait(head, ihead);

        right.getChildren().add(head);
        right.getChildren().add(text);
        right.getChildren().add(left);
        right.getChildren().add(arrow);
        right.setLayoutX(150);
        pane.getChildren().add(right);
        return pane;
    }
}
