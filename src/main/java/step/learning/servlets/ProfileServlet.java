package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.CarsDAO;
import step.learning.entities.Cars;
import step.learning.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class ProfileServlet extends HttpServlet {
    @Inject
    CarsDAO carsDAO;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        User authUser = (User) req.getAttribute("AuthUser");
        Cars car = carsDAO.getCarById(authUser.getIdCar());
        req.setAttribute("Car",car);
        if(authUser==null)
        {
            req.setAttribute("pageBody", "profile-unauth.jsp");
            //resp.sendRedirect(req.getContextPath()+"/registration");
        }
        else {
            req.setAttribute("pageBody", "profile.jsp");
        }
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }
}
