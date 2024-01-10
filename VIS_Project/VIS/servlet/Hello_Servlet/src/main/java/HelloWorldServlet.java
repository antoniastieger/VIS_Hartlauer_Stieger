//
// Created by Antonia Stieger on 10.01.2024.
//

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "No of hits", urlPatterns = {"/hits", "/hitCounter"})
public class HelloWorldServlet extends HttpServlet {
    private int hitCount;

    public void init() {
        hitCount = 0;
    }

    @Override public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        hitCount++;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        out.println("<html><body>");
        out.println("<h2>Total Number of Hits</h2>");
        out.println("<p>Number of calls: " + hitCount + "</p>");
        out.println("</body></html>");
    }
}
