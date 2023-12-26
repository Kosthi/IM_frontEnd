package com.kosthi.wechatclient.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * 注册界面类 未完善
 */
public class Register extends Window {

    private final ToggleGroup group;

    public Register() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(Register.class.getResource("Fxml/Register.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        setScene(new Scene(root, 840, 530));
        getScene().setFill(Color.TRANSPARENT);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("WeChat");

        group = new ToggleGroup();
        RadioButton radioButton = ((RadioButton) $("man"));
        radioButton.setToggleGroup(group);
        ((RadioButton) $("woman")).setToggleGroup(group);
        radioButton.setSelected(true);
        // 默认选中：男
        radioButton.requestFocus();

        move();
        setIcon();
        quit();
        minimiser();
    }

    public static void setHeadPortrait(Button button, String head) {
        // button.setStyle(String.format("-fx-background-image: url('Fxml/CSS/Image/head/%s.jpg')", head));
    }

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
