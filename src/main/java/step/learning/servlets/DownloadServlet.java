package step.learning.servlets;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import step.learning.services.TypeServices;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;

@Singleton
public class DownloadServlet extends HttpServlet {
    @Inject
    TypeServices typeServices;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String requestedFilename = req.getPathInfo();

        //resp.getWriter().println("DownloadServlet for " + requestedFilename);
        // проверяем существует ли файл
        int dotPosition = requestedFilename.lastIndexOf('.');
        if (dotPosition == -1) {
            resp.setStatus(400);
            resp.getWriter().print(requestedFilename + " File extension required");
            return;
        }

        String extension = requestedFilename.substring( dotPosition ) ;
        String mimeType = typeServices.getMimeByStringExtension( extension ) ;
        if( mimeType == null ) {
            resp.setStatus( 400 ) ;
            resp.getWriter().print( requestedFilename + ": File extension unsupported" ) ;
            return ;
        }

        String path = req.getServletContext().getRealPath("/"); // ....\target\WebBasics\
        File file = new File(path + "../upload" + requestedFilename);

        if (file.isFile()) {
            resp.setContentType(mimeType);
            resp.setContentLengthLong(file.length());
            // Копируем в поток передаваймый файл
            try (InputStream reader = Files.newInputStream(file.toPath())) {
                byte[] buf = new byte[1024];
                int n;
                OutputStream writer = resp.getOutputStream();
                while ((n = reader.read(buf)) > 0) {
                    writer.write(buf, 0, n);
                }
            } catch (IOException e) {
                System.out.println("DownloadServlet::doGet " + requestedFilename + "\n" + e.getMessage());
                resp.setStatus(500);
                resp.getWriter().print("Server error");
                return;
            }
        } else {

        }
    }

}
