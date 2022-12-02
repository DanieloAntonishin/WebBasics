package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.CarsDAO;
import step.learning.dao.UserDAO;
import step.learning.entities.Cars;
import step.learning.entities.User;
import step.learning.services.TypeServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.UUID;

@WebServlet("/carinfo")
@Singleton
public class CarInfoServlet extends HttpServlet {
    @Inject
    private CarsDAO carsDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private TypeServices typeServices;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String Id = req.getParameter("id");
        Cars car = carsDAO.getCarById(Id);
        req.setAttribute("Car", car);
        req.setAttribute("pageBody", "carinfo.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = (User) req.getAttribute("AuthUser");
        String carId = req.getParameter("id");
        if (authUser != null) {
            // Резервируем машину для авт-ного пользователя и добавляем её к нему
            carsDAO.SetDisable(carId);
            authUser.setIdCar(carId);
            userDAO.updateUser(authUser);
        }
        req.getSession().setAttribute("rentMessage", "Car rented successful");
        resp.sendRedirect(req.getContextPath() + "/carscatalog");
    }

//    @Override
//    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        User authUser = (User) req.getAttribute("AuthUser");
//        if(authUser.getLogin()=="admin"){
//            Cars changes = new Cars();
//
//            Part carPics = null;
//            try {
//                carPics = req.getPart("carPics");
//            }
//            catch (Exception ignored){}
//            if (carPics != null) {
//                String carFilename = carPics.getSubmittedFileName();
//                // отделяем расширение, проверяем на разрешенные, имя занятым UUID
//                int dotPosition = carFilename.lastIndexOf('.');
//                if(dotPosition==-1){
//                    resp.getWriter().print("Wrong file extension");
//                    return;
//                }
//                String extension=carFilename.substring(dotPosition);
//                // Проверка на тип
//                if(! typeServices.isImage(extension)){
//                    resp.getWriter().print("File type unsupported");
//                    return;
//                }
//
//                String savedName = UUID.randomUUID() + extension;
//                String path = req.getServletContext().getRealPath("/"); // ....\target\WebBasics\
//
//                File file= new File(path + "../upload/" + authUser.getAvatar());
//                file.delete(); // delete old img by user path
//
//                file = new File(path + "../upload/" + savedName);   // edit users img
//                Files.copy(carPics.getInputStream(), file.toPath());
//                changes.setPics(savedName); // edit users path to img
//            }
//
//            String reply;
//            String model = req.getParameter("model");
//            String bodyType = req.getParameter("bodyType");
//            String price = req.getParameter("price");
//            String horsePower = req.getParameter("horsePower");
//            String about = req.getParameter("about");
//            String consumption = req.getParameter("consumption");
//            String engineVolume = req.getParameter("engineVolume");
//
//            changes.setId(authUser.getId());
//            changes.setModel(model);
//            changes.setBodyType(bodyType);
//            changes.setPrice(Double.parseDouble(price));
//            changes.setConsumption(Double.parseDouble(consumption));
//            changes.setEngineVolume(Double.parseDouble(engineVolume));
//            changes.setHorsePower(Integer.parseInt(horsePower));
//            changes.setAbout(about);
//
//            reply = carsDAO.updateCar(changes)
//                    ?"OK"
//                    :"Update error";
//
//            resp.getWriter().print(reply);
//        }
//        else{
//            if(carsDAO.SetActive( req.getParameter("id")))
//            {
//                resp.getWriter().print("Ok. you rent it");
//            }
//        }}

}
