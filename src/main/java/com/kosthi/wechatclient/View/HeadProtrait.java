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

// 选择头像页面
public class HeadProtrait extends Window {
    private final ToggleGroup group;

    public HeadProtrait() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(HeadProtrait.class.getResource("Fxml/HeadPortrait.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 700, 440);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);

        group = new ToggleGroup();
        Group();
        setResizable(false);
        setTitle("We Chat");
        move();
        setIcon();
        quit();
        minimiser();
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

    public void Group() {
        ((RadioButton) $("one")).setToggleGroup(group);
        ((RadioButton) $("two")).setToggleGroup(group);
        ((RadioButton) $("three")).setToggleGroup(group);
        ((RadioButton) $("four")).setToggleGroup(group);
        ((RadioButton) $("five")).setToggleGroup(group);
        ((RadioButton) $("six")).setToggleGroup(group);
        ((RadioButton) $("seven")).setToggleGroup(group);
        ((RadioButton) $("eight")).setToggleGroup(group);
        ((RadioButton) $("nine")).setToggleGroup(group);
        ((RadioButton) $("ten")).setToggleGroup(group);

        ((RadioButton) $("one")).setSelected(true);
        ((RadioButton) $("one")).requestFocus();
    }
}
