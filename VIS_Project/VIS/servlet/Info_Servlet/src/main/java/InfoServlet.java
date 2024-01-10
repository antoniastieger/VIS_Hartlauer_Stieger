//
// Created by Antonia Stieger on 10.01.2024.
//

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "InfoServlet", urlPatterns = {"/info"})
public class InfoServlet extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // Get information about the client
        String clientIP = request.getRemoteAddr();
        String browserType = request.getHeader("User-Agent");
        String acceptedMIMETypes = request.getHeader("Accept");
        String clientProtocol = request.getProtocol();

        // Get information about the server
        int serverPort = request.getServerPort();
        String serverName = request.getServerName();

        // Get URL parameters
        String parameterValue = request.getParameter("parameterName");

        // Set the response content type
        response.setContentType("text/html");

        // Get the response writer
        PrintWriter out = response.getWriter();

        // Generate the HTML content based on the information
        out.println("<html>");
        out.println("<head><title>InfoServlet Output</title></head>");
        out.println("<body>");
        out.println("<h2>Client Information:</h2>");
        out.println("<p>IP of the client: " + clientIP + "</p>");
        out.println("<p>Browser type of the client: " + browserType + "</p>");
        out.println("<p>Accepted MIME types of the client: " + acceptedMIMETypes + "</p>");
        out.println("<p>Client protocol: " + clientProtocol + "</p>");
        out.println("<h2>Server Information:</h2>");
        out.println("<p>Server port: " + serverPort + "</p>");
        out.println("<p>Server name: " + serverName + "</p>");
        out.println("<h2>URL Parameter:</h2>");
        out.println("<p>Parameter value: " + parameterValue + "</p>");
        out.println("</body>");
        out.println("</html>");
    }
}
