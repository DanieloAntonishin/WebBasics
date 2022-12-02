package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.DataService;
import step.learning.services.MysqlDataServices;

import javax.servlet.*;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

@Singleton
public class DataFilter implements Filter {
    private FilterConfig filterConfig;

    private final DataService dataService;

    @Inject
    public DataFilter(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig=filterConfig;
    }

    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // DataService dataService = new MysqlDataServices() ;
        if( dataService.getConnection() == null ) {     // проверка подключения к бд, если нет то выводим "статику"
            servletRequest.getRequestDispatcher( "WEB-INF/static.jsp" )
                    .forward( servletRequest, servletResponse ) ;
        }
        else {
            servletRequest.setAttribute("DataService",dataService);     // добавляем в автрибут, для дальнейшего
            filterChain.doFilter(servletRequest, servletResponse);         // использования в JSP
        }
    }

    @Override
    public void destroy() {
        this.filterConfig=null;
    }
}
