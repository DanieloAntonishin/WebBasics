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

}
