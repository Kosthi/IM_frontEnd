package com.kosthi.wechatclient.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class MsgData {
    // 保存消息
    public static Vector<Vector<String>> msg = new Vector<>();
    // 保存消息
    public static Map<String, Vector<String>> MsgMap = new HashMap<>();
    // 保存好友账号
    public static Vector<String> accountList = new Vector<>();//保存好友账号
    // 保存未读消息
    public static Map<String, Integer> msgTip = new HashMap<>();
}
