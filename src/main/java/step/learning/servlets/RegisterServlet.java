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
import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.UUID;

@WebServlet("/registration")  // servlet-api
@MultipartConfig              // прием multipart - данных
@Singleton
public class RegisterServlet extends HttpServlet {
    @Inject
    private UserDAO userDAO;
    @Inject
    private CarsDAO carsDAO;
    @Inject
    private TypeServices typeServices;

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User changes = new User();
        User authUser = (User) req.getAttribute("AuthUser");
        Part userAvatar=null;
        try {
            userAvatar=req.getPart("userAvatar");
        }
        catch (Exception ignored){}
        if (userAvatar != null) {
            String userFilename = userAvatar.getSubmittedFileName();
            // отделяем расширение, проверяем на разрешенные, имя занятым UUID
            int dotPosition = userFilename.lastIndexOf('.');
            if(dotPosition==-1){
                resp.getWriter().print("Wrong file extension");
                return;
            }
            String extension=userFilename.substring(dotPosition);
            // Проверка на тип
            if(! typeServices.isImage(extension)){
                resp.getWriter().print("File type unsupported");
                return;
            }

            String savedName = UUID.randomUUID() + extension;
            String path = req.getServletContext().getRealPath("/"); // ....\target\WebBasics\

            File file= new File(path + "../upload/" + authUser.getAvatar());
            file.delete(); // delete old img by user path

            file = new File(path + "../upload/" + savedName);   // edit users img
            Files.copy(userAvatar.getInputStream(), file.toPath());
            changes.setAvatar(savedName); // edit users path to img
        }

        String reply;
        String login = req.getParameter("login");
        String carId = req.getParameter("carId");
        String name = req.getParameter("name");
        String email = req.getParameter("email");
        String password = req.getParameter("password");
        String accountMoney = req.getParameter("money");

        if(login!=null)
        {
            if(userDAO.isLoginUsed(login))
            {
                resp.getWriter().print("Login '"+login+"' in use");
                return;
            }
            changes.setLogin(login);
        }

        changes.setId(authUser.getId());
        changes.setName(name);
        changes.setIdCar(carId);
        changes.setEmail(email);
        changes.setPass(password);
        changes.setAccountMoney(accountMoney!=null?Double.parseDouble(accountMoney):0.0);

        reply = userDAO.updateUser(changes)
                ?"OK"
                :"Update error";

        resp.getWriter().print(reply);
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();
        req.setAttribute("pageBody","registration.jsp");

        String name = (String) session.getAttribute("name");
        String login = (String) session.getAttribute("login");
        String email= (String) session.getAttribute("email");
        boolean complete=false;
        if(session.getAttribute("complete") != null)    // установка и проверка пер-нной результата
            complete = (Boolean) session.getAttribute("complete");
        // получение пер-нных из сесии
        String nameError = (String) session.getAttribute("nameError");
        String fileError = (String) session.getAttribute("fileError");
        String loginError = (String) session.getAttribute("loginError");
        String emailError = (String) session.getAttribute("emailError");
        String passwordError = (String) session.getAttribute("passwordError");
        String confPassError = (String) session.getAttribute("confPassError");

        req.setAttribute("name", name);
        req.setAttribute("login", login);
        req.setAttribute("email", email);
        req.setAttribute("complete", complete);
        req.setAttribute("nameError", nameError);
        req.setAttribute("fileError", fileError);
        req.setAttribute("loginError", loginError);
        req.setAttribute("emailError", emailError);
        req.setAttribute("passwordError", passwordError);
        req.setAttribute("confPassError", confPassError);

        if(name!=null&&login!=null){                // очистка сесии
            session.removeAttribute("name");
            session.removeAttribute("login");
            session.removeAttribute("email");
            session.removeAttribute("complete");
        }
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);    // вывод
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        // переменые ввода
        String name = req.getParameter("name");
        String login =  req.getParameter("login");
        String email =  req.getParameter("email");
        String password = req.getParameter("password");
        String confirmPassword = req.getParameter("confirmPassword");

        // переменные с проверкой валидации на пустоту
        String nameError = null;
        String loginError =  null;
        String emailError =  null;
        String passwordError =  null;
        String fileError =  null;
        String confPassError =  null;     // проверка на совпадение паролей
        String savedName="";
        try {

            if(login==null||login.isEmpty())
            {
                loginError = "Login could not be empty";
            }
            if (!login.equals(login.trim())) {
                loginError = "Login could not contain trailing spaces";
            }
            if (userDAO.isLoginUsed(login)) {
                loginError = "Login is already in use";
            }
            if(email==null||email.isEmpty())
            {
                emailError="Email could not be empty";
            }
            else if(!email.contains("@"))
            {
                emailError="Email could not be without character '@' ";
            }
            if(name==null||name.isEmpty())
            {
                nameError = "Name could not be empty";
            }
            if (password == null || password.isEmpty()) {
                passwordError = "Password could be null";
            }
            if (!password.equals(confirmPassword)) {
                confPassError = "Passwords mismatch";
            }

            Part userAvatar=req.getPart("userAvatar");  // часть, отвечающия за файл ( имя - как у input)
            if(userAvatar==null)
            {
                fileError = "Form integrity violation";
            }
            long size=userAvatar.getSize();
            if(size>0)      // проверка на то,что есть ли у формы файл ил нет
            {
                // файл приложен - обрабатываем его
                String userFilename = userAvatar.getSubmittedFileName();
                // отделяем расширение, проверяем на разрешенные, имя занятым UUID
                int dotPosition = userFilename.lastIndexOf('.');
                if(dotPosition==-1){
                    fileError = "File without extension";
                }
                String extension=userFilename.substring(dotPosition);
                // Проверка на тип
                if(! typeServices.isImage(extension)){
                    fileError = "File type unsupported";
                }

                if(fileError==null) {   // сохраняем, если нет ошибок
                    savedName = UUID.randomUUID() + extension;
                    //String path =new File("./").getAbsolutePath(); // запрос текущей директории - C:\xampp\tomcat\bin.
                    String path = req.getServletContext().getRealPath("/"); // ....\target\WebBasics\
                    File file = new File(path + "../upload/" + savedName);
                    Files.copy(userAvatar.getInputStream(), file.toPath());
                }
            }

            if(loginError!=null||nameError!=null||passwordError!=null||confPassError!=null  // проход всех условий
            ||fileError!=null||emailError!=null){                                           // и генерация одного
                throw new Exception(                                                        // большого исключения
                        loginError!=null?loginError:"  "+                                   // +точный вывод все ошибок
                        nameError!=null?nameError:"  "+                                     // валидации пользователю
                        passwordError!=null?passwordError:"  "+
                        emailError!=null?emailError:"  "+
                        confPassError!=null?confPassError:"  "+
                        confPassError!=null?confPassError:"  "+
                        fileError!=null?fileError:"  ");

            }
            User user=new User();
            user.setName(name);
            user.setLogin(login);
            user.setPass(password);
            user.setAvatar(savedName);
            user.setEmail(email);
            // Генерируем код подтверждения для почты
            user.setEmailCode(UUID.randomUUID().toString().substring(0,6));

            if(userDAO.add(user)==null)
            {
                throw new Exception("Server error, try later");
            }
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }

        boolean complete=false;

        // условие, которое устанавлививает флаг на переменную
        if(confPassError==null&&loginError==null&&passwordError==null
                &&nameError==null&&fileError==null&&emailError==null
                &&(name!=null&&login!=null&&password!=null&&email!=null)) {
            complete = true;
        }

        // установка атрибутов полей, которые будут показаны пользователю в GET
        req.getSession().setAttribute("name",name);
        req.getSession().setAttribute("login",login);
        req.getSession().setAttribute("email",email);
        req.getSession().setAttribute("complete",complete);

        // установка атрибутов ошибок
        req.getSession().setAttribute("nameError",nameError);
        req.getSession().setAttribute("loginError",loginError);
        req.getSession().setAttribute("emailError",emailError);
        req.getSession().setAttribute("fileError",fileError);
        req.getSession().setAttribute("passwordError",passwordError);
        req.getSession().setAttribute("confPassError",confPassError);

        resp.sendRedirect(req.getRequestURI());
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        User authUser = (User) req.getAttribute("AuthUser");
        String carId = req.getParameter("carId");
        if(authUser!=null)
        {
            String path = req.getServletContext().getRealPath("/"); // ....\target\WebBasics\
            File file= new File(path + "../upload/" + authUser.getAvatar());
            file.delete(); // delete old img by user path
            if(userDAO.delete(authUser))
            {
                if(carId!=""||carId!=null) {
                    carsDAO.SetActive(carId);
                }
                req.setAttribute("AuthUser",null);
            }
            else{
                resp.getWriter().print("Delete user error");
            }
        }

    }
}
