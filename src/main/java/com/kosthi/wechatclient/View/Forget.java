package com.kosthi.wechatclient.View;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.util.Objects;

/**
 * 忘记密码
 */
public class Forget extends Window {

    public Forget() throws IOException {
        root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/Forget.fxml")));
        Scene scene = new Scene(root, 700, 450);
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("We Chat");
        move();
        quit();
        setIcon();
        minimiser();
    }


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
