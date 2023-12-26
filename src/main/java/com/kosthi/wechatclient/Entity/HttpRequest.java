package com.kosthi.wechatclient.Entity;

import com.kosthi.wechatclient.Util.MD5Util;
import com.kosthi.wechatclient.Util.OkHttpUtil;

import java.util.HashMap;
import java.util.Map;

public class HttpRequest {

    public static String login(String account, String password) {
        String uri = "http://localhost:8080/login";

        // 创建请求体,并添加数据（body参数不需要时，可以省略，需要时添加到bodyParam中即可.）
        Map<String, Object> bodyParam = new HashMap<>(2);
        // 与json中的键值一致
        bodyParam.put("account", account);
        bodyParam.put("password", MD5Util.encrypt(password));

        return OkHttpUtil.postJson(uri, bodyParam);
    }

    public static boolean checkIfUserExists(String account) {
        String uri = "http://localhost:8080/exist?account=" + account;
        return Boolean.parseBoolean(OkHttpUtil.get(uri, null));
    }

    public static boolean checkIfUserOnline(String account) {
        String uri = "http://localhost:8080/online?account=" + account;
        return Boolean.parseBoolean(OkHttpUtil.get(uri, null));
    }

    public static String addFriend(String from, String to) {
        String uri = "http://localhost:8080/addFriend?from=" + from + "&to=" + to;
        return OkHttpUtil.postJson(uri, null);
    }

    public static boolean checkIfFriend(String from, String to) {
        String uri = "http://localhost:8080/friend?from=" + from + "&to=" + to;
        return Boolean.parseBoolean(OkHttpUtil.get(uri, null));
    }

    public static boolean registerUser(User user) {
        String uri = "http://localhost:8080/register";

        // 创建请求体,并添加数据（body参数不需要时，可以省略，需要时添加到bodyParam中即可.）
        Map<String, Object> bodyParam = new HashMap<>(5);
        // 与json中的键值一致
        bodyParam.put("account", user.getAccount());
        bodyParam.put("username", user.getUsername());
        bodyParam.put("password", user.getPassword());
        bodyParam.put("sex", user.getSex());
        bodyParam.put("age", user.getAge());
        bodyParam.put("telephone", user.getTelephone());

        return OkHttpUtil.postJson(uri, bodyParam) != null;
    }

    public static String queryUserByAccount(String account) {
        String uri = "http://localhost:8080/user?account=" + account;
        return OkHttpUtil.get(uri, null);
    }

    public static String updateUserStatus(String account, Boolean isOnline) {
        String uri = "http://localhost:8080/status";
        Map<String, Object> body = new HashMap<>();
        body.put("account", account);
        body.put("isOnline", isOnline);
        return OkHttpUtil.put(uri, body);
    }

    public static String queryAllFriends(String account) {
        String uri = "http://localhost:8080/getFriends?account=" + account;
        return OkHttpUtil.get(uri, null);
    }
}
