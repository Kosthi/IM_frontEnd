package com.kosthi.wechatclient.View;

import javafx.scene.Cursor;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;

// 所有窗口的父类
public abstract class Window extends Stage {
    Parent root;
    private double xOffset;
    private double yOffset;

    /**
     * 设置图标
     */
    public void setIcon() {
        getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("Fxml/CSS/Image/icon.png"))));
    }

    // 窗口拖动
    // 计算鼠标与当前窗口的xy偏移值
    // 出现鼠标拖动事件则把当前窗口的位置加上这个偏移值
    public void move() {
        root.setOnMousePressed(event -> {
            xOffset = getX() - event.getScreenX();
            yOffset = getY() - event.getScreenY();
            getRoot().setCursor(Cursor.CLOSED_HAND);
        });
        root.setOnMouseDragged(event -> {
            setX(event.getScreenX() + xOffset);
            setY(event.getScreenY() + yOffset);
        });
        root.setOnMouseReleased(event -> root.setCursor(Cursor.DEFAULT));
    }

    /**
     * 抽象方法  窗口退出操作
     */
    abstract public void quit();

    /**
     * 最小化
     */
    abstract public void minimiser();

    /**
     * 获取root
     *
     * @return
     */
    public Parent getRoot() {
        return root;
    }

    /**
     * 选择界面元素
     *
     * @param id
     * @return
     */
    public Object $(String id) {
        return root.lookup("#" + id);
    }
}
