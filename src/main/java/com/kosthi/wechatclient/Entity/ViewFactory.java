package com.kosthi.wechatclient.Entity;

import com.kosthi.wechatclient.View.*;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.Map;

public class ViewFactory {

    private static final Map<ViewType, Object> viewMap = new HashMap<>();

    public static Object createUI(ViewType type) {
        switch (type) {
            case LOGIN:
                return viewMap.computeIfAbsent(type, k -> new Login());
            case REGISTER:
                return viewMap.computeIfAbsent(type, k -> new Register());
            case MAIN:
                return viewMap.computeIfAbsent(type, k -> new MainWindow());
            case HOMEPAGE:
                return viewMap.computeIfAbsent(type, k -> new Homepage());
            case SEARCH_FRIEND:
                return viewMap.computeIfAbsent(type, k -> new SearchFriend());
            case FRIEND_PAGE:
                return viewMap.computeIfAbsent(type, k -> new FriendPage());
            default:
                throw new IllegalArgumentException("Invalid UI Type");
        }
    }

    public static void show(ViewType type) {
        ((Stage) createUI(type)).show();
    }

    public static void close(ViewType type) {
        ((Stage) createUI(type)).close();
    }
}
