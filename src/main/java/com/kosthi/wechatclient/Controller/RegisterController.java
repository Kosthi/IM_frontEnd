package com.kosthi.wechatclient.Controller;

import com.kosthi.wechatclient.Entity.User;
import com.kosthi.wechatclient.Util.MD5Util;
import com.kosthi.wechatclient.Util.OkHttpUtil;
import com.kosthi.wechatclient.View.Register;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

public class RegisterController implements Initializable {
    @FXML
    private TextField account;
    @FXML
    private Label phoneNumberError;
    @FXML
    private TextField age;
    @FXML
    private TextField username;
    @FXML
    private PasswordField password;
    @FXML
    private PasswordField rePassword;
    @FXML
    private TextField phoneNumber;
    @FXML
    private RadioButton womanButton;
    @FXML
    private Label usernameError;
    @FXML
    private RadioButton manButton;
    @FXML
    private Label rePasswordError;
    @FXML
    private Label accountError;
    @FXML
    private Label ageError;
    @FXML
    private Label passwordError;
    @FXML
    private Button backButton;
    @FXML
    private Button chooseHeadButton;
    @FXML
    private Button registerButton;

    private Register registerView;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        registerButton.setOnMouseClicked(e -> {
            if (checkInfo()) {
                Character sex = manButton.isSelected() ? '男' : '女';
                User user = User.builder()
                        .account(account.getText())
                        .password(MD5Util.encrypt(password.getText()))
                        .username(username.getText())
                        .sex(sex)
                        .age(Byte.valueOf(age.getText()))
                        .telephone(phoneNumber.getText())
                        .build();
                registerUser(user);
//                if (registerUser(user)) {
//                    new Login().show();
//                }
            }
        });
    }

    private void clearAllError() {
        accountError.setText("");
        usernameError.setText("");
        passwordError.setText("");
        rePasswordError.setText("");
        ageError.setText("");
        phoneNumberError.setText("");
    }

    private boolean checkInfo() {
        clearAllError();

        String Account = account.getText();
        String Username = username.getText();
        String Password = password.getText();
        String RePassword = rePassword.getText();
        String Age = age.getText();
        String sex;
        String PhoneNumber = phoneNumber.getText();

        String accountRegExp = "^[0-9a-zA-Z,\\u4e00-\\u9fa5]{1,15}$";
        String nameRegExp = "^[a-zA-Z\\u4e00-\\u9fa5]{1,100}$";
        String phoneRegExp = "^1[3-9]\\d{9}$|" +
                "^1[3-9]\\d{1}[-\\s]\\d{4}[-\\s]\\d{4}$|" +
                "^\\(1[3-9]\\d{1}\\)\\d{4}-\\d{4}$|" +
                "^(?:\\(\\+\\d{2}\\)|\\+\\d{2})(\\d{11})$|" +
                "^0\\d{3}-\\d{7}$|" +
                "^0\\d{2}-\\d{8}$";
        String ageRegExp = "^\\d{1,3}$";
        String passwordReExp = "^[a-zA-Z0-9]{6,20}$";
        String rePasswordRegExp = "^[a-zA-Z0-9]{6,20}$";

        if (Account.isEmpty() || Username.isEmpty() || Password.isEmpty() || RePassword.isEmpty() || Age.isEmpty() || PhoneNumber.isEmpty()) {
            if (Account.isEmpty()) {
                accountError.setText("！未输入账号");
            }
            if (Username.isEmpty()) {
                usernameError.setText("！未输入姓名");
            }
            if (Password.isEmpty()) {
                passwordError.setText("！未输入密码");
            }
            if (RePassword.isEmpty()) {
                rePasswordError.setText("！未输入密码");
            }
            if (Age.isEmpty()) {
                ageError.setText("！未输入年龄");
            }
            if (PhoneNumber.isEmpty()) {
                phoneNumberError.setText("！未输入电话号");
            }
            return false;
        } else if (!Pattern.matches(accountRegExp, Account) || !Pattern.matches(nameRegExp, Username) || !Pattern.matches(passwordReExp, Password) || !Pattern.matches(rePasswordRegExp, RePassword) || !Pattern.matches(ageRegExp, Age)) {
            if (!Pattern.matches(accountRegExp, Account)) {
                accountError.setText("！错误,账号是长度不超过15位的中文和英文和数字");
            }
            if (!Pattern.matches(nameRegExp, Username)) {
                usernameError.setText("！姓名格式错误");
            }
            if (!Pattern.matches(passwordReExp, Password)) {
                passwordError.setText("！错误,密码是长度在6-20位的英文字母和数字");
            }
            if (!Pattern.matches(rePasswordRegExp, RePassword)) {
                rePasswordError.setText("！错误,密码是长度在6-20位的英文字母和数字");
            }
            if (!Pattern.matches(ageRegExp, Age)) {
                ageError.setText("！错误,年龄只能是数字");
            }
            if (!Pattern.matches(phoneRegExp, PhoneNumber)) {
                phoneNumberError.setText("！电话号格式错误");
            }
            return false;
        } else if (checkIfUserExists(Account) || !Password.equals(RePassword)) {
            if (checkIfUserExists(Account)) {
                accountError.setText("！错误,账号已经存在");
            }
            if (!Password.equals(RePassword)) {
                rePasswordError.setText("！两次密码不一致");
            }
            return false;
        }
        return true;
    }

    private boolean checkIfUserExists(String account) {
        String uri = "http://localhost:8080/exist?account=" + account;
        return Boolean.parseBoolean(OkHttpUtil.get(uri, null));
    }

    private boolean registerUser(User user) {
        String uri = "http://localhost:8080/register";

        // 创建请求头,添加请求头信息
//        HttpHeaders httpHeaders = new HttpHeaders();
//        httpHeaders.add(HttpHeaders.CONTENT_TYPE, "application/json");
//        httpHeaders.add(HttpHeaders.CONTENT_ENCODING, "UTF-8");

        // 创建请求体,并添加数据（body参数不需要时，可以省略，需要时添加到bodyParam中即可.）
        Map<String, Object> bodyParam = new HashMap<>(5);
        // 与json中的键值一致
        bodyParam.put("account", user.getAccount());
        bodyParam.put("username", user.getUsername());
        bodyParam.put("password", user.getPassword());
        bodyParam.put("sex", user.getSex());
        bodyParam.put("age", user.getAge());
        bodyParam.put("telephone", user.getTelephone());

        return OkHttpUtil.postJson(uri, bodyParam) != null;
    }
}
