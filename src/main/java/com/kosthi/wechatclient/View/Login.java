package com.kosthi.wechatclient.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;

// 登入界面
public class Login extends Window {
    @FXML
    public TextField account;

    @FXML
    public PasswordField password;

    @FXML
    public Button loginButton;

    public Login() throws IOException {
        root = FXMLLoader.load(Login.class.getResource("Fxml/Login.fxml"));
        Scene scene = new Scene(root, 450, 480);
        // 背景透明
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        // 窗体透明
        initStyle(StageStyle.TRANSPARENT);
        // 禁止大小调整
        setResizable(false);
        setTitle("We Chat");
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
        ((Button) $("minimiser1")).setOnAction(event -> {
            setIconified(true);
        });
    }

    /**
     * 设置错误提示
     *
     * @param id
     * @param Text
     */
    public void setErrorTip(String id, String Text) {
        ((Label) $(id)).setText(Text);
    }

    /**
     * 重置错误提醒
     */
    public void resetErrorTip() {
        setErrorTip("accountError", "");
        setErrorTip("passwordError", "");
    }

    /*
    清除各种输入框
     */
    public void clear() {
//        ((TextField) $("UserName")).clear();
//        ((PasswordField) $("Password")).clear();
        account.clear();
        password.clear();
    }

    public void clear(String id) {
        ((TextField) $(id)).clear();
    }
}
