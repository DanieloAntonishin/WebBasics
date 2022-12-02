package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Singleton
public class LogoutFilter implements Filter {   // фильтр выхода пользователя на всех старицах
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        HttpSession session = request.getSession();

        if (request.getMethod().equals("GET")) {                             // Проверка на метод
            String logOut = (String) request.getParameter("logOut");      // Получение значения кнопки "Log Out" на
                                                                             // нажатие или нет
            if (logOut != null && logOut.equals("Yes")) {                    // если не путая и нажата то чистим в
                session.removeAttribute("AuthUserId");              // сессии атрибут для входа
                response.sendRedirect(request.getContextPath());           // возврат на исходную страницу
                return;
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
