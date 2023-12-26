package com.kosthi.wechatclient.Model;

import com.kosthi.wechatclient.Entity.HttpRequest;
import com.kosthi.wechatclient.Entity.User;
import com.kosthi.wechatclient.Entity.ViewFactory;
import com.kosthi.wechatclient.Entity.ViewType;
import com.kosthi.wechatclient.Util.OkHttpUtil;
import com.kosthi.wechatclient.View.Alert;
import com.kosthi.wechatclient.View.FriendPage;
import com.kosthi.wechatclient.View.MainWindow;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Vector;

/**
 * 接收信息类 多线程接收服务端的信息
 */
public class ChatManager {
    private static final ChatManager instance = new ChatManager();
    private String ip = "127.0.0.1";
    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private MainWindow mainWindow;

    private ChatManager() {
    }

    public static ChatManager getInstance() {
        return instance;
    }

    public void setMainWindow(MainWindow mainWindow) {
        this.mainWindow = mainWindow;
    }

    /**
     * 链接服务器
     *
     * @param ip
     * @param account
     */
    public void connect(String ip, String account) {
        this.ip = ip;

        // 创建匿名线程负责与服务端通信
        new Thread(() -> {
            try {
                socket = new Socket(ip, 2347);
                in = new DataInputStream(socket.getInputStream());
                out = new DataOutputStream(socket.getOutputStream());
                out.writeUTF(account);

                String line;
                while ((line = in.readUTF()) != null) {
                    System.out.println(line);
                    String[] str = line.split(" ");
                    String fromAccount = str[0];
                    String Y_account = str[1];
                    StringBuilder MsgBuilder = new StringBuilder();
                    for (int i = 2; i < str.length; i++) {
                        MsgBuilder.append(str[i]).append(" ");
                    }
                    String Msg = MsgBuilder.toString();

                    // 获取信息格式
                    // 别人把你添加为好友的信息
                    if (Msg.equals("###@ ")) {

                        String userJson = HttpRequest.queryUserByAccount(fromAccount);
                        if (userJson == null) {
                            continue;
                        }
                        User user = OkHttpUtil.GSON.fromJson(userJson, User.class);

                        // 添加到主窗口好友item
                        mainWindow.addFriend(user.getHead(), fromAccount, "little remark", (FriendPage) ViewFactory.createUI(ViewType.FRIEND_PAGE));

                        // 加入账号表
                        MsgData.accountList.add(fromAccount);
                        // 初始化信息
                        MsgData.msg.add(new Vector<>());
                        // 设置主窗口中好友列的登录状态
                        mainWindow.getFriendVector().get(MsgData.accountList.size() - 1).setOnline();

                        // 当前窗口是聊天助手，则提示并加入消息列表
                        if (((Label) mainWindow.$("Y_account")).getText().equals("WeChat聊天助手")) {
                            String msg = fromAccount + "把你添加为好友,和他聊天吧";
                            mainWindow.addLeft("system", msg);
                            MsgData.msg.get(0).add("WeChat聊天助手 " + msg);
                        } else {
                            // 否则，加入消息列表，并更新未读消息
                            String msg = fromAccount + "把你添加为好友,和他聊天吧";
                            MsgData.msg.get(0).add("WeChat聊天助手 " + msg);

                            int cnt = MsgData.msgTip.get("WeChat聊天助手");
                            ++cnt;
                            MsgData.msgTip.put("WeChat聊天助手", cnt);
                            mainWindow.getFriendVector().get(0).addMsgTip(cnt);
                        }
                        // 初始化未读消息为0
                        MsgData.msgTip.put(fromAccount, 0);
                    } else if (Msg.equals("##@@ ")) // 别人把你删除的信息
                    {
                        // 当前窗口是聊天助手，则提示并加入消息列表
                        if (((Label) mainWindow.$("Y_account")).getText().equals("WeChat聊天助手")) {
                            String msg = fromAccount + "已把你删除";
                            mainWindow.addLeft("system", msg);
                            MsgData.msg.get(0).add("WeChat聊天助手 " + msg);

                            // 从好友列表中移除，并清除相关数据缓存
                            int index = MsgData.accountList.indexOf(fromAccount);
                            if (index != -1) {
                                mainWindow.getFriendVector().remove(index);
                                ((ListView) mainWindow.$("FirendList")).getItems().remove(index);
                                MsgData.accountList.remove(index);
                                MsgData.msg.remove(index);
                                MsgData.MsgMap.remove(fromAccount);
                            }
                        } else {
                            // 否则，加入消息列表，并更新未读消息
                            String msg = fromAccount + "已把你删除";
                            MsgData.msg.get(0).add("WeChat聊天助手 " + msg);
                            int cnt = MsgData.msgTip.get("WeChat聊天助手");
                            cnt++;
                            MsgData.msgTip.put("WeChat聊天助手", cnt);
                            mainWindow.getFriendVector().get(0).addMsgTip(cnt);
                            if (((Label) mainWindow.$("Y_account")).getText().equals(fromAccount)) {
                                mainWindow.getFriendList().getSelectionModel().select(0);
                            }
                            int i = MsgData.accountList.indexOf(fromAccount);
                            if (i != -1) {
                                mainWindow.getFriendVector().remove(i);
                                ((ListView) mainWindow.$("FirendList")).getItems().remove(i);
                                MsgData.accountList.remove(i);
                                MsgData.msg.remove(i);
                                MsgData.MsgMap.remove(fromAccount);
                            }
                        }
                    } else if (Msg.equals("#@@@ ")) { //有用户上线的信息
                        int i = MsgData.accountList.indexOf(fromAccount);
                        if (i != -1) {
                            mainWindow.getFriendVector().get(i).setOnline();
                        }
                    } else if (Msg.equals("@@@@ ")) //用户下线信息
                    {
                        int i = MsgData.accountList.indexOf(fromAccount);
                        if (i != -1) {
                            mainWindow.getFriendVector().get(i).setOutline();
                        }
                    } else {//一般信息
                        // 如果消息发送方正好是当前选中的好友，显示消息并加入消息容器中
                        if (MsgData.accountList.get(mainWindow.getFriendList().getSelectionModel().getSelectedIndex()).equals(fromAccount)) {
                            // 查表获取头像
                            String userJson = HttpRequest.queryUserByAccount(fromAccount);
                            User fromUser = OkHttpUtil.GSON.fromJson(userJson, User.class);
                            // 显示消息 位置左
                            mainWindow.addLeft(fromUser.getHead(), Msg);
                            // 加入消息容器中
                            int i = MsgData.accountList.indexOf(fromAccount);
                            if (i != -1) {
                                MsgData.msg.get(i).add(fromAccount + " " + Msg);
                            }
                        } else {
                            // 否则，加入消息容器并更新未读消息数量
                            int i = MsgData.accountList.indexOf(fromAccount);
                            if (i != -1) {
                                MsgData.msg.get(i).add(fromAccount + " " + Msg);
                                // 未读消息加1
                                int cnt = MsgData.msgTip.get(fromAccount);
                                cnt++;
                                MsgData.msgTip.put(fromAccount, cnt);
                                // 显示更新后的未读消息
                                mainWindow.getFriendVector().get(i).addMsgTip(cnt);
                            }
                        }
                    }
                }
                in.close();
                out.close();
                in = null;
                out = null;
                System.out.println("Socket closing...");
            } catch (IOException e) {
                System.out.println("好吧");
            }
        }).start();
    }

    // 发送消息  向服务器发送
    public void send(String Msg) throws IOException {
        if (out != null) {
            out.writeUTF(Msg);
            // 强制把缓冲区数据发送
            out.flush();
        } else {
            Alert alert = new Alert();
            alert.setInformation("发送失败!");
            alert.exec();
        }
    }
}
