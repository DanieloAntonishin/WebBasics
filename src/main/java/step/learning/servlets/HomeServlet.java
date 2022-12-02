package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.EmailService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@WebServlet("")
@Singleton
public class HomeServlet extends HttpServlet {
    @Inject
    private EmailService emailService;
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        emailService.send("javaseniorweb@gmail.com",
//                "Служба безпеки України","Be careful)");
        req.getRequestDispatcher("WEB-INF/index.jsp").forward(req, resp);
    }
}
