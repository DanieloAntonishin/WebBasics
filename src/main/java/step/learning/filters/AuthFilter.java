package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.UserDAO;
import step.learning.entities.User;
import step.learning.services.DataService;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class AuthFilter implements Filter {
    private FilterConfig filterConfig;
    private final UserDAO userDAO;
    @Inject
    public AuthFilter(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig=filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request=(HttpServletRequest) servletRequest;
        HttpServletResponse response=(HttpServletResponse) servletResponse;
        HttpSession session=request.getSession();

        if(request.getMethod().equals("POST")){
            String userLogin=request.getParameter("userLogin");
            String userPassword=request.getParameter("userPassword");
            if(userLogin!=null&&userPassword!=null)
            {
                User user=userDAO.getUserByCredentials(userLogin,userPassword);
                if(user==null)
                {
                    session.setAttribute("AuthError","Credentials incorrect");
                }
                else{
                    session.setAttribute("AuthUserId",user.getId());
                }
                //System.out.println(userLogin+" "+userPassword);

                response.sendRedirect(request.getRequestURI()+"?"+request.getQueryString());
                return;
            }
        }

        String autData=(String) session.getAttribute("AuthError");
        if(autData!=null){  // в сессии хранится ошибка авторизации
            request.setAttribute("AuthError",autData);
            session.removeAttribute("AuthError");
        }
        autData=(String) session.getAttribute("AuthUserId");


        if(autData!=null) {  // в сессии хранится id авторизованого польз-ля
            request.setAttribute("AuthUser",userDAO.getUserById(autData));
        }
        else{
            request.setAttribute("AuthUser",null);
        }
        filterChain.doFilter(servletRequest,servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig=null;
    }
}