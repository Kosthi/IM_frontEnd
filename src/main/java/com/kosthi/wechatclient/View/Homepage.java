package com.kosthi.wechatclient.View;

import com.kosthi.wechatclient.Entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * 个人主页
 */
public class Homepage extends Window {
    public Homepage() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/Homepage.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 600, 700);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("We Chat");
        ((Button) $("alter")).setTooltip(new Tooltip("修改资料"));
        move();
        quit();
        setIcon();
        minimiser();
    }

    public static void setHeadPortrait(Button button, String head) {
        // button.setStyle(String.format("-fx-background-image: url('Fxml/CSS/Image/head/%s.jpg')", head));
    }

//    public static void setHeadPortrait(Button button, String head, String file) {
//        button.setStyle(String.format("-fx-background-image: url('Fxml/CSS/Image/%s/%s.jpg')", file, head));
//    }

    /**
     * 退出
     */
    @Override
    public void quit() {
        ((Button) $("quit1")).setTooltip(new Tooltip("关闭"));
        ((Button) $("quit1")).setOnAction(event -> close());
    }

    @Override
    public void minimiser() {
        ((Button) $("minimiser1")).setTooltip(new Tooltip("最小化"));
        ((Button) $("minimiser1")).setOnAction(event -> setIconified(true));
    }

//    public void setUserData(String id, String text) {
//        if (id.equals("account")) {
//            ((Label) $(id)).setText(text);
//        } else if (id.equals("label")) {
//            ((TextArea) $(id)).setText(text);
//        } else {
//            ((TextField) $(id)).setText(text);
//        }
//    }

    public void setUserData(User user) {
        ((Label) $("account")).setText(user.getAccount());
        ((TextField) $("name")).setText(user.getUsername());
        ((TextField) $("address")).setText(user.getAddress());
        ((TextField) $("sex")).setText(String.valueOf(user.getSex()));
        ((TextField) $("age")).setText(String.valueOf(user.getAge()));
        ((TextField) $("phone")).setText(user.getTelephone());
        ((TextArea) $("label")).setText(user.getLabel());
        setHeadPortrait(((Button) $("head")), user.getHead());
        setHeadPortrait(((Button) $("background")), user.getBackground());
    }
}
