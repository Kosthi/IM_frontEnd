package com.kosthi.wechatclient.View;


import com.kosthi.wechatclient.Entity.User;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Objects;

public class FriendPage extends Window {
    public FriendPage() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/FriendPage.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 385, 648);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("We Chat");
        move();
        quit();
        minimiser();
        setIcon();
    }

    public static void setHeadPortrait(Button button, String head, String file) {
        // button.setStyle(String.format("-fx-background-image: url('Fxml/CSS/Image/%s/%s.jpg')", file, head));
    }

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

    public void setFriendData(User user, String remark) throws SQLException {
        ((Label) $("account")).setText(user.getAccount());
        ((TextArea) $("label")).setText(user.getLabel());
        ((TextField) $("name")).setText(user.getUsername());
        ((TextField) $("address")).setText(user.getAddress());
        ((TextField) $("sex")).setText(String.valueOf(user.getSex()));
        ((TextField) $("age")).setText(String.valueOf(user.getAge()));
        ((TextField) $("phone")).setText(user.getTelephone());
        ((TextField) $("remark")).setText(remark);
        setHeadPortrait(((Button) $("head")), user.getHead(), "head1");
    }
}
