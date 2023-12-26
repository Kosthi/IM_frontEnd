package com.kosthi.wechatclient;

import com.kosthi.wechatclient.Entity.ViewFactory;
import com.kosthi.wechatclient.Entity.ViewType;
import com.kosthi.wechatclient.Model.ChatManager;
import com.kosthi.wechatclient.View.MainWindow;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class ChatApplication extends Application {
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        ViewFactory.show(ViewType.LOGIN);
        ChatManager.getInstance().setMainWindow((MainWindow) ViewFactory.createUI(ViewType.MAIN));
    }
}
