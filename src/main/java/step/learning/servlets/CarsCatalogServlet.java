package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.CarsDAO;
import step.learning.entities.Cars;
import step.learning.services.TypeServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Singleton
@WebServlet("/carscatalog")
public class CarsCatalogServlet extends HttpServlet {
    @Inject
    private CarsDAO carsDAO;
    @Inject
    private TypeServices typeServices;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        List<Cars> carsList = carsDAO.getListOfCars();

        String message = (String) req.getSession().getAttribute("rentMessage");

        req.setAttribute("CarsList",carsList); // список с машинами
        req.setAttribute("pageBody", "car-catalog.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }
}
