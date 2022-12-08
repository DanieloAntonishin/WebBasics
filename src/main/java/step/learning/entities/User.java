package step.learning.entities;

import java.sql.ResultSet;
import java.sql.SQLException;

public class User {
    private String id;
    private String login;
    private String pass;
    private String name;
    private String salt;
    private String email;
    private String emailCode;
    private String idCar;
    private int emailCodeAttempts;
    private double accountMoney;
    private String avatar;
    public User() {
    }

    public User(ResultSet res) throws SQLException {
        id     = res.getString("id");
        login  = res.getString("login");
        pass   = res.getString("pass");
        name   = res.getString("name");
        salt   = res.getString("salt");
        idCar  = res.getString("id_car");
        avatar = res.getString("avatar");
        email = res.getString("email");
        accountMoney = res.getDouble("money");
        emailCode = res.getString("email_code");
        emailCodeAttempts = res.getInt("email_code_attempts");
    }

    public double getAccountMoney() {
        return accountMoney;
    }

    public void setAccountMoney(double accountMoney) {
        this.accountMoney = accountMoney;
    }

    public int getEmailCodeAttempts() {
        return emailCodeAttempts;
    }

    public void setEmailCodeAttempts(int emailCodeAttempts) {
        this.emailCodeAttempts = emailCodeAttempts;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalt() {
        return salt;
    }

    public void setSalt(String salt) {
        this.salt = salt;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmailCode() {
        return emailCode;
    }

    public void setEmailCode(String emailCode) {
        this.emailCode = emailCode;
    }

    public String getIdCar() {
        return idCar;
    }

    public void setIdCar(String idCar) {
        this.idCar = idCar;
    }
}
