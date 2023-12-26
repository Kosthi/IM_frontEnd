package com.kosthi.wechatclient.View;

import com.kosthi.wechatclient.Entity.HttpRequest;
import com.kosthi.wechatclient.Entity.User;
import com.kosthi.wechatclient.Model.ChatManager;
import com.kosthi.wechatclient.Model.MsgData;
import com.kosthi.wechatclient.Util.OkHttpUtil;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Map;
import java.util.Vector;

// 好友列表项
public class friendListItem {
    private final Button head;
    private final Label information;
    private final Pane pane;
    private final Button send;
    private final Button MsgTip;
    private final Button state;
    private final String friendName;
    // 列表中每个好友对应一个 item
    Vector<MenuItem> items;
    private String friendHead;

    public friendListItem(String ihead, String iaccount, String remark) {
        head = new Button();
        information = new Label();
        pane = new Pane();
        MsgTip = new Button();
        MsgTip.setPrefSize(20, 20);
        MsgTip.setLayoutY(15);
        MsgTip.setLayoutX(275);
        state = new Button();
        state.setPrefSize(15, 15);
        state.setLayoutX(55);
        state.setLayoutY(30);
        state.getStyleClass().add("outline");
        pane.getChildren().add(head);
        pane.getChildren().add(state);
        pane.getChildren().add(information);
        MsgTip.getStyleClass().add("no-MsgTip");
        pane.getChildren().add(MsgTip);
        pane.setPrefSize(295, 50);
        head.setPrefSize(46, 46);
        head.setLayoutX(2);
        head.setLayoutY(2);
        head.setTooltip(new Tooltip("查看好友资料"));
        send = new Button();
        send.setPrefSize(295, 50);
        send.getStyleClass().add("sendMsg");
        send.setLayoutX(0);
        send.setLayoutY(0);
        information.setPrefSize(150, 30);
        information.setLayoutX(55);
        information.setLayoutY(1);
        head.getStyleClass().add("head");
        information.getStyleClass().add("information");
        pane.getStyleClass().add("ListItem");
        pane.getChildren().add(send);
        items = new Vector<>();
        items.add(new MenuItem(""));
        items.add(new MenuItem("好友资料"));
        items.add(new MenuItem("清除聊天记录"));
        items.add(new MenuItem("删除好友"));
        setHead(ihead);
        setText(remark);
        friendName = iaccount;
        setMenuItem();
    }

    public Pane getPane() {
        return pane;
    }

    public String getText() {
        return information.getText();
    }

    public void setText(String text) {
        information.setText(text);
    }

    public String getFriendName() {
        return friendName;
    }

    public Button getHead() {
        return head;
    }

    public void setHead(String head) {
        setHeadPortrait(this.head, head);
        friendHead = head;
    }

    /**
     * 在好友栏显示好友个人信息
     *
     * @param friendPage
     * @param account
     */
    public void setActionForInfo(FriendPage friendPage, String account) {
        // 选中显示好友资料
        items.get(1).setOnAction(event -> {
            if (account.equals("WeChat聊天助手")) {
            } else {
                if (friendPage.isShowing()) {
                    friendPage.close();
                }
                try {
//                    ResultSet resultSet = database.execResult("SELECT * FROM user WHERE account=?", account);
//                    resultSet.next();
                    String userJson = HttpRequest.queryUserByAccount(account);
                    if (userJson == null) {
                        return;
                    }
                    User user = OkHttpUtil.GSON.fromJson(userJson, User.class);
                    friendPage.setFriendData(user, information.getText());
                    friendPage.show();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 好友项右键菜单
     */
    public void setMenuItem() {
        ContextMenu menu = new ContextMenu();
        for (int i = 0; i < items.size(); i++) {
            menu.getItems().add(items.get(i));
        }
        send.setContextMenu(menu);
    }

    /**
     * 未读消息提示
     *
     * @param value
     */
    public void addMsgTip(int value) {
        MsgTip.getStyleClass().clear();
        MsgTip.getStyleClass().add("MsgTip");
        MsgTip.setText(" " + value);
    }

    /**
     * 清除未读消息提示
     */
    public void clearMsgTip() {
        MsgTip.getStyleClass().clear();
        MsgTip.getStyleClass().add("no-MsgTip");
        MsgTip.setText("");
    }

    /**
     * 好友上线状态
     */
    public void setOnline() {
        state.getStyleClass().clear();
        state.getStyleClass().add("online");
    }

    /**
     * 好友下线状态
     */
    public void setOutline() {
        state.getStyleClass().clear();
        state.getStyleClass().add("outline");
    }

    /**
     * 获取好友状态
     *
     * @return
     */
    public boolean getState() {
        return state.getStyleClass().equals("online");
    }

    public Vector<MenuItem> getItems() {
        return items;
    }

    /**
     * 给列表中的某人发消息的页面切换，选择特定好友
     *
     * @param mainWindow
     * @param userAccount
     * @param Head
     */
    public void setActionForSendMsg(MainWindow mainWindow, String userAccount, String Head) {
        // 为 send 针对选中不同的好友设置不同参数事件响应
        send.setOnAction(event -> {
            // System.out.println("Send resetting...");

            String friendAccount = friendName;
            ((Label) mainWindow.$("Y_account")).setText(information.getText());

            // 选中好友时，发消息按钮可用
            if (friendAccount.equals("WeChat聊天助手")) {
                ((TextField) mainWindow.$("input")).setDisable(true);
                ((Button) mainWindow.$("send")).setDisable(true);
            } else {
                ((TextField) mainWindow.$("input")).setDisable(false);
                ((Button) mainWindow.$("send")).setDisable(false);
            }

            // 获取当前好友在好友列表中的位置
            int index = MsgData.accountList.indexOf(friendAccount);
            if (index != -1) {
                // 选中
                mainWindow.getFriendList().getSelectionModel().select(index);
                // 清除消息提示
                this.clearMsgTip();
                // 未读消息清0
                MsgData.msgTip.put(friendAccount, 0);
            }

            // 放入消息映射
            for (int i = 0; i < MsgData.accountList.size(); i++) {
                MsgData.MsgMap.put(MsgData.accountList.get(i), MsgData.msg.get(i));
            }

            // 遍历消息映射
            for (Map.Entry<String, Vector<String>> entry : MsgData.MsgMap.entrySet()) {
                if (entry.getKey().equals(friendAccount)) {
                    ((ListView) mainWindow.$("ChatList")).getItems().clear();

                    Vector<String> record = entry.getValue();

                    // 遍历每条消息
                    for (int i = 0; i < record.size(); i++) {
                        String[] current = record.get(i).split(" ");
                        String account = current[0];
                        StringBuilder Msg = new StringBuilder();
                        for (int j = 1; j < current.length; j++) {
                            Msg.append(current[j]).append(" ");
                        }
                        if (account.equals(userAccount)) {
                            mainWindow.addLeft(friendHead, Msg.toString());
                        } else {
                            mainWindow.addRight(Head, Msg.toString());
                        }
                    }
                    break;
                }
            }
        });
    }

    public void setActionForClear(MainWindow mainWindow) {
        items.get(2).setOnAction(event -> {
            if (friendName.equals("WeChat聊天助手")) {
                return;
            }
            int index = MsgData.accountList.indexOf(friendName);
            if (index != -1) {
                int index1 = mainWindow.getFriendList().getSelectionModel().getSelectedIndex();
                MsgData.msg.get(index).clear();
                MsgData.msgTip.remove(friendName);
                MsgData.MsgMap.remove(friendName);
                if (index == index1) {
                    ((ListView) mainWindow.$("ChatList")).getItems().clear();
                }
            }
        });
    }

    /**
     * 邓鹏飞
     * 删除列表项的好友
     *
     * @param mainWindow
     * @param I_account
     */
    public void setActionForDelete(MainWindow mainWindow, String I_account) {
        items.get(3).setOnAction(event -> {
            int i = MsgData.accountList.indexOf(friendName);
            if (i != -1) {
                if (i == mainWindow.getFriendList().getSelectionModel().getSelectedIndex()) {
                    mainWindow.getFriendList().getSelectionModel().select(0);
                    ((Label) mainWindow.$("Y_account")).setText("WeChat聊天助手");
                }
                mainWindow.getFriendVector().remove(i);
                ((ListView) mainWindow.$("FirendList")).getItems().remove(i);
                MsgData.accountList.remove(i);
                MsgData.msg.remove(i);
                MsgData.MsgMap.remove(friendName);

            }/*
            if(((Label)mainWindow.$("Y_account")).getText().equals(information.getText())){
                        mainWindow.getFriendList().getSelectionModel().select(0);
                ((Label)mainWindow.$("Y_account")).setText("WeChat聊天助手");
            }*/
//                database.exec("DELETE FROM companion WHERE I_account = ? AND Y_account = ?", I_account, friendName);
//                database.exec("DELETE FROM companion WHERE I_account = ? AND Y_account = ?", friendName, I_account);
            try {
                ChatManager.getInstance().send("##@@ " + I_account + " " + friendName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * 标记为已读未读
     */
    public void setActionForMsgTip() {
        if (MsgTip.getStyleClass().equals("no-MsgTip")) {
            items.get(0).setText("标为未读");
        } else {
            items.get(0).setText("标为已读");
        }
        items.get(0).setOnAction(event -> {
            if (items.get(0).getText().equals("标为已读")) {
                MsgTip.getStyleClass().clear();
                MsgTip.getStyleClass().add("no-MsgTip");
                clearMsgTip();
                items.get(0).setText("标为未读");
            } else {
                MsgTip.getStyleClass().clear();
                MsgTip.getStyleClass().add("MsgTip");
                addMsgTip(1);
                items.get(0).setText("标为已读");
            }
        });
    }

    public void setHeadPortrait(Button button, String head) {
        // button.setStyle(String.format("-fx-background-image: url('Fxml/CSS/Image/head/%s.jpg')", head));
    }
}
