package step.learning.dao;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.entities.Cars;
import step.learning.services.DataService;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.sql.Statement;
import java.time.Year;
import java.util.*;

@Singleton
public class CarsDAO {
    private final DataService dataService;
    @Inject
    public CarsDAO(DataService dataService)
    {
        this.dataService = dataService;
    }
    public boolean updateCar(Cars car) {
        if (car == null || car.getId() == null) return false;
        Map<String, String> sqlReq = new HashMap<>();
        Map<String, Double> sqlReqNumeric = new HashMap<>(); // для числовых
        if (car.getModel() != null) sqlReq.put("model", car.getModel());
        if (car.getBodyType() != null) sqlReq.put("bodyType", car.getBodyType());
        if (car.getAbout() != null) sqlReq.put("about", car.getAbout());
        if (car.getPics() != null) sqlReq.put("pics", car.getPics());
        if (car.getHorsePower() > 0) sqlReqNumeric.put("horse_power", (double) car.getHorsePower());
        if (car.getPrice() > 0.0) sqlReqNumeric.put("price", car.getPrice());
        if (car.getConsumption() > 0.0) sqlReqNumeric.put("consumption", car.getConsumption());
        if (car.getEngineVolume() > 0.0) sqlReqNumeric.put("engine_volume", car.getEngineVolume());

        String sql = "UPDATE Cars u SET";
        boolean needComma = false;
        for (String filedName : sqlReq.keySet()) {
            sql += String.format("%c u.`%s` =? ", (needComma ? ',' : ' '), filedName);
            needComma = true;
        }
        for (String filedName : sqlReqNumeric.keySet())  // числовые поля не несут опасности,
        {                                                // поэтому подставляем сразу в запрос
            sql += String.format(Locale.US,"%c u.`%s` = %.2f ", (needComma ? ',' : ' '), filedName, sqlReqNumeric.get(filedName));
            needComma = true;
        }

        sql += " WHERE u.`id`=? ";

        if (!needComma) { return false; } // не было ни одного параметра
        try (PreparedStatement prep = dataService.getConnection().prepareStatement(sql)) {
            int n = 1;
            for (String filedName : sqlReq.keySet()) {
                prep.setString(n, sqlReq.get(filedName));
                ++n;
            }
            prep.setString(n, car.getId());
            prep.executeUpdate();

        } catch (SQLException e) {
            System.out.println("carDAO::updateCar " + e.getMessage());
            return false;
        }

        return true;
    }

    public Cars getCarById(String carId) {
        String sql = "SELECT * FROM Cars u WHERE u.`id`= ? ";
        try (PreparedStatement prep = dataService.getConnection().prepareStatement(sql)) {
            prep.setString(1, carId);
            ResultSet res = prep.executeQuery();
            if (res.next()) {
                return new Cars(res);
            }
        } catch (SQLException e) {
            System.out.println("carDAO::getCarById " + e.getMessage() + "\n" + sql);
        }
        return null;
    }
    public List<Cars> getListOfCars() {
        List<Cars> carsList = new ArrayList<>();
        String sql = "SELECT * FROM Cars ";
        try (Statement statement = dataService.getConnection().createStatement()) {
            ResultSet resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
               carsList.add(new Cars(resultSet));
            }

            resultSet.close();
            return carsList;

        } catch (SQLException e) {
            System.out.println("Query Ok error: " + e.getMessage());
            System.out.println(sql);
        }
        return null;
    }
    /**
     * Inserts car in DB cars table
     *
     * @param car data to insert
     * @return `id` of new record or null if fails
     */
    public String add(Cars car) {
        // генерируем id для новой записи
        String id = UUID.randomUUID().toString();
        // готовим запрос (подстановка введеных данных)
        String sql = "INSERT INTO cars(`id`,`model`,`body_type`,`about`,`pics`,`horse_power`,`engine_volume`," +
                "`price`,`consumption`) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement prep = dataService.getConnection().prepareStatement(sql)) {
            prep.setString(1, id);
            prep.setString(2, car.getModel());
            prep.setString(3, car.getBodyType());
            prep.setString(4, car.getAbout());
            prep.setString(5, car.getPics());
            prep.setInt(6, car.getHorsePower());
            prep.setDouble(7, car.getEngineVolume());
            prep.setDouble(8, car.getPrice());
            prep.setDouble(9, car.getConsumption());
            prep.executeUpdate();

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            System.out.println(sql);
            return null;
        }

        return id;
    }

    public boolean delete(String id)
    {
        String sql = "DELETE FROM Cars WHERE `id` = ?";
        try(PreparedStatement prep = dataService.getConnection().prepareStatement(sql)) {
            prep.setString(1,id);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("CarsDAO::delete "+e.getMessage());
            System.out.println(sql);
            return false;
        }
        return true;
    }

    public boolean SetActive (String id) {
        String sql = "UPDATE Cars c SET c.`isActive` = '1' WHERE c.`id` = ?";
        try(PreparedStatement prep = dataService.getConnection().prepareStatement(sql)) {
            prep.setString(1,id);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("CarsDAO::SetActive "+e.getMessage());
            System.out.println(sql);
            return false;
        }
        return true;
    }
    public boolean SetDisable(String id) {
        String sql = "UPDATE Cars c SET c.`isActive` = '0' WHERE c.`id` = ?";
        try(PreparedStatement prep = dataService.getConnection().prepareStatement(sql)) {
            prep.setString(1,id);
            prep.executeUpdate();
        }
        catch (SQLException e) {
            System.out.println("CarsDAO::SetDisable "+e.getMessage());
            System.out.println(sql);
            return false;
        }
        return true;
    }

}
