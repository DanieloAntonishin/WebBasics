package step.learning.entities;

import javax.servlet.http.HttpServletRequest;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

public class Cars {
    private String id;
    private String model;
    private String bodyType;
    private String about;
    private String pics;
    private int horsePower;
    private double engineVolume;
    private double price;
    private double consumption;
    private boolean isActive;

    public Cars(){

    }
    public Cars(HttpServletRequest req, String savedName) {
        model  = req.getParameter("model");
        bodyType   = req.getParameter("bodyType");
        about   = req.getParameter("about");
        pics   = savedName;
        price   = Double.parseDouble(req.getParameter("price"));
        horsePower   = Integer.parseInt(req.getParameter("horsePower"));
        engineVolume   = Double.parseDouble(req.getParameter("engineVolume"));
        consumption   = Double.parseDouble(req.getParameter("consumption"));
        isActive = true;
    }

    public Cars(ResultSet res) throws SQLException {
        id     = res.getString("id");
        model  = res.getString("model");
        bodyType   = res.getString("body_type");
        about   = res.getString("about");
        pics = res.getString("pics");
        price = res.getDouble("price");
        consumption = res.getDouble("consumption");
        engineVolume = res.getDouble("engine_volume");
        horsePower = res.getInt("horse_power");
        isActive = res.getBoolean("isActive");
    }

    public void Init(List<Cars> carList)
    {
        for(Cars car : carList)
        {
            model  = car.getModel();
            bodyType   = car.getBodyType();
            about   = car.getAbout();
            //pics = res.getString("pics");
            price = car.getPrice();
            consumption = car.getConsumption();
            engineVolume = car.getEngineVolume();
            horsePower = car.getHorsePower();
            isActive = car.isActive();
        }
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBodyType() {
        return bodyType;
    }

    public void setBodyType(String bodyType) {
        this.bodyType = bodyType;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public int getHorsePower() {
        return horsePower;
    }

    public void setHorsePower(int horsePower) {
        this.horsePower = horsePower;
    }

    public double getEngineVolume() {
        return engineVolume;
    }

    public void setEngineVolume(double engineCapacity) {
        this.engineVolume = engineCapacity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getConsumption() {
        return consumption;
    }

    public void setConsumption(double consumption) {
        this.consumption = consumption;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
