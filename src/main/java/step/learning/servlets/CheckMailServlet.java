package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.UserDAO;
import step.learning.entities.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Singleton
public class CheckMailServlet extends HttpServlet {
    @Inject
    private UserDAO userDAO;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String confirm = req.getParameter("confirm");
        String userId = req.getParameter("userid");
        if(confirm!=null) {           // есть сообщение для подверждения
                try {
                    User user = userId == null
                            ? (User) req.getAttribute( "AuthUser" )     // сообщение из формы
                            : userDAO.getUserById( userId ) ;              // сообщение из почты
                    if ( user == null ) // ( ошибка авторизации по id либо по AuthUser)
                        throw new Exception("Invalid user credentials");
                    req.setAttribute("AuthUser",user);
                    if ( user.getEmailCode() == null ) // почта подтверждена, вероятно обновление страницы с параметрами
                        throw new Exception("Email already confirmed");

                    if ( !confirm.equals( user.getEmailCode() ) ) {// код не подтвержден
                        // увеличиваем счетчик попыток
                        userDAO.incEmailCodeAttempts(user);
                        throw new Exception("Invalid code. "+user.getEmailCodeAttempts()+ " attempts ");
                    }
                    if ( !userDAO.confirmEmail(user) )  // ошибка БД
                        throw new Exception("DB error");

                    req.setAttribute("confirm", "OK");  // код подтвержден
                }
                catch (Exception ex) {
                    req.setAttribute("confirmError", ex.getMessage());
                }
            }
        req.setAttribute("pageBody", "checkmail.jsp");
        req.getRequestDispatcher("/WEB-INF/_layout.jsp").forward(req, resp);
    }
}


