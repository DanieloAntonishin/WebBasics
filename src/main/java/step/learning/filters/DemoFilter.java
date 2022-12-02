package step.learning.filters;

import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

//@WebFilter("/*")
@Singleton
public class DemoFilter implements Filter {
    private FilterConfig filterConfig;
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig=filterConfig;
    }

    @Override
    public void doFilter(                                           // Основной метод фильтр
                        ServletRequest servletRequest,              // запрос (не HTTP)
                         ServletResponse servletResponse,           // ответ (не HTTP)
                         FilterChain filterChain)                   // Ссылка на след "фильтр"
                         throws IOException, ServletException {

//        HttpServletRequest request=(HttpServletRequest) servletRequest;
//        if (request.getServletPath().equals("/")) {
//            request.getRequestDispatcher("home").forward(request,servletResponse);
//            return;
//        }
        //System.out.println("Filter start");
        servletRequest.setAttribute("DemoFilter","Filter works");
        filterChain.doFilter(servletRequest,servletResponse);
        //System.out.println("Filter end");
    }

    @Override
    public void destroy() {
        this.filterConfig=null;
    }
}
