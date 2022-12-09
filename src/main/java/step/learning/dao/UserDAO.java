package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entities.User;
import step.learning.services.EmailService;
import step.learning.services.hash.HashService;
import step.learning.services.DataService;

import javax.inject.Named;
import java.sql.*;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

@Singleton
public class UserDAO {
    private final Connection connection;
    private final HashService hashService;
    private final DataService dataService;
    private final EmailService emailService;

    @Inject
    public UserDAO(DataService dataService,  @Named("Sha-1") HashService hashService,EmailService emailService) {
        this.dataService = dataService;
        this.hashService = hashService;
        this.emailService = emailService;

        this.connection = dataService.getConnection();
    }

    /**
     * Change value of email_code_attempts if user enter wrong email_code
     * @param user
     * @return false if error in DB
     */
    public boolean incEmailCodeAttempts (User user) {
        if(user==null||user.getId()==null) return false;
        String sql = "UPDATE users u SET u.`email_code_attempts` = u.`email_code_attempts`+1 WHERE u.`id`= ? ";
        try(PreparedStatement statement = dataService.getConnection().prepareStatement(sql)){
            statement.setString(1,user.getId());
            statement.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("UserDAO::incEmailCodeAttempts "+e.getMessage());
            System.out.println(sql);
            return false;
        }
        user.setEmailCodeAttempts(user.getEmailCodeAttempts()+1);
        return true;
    }

    /**
     * Updates data for given user: set email to be confirmed
     * @param user with Id set
     * @return success status
     */
    public boolean confirmEmail(User user) {
        if(user.getId() == null) return false;

        String sql = "UPDATE Users SET email_code = NULL WHERE id = ?";
        try (PreparedStatement prep = dataService.getConnection().prepareStatement( sql )) {
            prep.setString(1, user.getId());
            prep.executeUpdate();
        }
        catch (SQLException e)
        {
            System.out.println("UserDAO::confirmEmail "+e.getMessage());
            System.out.println(sql + " " + user.getId());
            return false;
        }
        return true;
    }

    /**
     * Updates data for user given. Only non-null fields are considered
     * @param user entity 'User' with 'id'
     * @return result 'true/false'
     */
    public boolean updateUser(User user) {
        if(user==null||user.getId()==null) return false;
        Map<String,String> sqlReq=new HashMap<>();
        Map<String,Double> sqlReqNumeric = new HashMap<>(); // для числовых
        if(user.getLogin()!=null) sqlReq.put("login",user.getLogin());
        if(user.getName()!=null) sqlReq.put("name",user.getName());
        if(user.getAccountMoney() > 0.0) sqlReqNumeric.put("money", user.getAccountMoney());
        if(user.getIdCar()!=null) {
            if(user.getIdCar().equals("null")) {
                sqlReq.put("id_car", null);
            }
            else {
                sqlReq.put("id_car", user.getIdCar());
            }
        }
        if(user.getAvatar()!=null) sqlReq.put("avatar",user.getAvatar());
        if(user.getEmail()!=null) {
            // Обновление + новый код
            user.setEmailCode(UUID.randomUUID().toString().substring(0,6));
            sqlReq.put("email",user.getEmail());
            sqlReq.put("email_code",user.getEmailCode());
            // + сборос счетчика попыток
            sqlReqNumeric.put("email_code_attempts",(double)0);
        }
        if(user.getPass()!=null) {   // изминение пароля
            // генерируем соль
            String salt = hashService.hash(UUID.randomUUID().toString());
            // Генерируем хеш пароля
            String passHash = this.hashPassword(user.getPass(), salt);
            sqlReq.put("pass",passHash);
            sqlReq.put("salt",salt);
        }

        String sql = "UPDATE Users u SET";
        boolean needComma=false;
        for(String filedName : sqlReq.keySet())
        {
            sql+=String.format("%c u.`%s` =? ",(needComma? ',':' '),filedName);
            needComma=true;
        }
        for(String filedName : sqlReqNumeric.keySet())  // числовые поля не несут опасности,
        {                                               // поэтому подставляем сразу в запрос
            sql+=String.format(Locale.US,"%c u.`%s` = %.2f ",(needComma? ',':' '),filedName,sqlReqNumeric.get(filedName));
            needComma=true;
        }

        sql+=" WHERE u.`id`=? ";

        if(!needComma) { return false; } // не было ни одного параметра
        try (PreparedStatement prep=dataService.getConnection().prepareStatement(sql)){
            int n=1;
            for(String filedName : sqlReq.keySet())
            {
                prep.setString(n,sqlReq.get(filedName));
                ++n;
            }
            prep.setString(n, user.getId());
            prep.executeUpdate();

        }
        catch (SQLException e)
        {
            System.out.println("UserDAO::updateUser "+e.getMessage());
            return false;
        }
        // Запрос у БД выполнен успешно, если нужно, отправляем код на почту
        if(user.getEmailCode()!=null)
        {
            String text = String.format("<h2>Hi!</h2> <p>Confirm this code  <b>%s</b></p><p> Follow <a href='http://localhost:8080/WebBasics_war_exploded/checkmail/?userid=%s&confirm=%s'>link</a> to confirm</p>",
                    user.getEmailCode(),user.getId(),user.getEmailCode());
            emailService.send(user.getEmail(), "Email confirmation", text);
        }
        return true;
    }

    /**
     * Update field of foreign key id_car and set id of rented car for user
     * @param userId
     * @return false if error in DB
     */
    public boolean updateRentedCar(User userId) {
        String sql="UPDATE Users u SET u.`id_car` = ? WHERE u.`id`= ? ";
        try(PreparedStatement prep=dataService.getConnection().prepareStatement(sql))
        {
            prep.setString(1,userId.getIdCar());
            prep.setString(2,userId.getId());
            prep.executeUpdate();
            return true;
        }
        catch (SQLException e){
            System.out.println("UserDAO::updateRentedCar "+e.getMessage()+"\n"+sql);
        }
        return false;
    }

    /**
     * Get user obj by user id
     * @param userId
     * @return user object
     */
    public User getUserById(String userId) {
        String sql="SELECT * FROM Users u WHERE u.`id`= ? ";
        try(PreparedStatement prep=dataService.getConnection().prepareStatement(sql))
        {
            prep.setString(1,userId);
            ResultSet res=prep.executeQuery();
            if(res.next()) {
                return new User(res);
            }
        }
        catch (SQLException e){
            System.out.println("UserDAO::getUserById "+e.getMessage()+"\n"+sql);
        }
        return null;
    }

    /**
     * Inserts user in DB Users table
     *
     * @param user data to insert
     * @return `id` of new record or null if fails
     */
    public String add(User user) {
        // генерируем id для новой записи
        String id = UUID.randomUUID().toString();
        // генерируем соль
        String salt = hashService.hash(UUID.randomUUID().toString());
        // Генерируем хеш пароля
        String passHash = this.hashPassword(user.getPass(), salt);
        // готовим запрос (подстановка введеных данных)
        String sql = "INSERT INTO Users(`id`,`login`,`pass`,`name`,`salt`,`avatar`,`email`,`email_code`) " +
                "VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, id);
            prep.setString(2, user.getLogin());
            prep.setString(3, passHash);
            prep.setString(4, user.getName());
            prep.setString(5, salt);
            prep.setString(6, user.getAvatar());
            prep.setString(7, user.getEmail());
            prep.setString(8, user.getEmailCode());
            prep.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(sql);
            return null;
        }

        // После добавления отправляем на почту код
        String text = String.format("Hi, %s! Confirm this code is <b>%s</b>", user.getName(), user.getEmailCode());
        emailService.send(user.getEmail(), "Email confirmation", text);

        return id;
    }

    /**
     * Delete user from DB by user object
     * @param user
     * @return false if error in DB
     */
    public boolean delete(User user) {
        String sql = "DELETE FROM Users WHERE `id` = ?";
        try(PreparedStatement prep = dataService.getConnection().prepareStatement(sql)) {
            prep.setString(1,user.getId());
            prep.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("UserDAO::delete "+e.getMessage());
            System.out.println(sql);
            return false;
        }
        return true;
    }

    /**
     * Checks User table for login given
     *
     * @param login valur to look
     * @return true if log is in table
     */
    public boolean isLoginUsed(String login) {
        if (!(login == null || login.equals("")))      // валидация на пустоту и null
        {
            String sql = "SELECT COUNT(u.`id`) FROM Users u WHERE u.`login`=?";
            try (PreparedStatement prep = connection.prepareStatement(sql)) {
                prep.setString(1, login);
                ResultSet res = prep.executeQuery();
                res.next();
                return res.getInt(1) > 0;
            } catch (SQLException ex) {
                System.out.println(ex.getMessage());
                System.out.println(sql);
                return true;
            }
        }
        return false;
    }

    /**
     * Calculates hash (optionally salt) from password
     *
     * @param password Open password string
     * @return hash for DB table
     */
    public String hashPassword(String password, String salt) {
        return hashService.hash(salt + password + salt);
    }

    /**
     * Get user from DB table by login and password
     *
     * @param login Credentials
     * @param pass  Credentials
     * @return null or user
     */
    public User getUserByCredentials(String login, String pass) {
        String sql = "SELECT u.* FROM Users u WHERE u.`login`=? ";
        try (PreparedStatement prep = connection.prepareStatement(sql)) {
            prep.setString(1, login);
            ResultSet res = prep.executeQuery();
            if (res.next()) {
                User user = new User(res);
                // pass - открытий пароль, user.pass - Hash(pass,user.salt)
                // если у пользователя нет соли, то не добавляем её при хешировании входящего пароля пользователя
                if(this.hashPassword(pass, user.getSalt()==null ? "" : user.getSalt()).equals(user.getPass()))
                {
                    return user;
                }
            }
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
        return null;
    }

}
