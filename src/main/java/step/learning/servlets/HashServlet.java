package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.hash.HashService;

import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/hash")
@Singleton
public class HashServlet extends HttpServlet {

    @Inject
    @Named("Sha-1")
    private HashService sha1HashService;    // подключения sha-1 через иньектор

    @Inject
    @Named("MD-5")
    private HashService md5HashService;    // подключения md-5 через иньектор

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setCharacterEncoding("UTF-8");
        HttpSession session = req.getSession();

        // получение от сессии данных и передача их в JSP
        String md5HashUserInput = (String) session.getAttribute("md5HashUserInput");
        String sha1HashUserInput = (String) session.getAttribute("sha1HashUserInput");
        req.setAttribute("md5HashUserInput", md5HashUserInput);
        req.setAttribute("sha1HashUserInput", sha1HashUserInput);

        if (sha1HashUserInput != null && md5HashUserInput != null) {    // Обнуление пустых данных
            session.removeAttribute("sha1HashUserInput");
            session.removeAttribute("md5HashUserInput");
        }
        req.getRequestDispatcher("WEB-INF/hash.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setCharacterEncoding("UTF-8");
        String userInput = req.getParameter("userInput");
        // проверка и хеширование ввода с передачей результата в сессию
        if(userInput.equals(""))
        {
            req.getSession().setAttribute("md5HashUserInput", "Empty input MD-5");
            req.getSession().setAttribute("sha1HashUserInput", "Empty input in Sha-1");
        }
        else {
            String md5HashUserInput = md5HashService.hash(userInput);
            String sha1HashUserInput = sha1HashService.hash(userInput);
            req.getSession().setAttribute("md5HashUserInput", md5HashUserInput);
            req.getSession().setAttribute("sha1HashUserInput", sha1HashUserInput);
        }
        resp.sendRedirect(req.getRequestURI());     // редирект на GET
    }
}
