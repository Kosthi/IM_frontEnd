package com.kosthi.wechatclient.View;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.paint.Color;
import javafx.stage.StageStyle;
import lombok.Getter;

import java.io.IOException;
import java.util.Objects;
import java.util.Vector;

/**
 * 搜索好友页面
 */
@Getter
public class SearchFriend extends Window {
    public static Vector<String> friendVector;
    private final Vector<ListItem> items;

    @FXML
    private ListView friendList;

    public SearchFriend() {
        try {
            root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("Fxml/SearchFriend.fxml")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Scene scene = new Scene(root, 600, 350);
        // friendList = ((ListView) $("friendList"));
        ((TextField) $("textInput")).setTooltip(new Tooltip("输入账号，回车查询"));
        items = new Vector<>();
        friendVector = new Vector<>();
        scene.setFill(Color.TRANSPARENT);
        setScene(scene);
        initStyle(StageStyle.TRANSPARENT);
        setResizable(false);
        setTitle("WeChat");
        move();
        setIcon();
        quit();
        minimiser();
    }

    public void add(String head, String account, MainWindow mainWindow) {
        items.add(new ListItem(head, account));
        int index = items.size() - 1;
        items.get(index).setActionForAdd(mainWindow);
        friendVector.add(items.get(index).getText());
        friendList.getItems().add(items.get(index).getPane());
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
}
