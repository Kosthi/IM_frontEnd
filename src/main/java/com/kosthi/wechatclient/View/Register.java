package com.kosthi.wechatclient.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;

/**
 * 注册界面类 尚未完善
 */
public class Register extends Window {

    private ToggleGroup group;

    public Register() throws IOException {
        URL url = Register.class.getResource("Fxml/Register.fxml");
        root = FXMLLoader.load(Register.class.getResource("Fxml/Register.fxml"));
        setScene(new Scene(root, 840, 530));
        getScene().setFill(Color.TRANSPARENT);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("We Chat");
        group = new ToggleGroup();
        RadioButton radioButton = ((RadioButton) $("man"));
        radioButton.setToggleGroup(group);
        ((RadioButton) $("woman")).setToggleGroup(group);
        radioButton.setSelected(true);
        radioButton.requestFocus();
        move();
        setIcon();
        quit();
        minimiser();
    }

    public static void setHeadPortrait(Button button, String head) {
        button.setStyle(String.format("-fx-background-image: url('/View/Fxml/CSS/Image/head/%s.jpg')", head));
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

    public void setErrorTip(String id, String Text) {
        ((Label) $(id)).setText(Text);
    }

    public void resetErrorTip() {
        ((Label) $("accountError")).setText("");
        ((Label) $("nameError")).setText("");
        ((Label) $("passwordError")).setText("");
        ((Label) $("rePasswordError")).setText("");
        ((Label) $("ageError")).setText("");
        ((Label) $("phoneNumberError")).setText("");
    }

    public void clear() {
        ((TextField) $("account")).clear();
        ((TextField) $("username")).clear();
        ((PasswordField) $("password")).clear();
        ((PasswordField) $("rePassword")).clear();
        ((TextField) $("age")).clear();
        ((TextField) $("phoneNumber")).clear();
        RadioButton radioButton = ((RadioButton) $("man"));
        radioButton.setSelected(true);
        radioButton.requestFocus();
        setHeadPortrait(((Button) $("HeadPortrait")), "head1");
    }
}
