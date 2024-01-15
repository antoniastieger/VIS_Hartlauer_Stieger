//
// Created by Antonia Stieger on 10.01.2024.
//

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * This servlet class, HelloWorldServlet, is a simple example demonstrating a hit counter.
 * It extends HttpServlet to handle HTTP requests.
 * It is annotated with @WebServlet to specify its name and URL patterns for mapping.
 * The servlet maintains a hit count, which increments with each incoming request.
 */
@WebServlet(name = "HelloWorldServlet", urlPatterns = {"/", "/hits", "/hitCounter"})
public class HelloWorldServlet extends HttpServlet {
    /**
     * The hitCount variable stores the total number of hits to this servlet.
     */
    private int hitCount;

    /**
     * Initializes the servlet by setting the hitCount to 0.
     */
    public void init() {
        hitCount = 0;
    }

    /**
     * Handles HTTP GET requests. Increments the hit count and sends a response
     * displaying the total number of hits.
     *
     * @param _request  the HttpServletRequest object containing the request parameters
     * @param _response the HttpServletResponse object for sending the responses
     * @throws IOException      if an I/O error occurs during the processing of the request
     */
    @Override
    public void doGet(HttpServletRequest _request, HttpServletResponse _response)
            throws IOException {
        hitCount++;
        _response.setContentType("text/html");
        PrintWriter out = _response.getWriter();

        out.println("<html><body>");
        out.println("<h2>Total Number of Hits</h2>");
        out.println("<p>Number of calls: " + hitCount + "</p>");
        out.println("</body></html>");
    }
}