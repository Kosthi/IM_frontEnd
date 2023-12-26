package com.kosthi.wechatclient.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

// 登入界面
public class Login extends Window {

    public Login() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Login.class.getResource("Fxml/Login.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 450, 480);
        // 背景透明
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        // 窗体透明
        initStyle(StageStyle.TRANSPARENT);
        // 禁止大小调整
        setResizable(false);
        setTitle("WeChat");
        // 窗体移动
        move();
        // 退出
        quit();
        // 设置图标
        setIcon();
        // 最小化
        minimiser();
    }

    /**
     * 退出
     */
    @Override
    public void quit() {
        ((Button) $("quit1")).setTooltip(new Tooltip("退出"));
        ((Button) $("quit1")).setOnAction(event -> {
            close();
            System.exit(0);
        });
    }

    @Override
    public void minimiser() {
        ((Button) $("minimiser1")).setTooltip(new Tooltip("最小化"));
        ((Button) $("minimiser1")).setOnAction(event -> setIconified(true));
    }
}
