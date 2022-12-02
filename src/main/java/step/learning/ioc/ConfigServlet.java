package step.learning.ioc;

import com.google.inject.Singleton;
import com.google.inject.servlet.ServletModule;
import step.learning.filters.*;
import step.learning.servlets.*;

@Singleton
public class ConfigServlet extends ServletModule {
    @Override
    protected void configureServlets() {
        // Программная замена web.xml - конфигурация фильтров
        filter("/*").through(FirstFilter.class);
        filter("/*").through(DataFilter.class);
        filter("/*").through(AuthFilter.class);
        filter("/*").through(LogoutFilter.class);
        filter("/*").through(DemoFilter.class);

        // ... и сервлетов
        serve("/filters").with(FiltersServlet.class);
        serve("/registration").with(RegisterServlet.class);
        serve("/publishcar").with(PublishCarServlet.class);
        serve("/carscatalog").with(CarsCatalogServlet.class);
        serve("/carinfo").with(CarInfoServlet.class);
        serve("/servlet").with(ViewServlet.class);
        serve("/profile").with(ProfileServlet.class);
        serve("/hash").with(HashServlet.class);
        serve("/checkmail/").with(CheckMailServlet.class);
        serve("/image/*").with(DownloadServlet.class);
        serve("/").with(HomeServlet.class);
    }
}
