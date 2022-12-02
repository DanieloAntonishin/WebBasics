package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.sun.tools.jconsole.JConsoleContext;
import step.learning.dao.CarsDAO;
import step.learning.dao.UserDAO;
import step.learning.entities.Cars;
import step.learning.entities.User;
import step.learning.services.TypeServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@WebServlet("/publishcar")       // servlet-api
@MultipartConfig              // прием multipart - данных
@Singleton
public class PublishCarServlet extends HttpServlet {
    private String errorsField[] = {"modelError", "bodyTypeError", "aboutError"
            , "horsePowerError", "priceError", "consumptionError", "engineVolumeError"};
    private String attributeField[] = {"model","bodyType","about","horsePower","price","consumption","engineVolume"};
    @Inject
    private CarsDAO carsDAO;
    @Inject
    private UserDAO userDAO;
    @Inject
    private TypeServices typeServices;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        req.setAttribute("pageBody", "publishcar.jsp");
        //Map <String,String> attributeGet = new HashMap<>();

        //region Put string fields attribute to map
       /* attributeGet.put("model",(String) session.getAttribute("model"));
        attributeGet.put("color",(String) session.getAttribute("color"));
        attributeGet.put("about",(String) session.getAttribute("about"));
        attributeGet.put("producer",(String) session.getAttribute("producer"));*/
        //endregion

        //region Put errors attribute to map
       /* attributeGet.put("modelError",(String) session.getAttribute("modelError"));
        attributeGet.put("producerError",(String) session.getAttribute("producerError"));
        attributeGet.put("fileError",(String) session.getAttribute("fileError"));
        attributeGet.put("colorError",(String) session.getAttribute("colorError"));
        attributeGet.put("aboutError",(String) session.getAttribute("aboutError"));
        attributeGet.put("yearError",(String) session.getAttribute("yearError"));
        attributeGet.put("priceError",(String) session.getAttribute("priceError"));
        attributeGet.put("mileageError",(String) session.getAttribute("mileageError"));*/
       /*  for(String filedName : attributeGet.keySet())
        {
            req.setAttribute(filedName,attributeGet.get(filedName));
        }*/
       /*for(String filedName : attributeGet.keySet()) // очистка сесии
        {
            if(attributeGet.get(filedName)!=null)
            {
                session.removeAttribute(filedName);
            }
        }*/

        boolean complete = false;
        if (session.getAttribute("complete") != null)    // установка и проверка пер-нной результата
            complete = (Boolean) session.getAttribute("complete");
        //endregion

        System.out.println(complete);

        for (int i = 0; i < attributeField.length; i++) // string
        {
            req.setAttribute(attributeField[i], session.getAttribute(attributeField[i]));
        }

        for (int i = 0; i < errorsField.length; i++) // errors
        {
            req.setAttribute(errorsField[i], session.getAttribute(errorsField[i]));
        }

        for (int i = 0; i < attributeField.length; i++) // string
        {
            if(attributeField[i]!=null)
            {
                session.removeAttribute(attributeField[i]);
            }
        }

        req.setAttribute("complete", complete);

        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // переменые ввода
        /*Integer year = Integer.parseInt((String) req.getParameter("year"));
        Double price = Double.parseDouble((String) req.getParameter("price"));
        Double mileage =  Double.parseDouble((String) req.getParameter("mileage"));*/

        // переменные с проверкой валидации на пустоту

        String fileError = null;
        String savedName = "";

        Map<String, String> validationError = new HashMap<>();
        try {
            for (int i = 0; i < attributeField.length; i++) {

                    if (req.getParameter(attributeField[i]) == null || req.getParameter(attributeField[i]).isEmpty()) {
                        validationError.put(errorsField[i], attributeField[i] + " could not be empty");
                    } else {
                        validationError.put(errorsField[i], null);
                    }

            }

            Part carPics = req.getPart("carPics");  // часть, отвечающия за файл ( имя - как у input)
            if (carPics == null) {
                fileError = "Form integrity violation";
            }
            long size = carPics.getSize();
            if (size > 0)      // проверка на то,что есть ли у формы файл ил нет
            {
                // файл приложен - обрабатываем его
                String carFilename = carPics.getSubmittedFileName();
                // отделяем расширение, проверяем на разрешенные, имя занятым UUID
                int dotPosition = carFilename.lastIndexOf('.');
                if (dotPosition == -1) {
                    fileError = "File without extension";
                }
                String extension = carFilename.substring(dotPosition);
                // Проверка на тип
                if (!typeServices.isImage(extension)) {
                    fileError = "File type unsupported";
                }

                if (fileError == null) {   // сохраняем, если нет ошибок
                    savedName = UUID.randomUUID() + extension;
                    //String path =new File("./").getAbsolutePath(); // запрос текущей директории - C:\xampp\tomcat\bin.
                    String path = req.getServletContext().getRealPath("/"); // ....\target\WebBasics\
                    File file = new File(path + "../upload/" + savedName);
                    Files.copy(carPics.getInputStream(), file.toPath());
                }
                else{
                    throw new Exception("File save error");
                }
            }

            for (String err : validationError.keySet()) {
                if(validationError.get(err)!=null) {
                    throw new Exception(validationError.get(err));
                }
            }

            Cars car = new Cars(req,savedName);


            if (carsDAO.add(car) == null) {
                throw new Exception("Server error, try later");
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }

        boolean complete = true;
        for(String x: validationError.keySet())
        {
            System.out.println(x+" : "+validationError.get(x));
        }
        // условие, которое устанавлививает флаг на переменную
        if(fileError == null) {
            for (String err : validationError.keySet()) {
                if (validationError.get(err) != null) {
                    complete = false;
                    break;
                }
            }
            for(int i=0;i<attributeField.length;i++)
            {
                if(req.getParameter(attributeField[i])==null)
                {
                    complete = false;
                    break;
                }
            }
        }

        // установка атрибутов полей, которые будут показаны пользователю в GET

        for(int i=0;i<attributeField.length;i++)
        {
            req.getSession().setAttribute(attributeField[i],req.getParameter(attributeField[i]));
        }
        // установка атрибутов ошибок
        for(String err: validationError.keySet())
        {
            req.getSession().setAttribute(err,validationError.get(err));
        }
        req.getSession().setAttribute("fileError", fileError);
        System.out.println(complete);
        req.getSession().setAttribute("complete", complete);

        resp.sendRedirect(req.getRequestURI());
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = (User) req.getAttribute("AuthUser");
        Cars changes = new Cars();
        String carId = req.getParameter("id");
        Cars oldCar = carsDAO.getCarById(carId);
        if(authUser.getLogin().equals("person")){
            Part carPics = null;
            try {
                carPics = req.getPart("carPics");
            }
            catch (Exception ignored){}
            if (carPics != null) {
                String carFilename = carPics.getSubmittedFileName();
                // отделяем расширение, проверяем на разрешенные, имя занятым UUID
                int dotPosition = carFilename.lastIndexOf('.');
                if(dotPosition==-1){
                    resp.getWriter().print("Wrong file extension");
                    return;
                }
                String extension=carFilename.substring(dotPosition);
                // Проверка на тип
                if(! typeServices.isImage(extension)){
                    resp.getWriter().print("File type unsupported");
                    return;
                }

                String savedName = UUID.randomUUID() + extension;
                String path = req.getServletContext().getRealPath("/"); // ....\target\WebBasics\

                File file= new File(path + "../upload/" + oldCar.getPics());
                file.delete(); // delete old img by user path

                file = new File(path + "../upload/" + savedName);   // edit users img
                Files.copy(carPics.getInputStream(), file.toPath());
                changes.setPics(savedName); // edit users path to img
            }

            String model = req.getParameter("model");
            String bodyType = req.getParameter("bodyType");
            String price = req.getParameter("price");
            String horsePower = req.getParameter("horsePower");
            String about = req.getParameter("about");
            String consumption = req.getParameter("consumption");
            String engineVolume = req.getParameter("engineVolume");

            changes.setId(carId);
            changes.setModel(model);
            changes.setBodyType(bodyType);
            changes.setPrice(price!=null?Double.parseDouble(price):0.0);
            changes.setConsumption(consumption!=null?Double.parseDouble(consumption):0.0);
            changes.setEngineVolume(engineVolume!=null?Double.parseDouble(engineVolume):0.0);
            changes.setHorsePower(horsePower!=null?Integer.parseInt(horsePower):0);
            changes.setAbout(about);

            String reply = carsDAO.updateCar(changes)
                    ?"OK"
                    :"Update error";

            resp.getWriter().print(reply);
        }
        else{
            if(authUser!=null&&req.getParameter("isRented").equals("true"))
            {
                resp.getWriter().print("Ok. you rent it");
                carsDAO.SetDisable(carId);
                authUser.setIdCar(carId);
            }
            if(authUser!=null&&req.getParameter("isRented").equals("false"))
            {
                resp.getWriter().print("Ok. you cancel rent it");
                carsDAO.SetActive(carId);
                authUser.setIdCar(null);
            }
            userDAO.updateRentedCar(authUser);
        }

//        String reply = carsDAO.updateCar(changes)
//                ?"OK"
//                :"Update error";
//
//        resp.getWriter().print(reply);
    }
}
