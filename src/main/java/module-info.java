module com.kosthi.wechatclient {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.java;
    requires junit;
    requires spring.web;
    requires com.fasterxml.jackson.annotation;
    requires lombok;
    requires okhttp3;
    requires spring.core;
    requires java.annotation;
    requires org.slf4j;
    requires com.google.gson;
    requires org.apache.commons.codec;

    opens com.kosthi.wechatclient to javafx.fxml;
    exports com.kosthi.wechatclient;
    exports com.kosthi.wechatclient.Controller;
    opens com.kosthi.wechatclient.Controller to javafx.fxml;
    opens com.kosthi.wechatclient.View to javafx.fxml;
    exports com.kosthi.wechatclient.Main to javafx.graphics;
    exports com.kosthi.wechatclient.Model;
    exports com.kosthi.wechatclient.Model.Data;
    exports com.kosthi.wechatclient.View;
    exports com.kosthi.wechatclient.Test;
}