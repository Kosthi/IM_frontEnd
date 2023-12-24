package com.kosthi.wechatclient.Main;

import com.kosthi.wechatclient.View.Login;
import javafx.application.Application;
import javafx.stage.Stage;

public class Main extends Application {
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        // new Controller().exec();
        new Login().show();
    }
}
