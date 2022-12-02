package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.DataService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

//@WebServlet("/filters")
@Singleton
public class FiltersServlet extends HttpServlet {

    private final DataService dataService;
    @Inject
    public FiltersServlet(DataService dataService) {
        this.dataService = dataService;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        //System.out.println("Servlet - before forward");
        //DataService dataService=(DataService) req.getAttribute("DataService");
        String viewInfo="";
        try(Statement statement=dataService.getConnection().createStatement();
            ResultSet res=statement.executeQuery("SELECT COUNT(*) FROM Users"))
        {
            if(res.next()){
                viewInfo="Users count "+res.getInt(1);
            }
            else {
                viewInfo="No data about Users";
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL error: "+e.getMessage());

        }
        req.setAttribute("viewInfo",viewInfo);

        List<String> users=new ArrayList<>();
        try(Statement statement=dataService.getConnection().createStatement();
            ResultSet res=statement.executeQuery("SELECT * FROM Users")) {
            while (res.next()) {
                users.add(res.getString("name") + " " + res.getString("login"));
            }
        }
        catch (SQLException e)
        {
            System.out.println("SQL error: "+e.getMessage());
        }
        req.setAttribute("users",users.toArray(new String[0]));
        req.getRequestDispatcher("WEB-INF/filters.jsp").forward(req,resp);
       // System.out.println("Servlet - after forward");
    }
}
