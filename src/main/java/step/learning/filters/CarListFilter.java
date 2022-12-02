package step.learning.filters;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.dao.CarsDAO;

import javax.servlet.*;
import java.io.IOException;

@Singleton
public class CarListFilter implements Filter {
    @Inject
    private CarsDAO carsDAO;
    @Inject
    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

    }

    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}
