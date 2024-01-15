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
 * Servlet implementation class InfoServlet.
 * This servlet reads out the available information about the client and server based on the HTTP protocol
 * and shows these values in a web page.
 * The information to be displayed includes: IP of the client, browser type of the client, accepted MIME types of
 * the client, client protocol, server port, and server name.
 * It can be extended with the option to use URL parameters and generate a different web page depending on the parameter value.
 * Available parameters: "server" - display server information, "client" - display client information.
 */
@WebServlet(name = "InfoServlet", urlPatterns = {"/","/info"})
public class InfoServlet extends HttpServlet {

    /**
     * Handles HTTP GET request.
     * @param _request  The request from the client.
     * @param _response The response to be sent to the client.
     * @throws IOException      If an input or output error is detected when the servlet handles the request.
     */
    protected void doGet(HttpServletRequest _request, HttpServletResponse _response) throws IOException {
        // Get information about the client
        String clientIP = _request.getRemoteAddr();
        String browserType = _request.getHeader("User-Agent");
        String acceptedMIMETypes = _request.getHeader("Accept");
        String clientProtocol = _request.getProtocol();

        // Get information about the server
        int serverPort = _request.getServerPort();
        String serverName = _request.getServerName();

        // Get URL parameters
        String parameterValue = _request.getParameter("parameterName");

        _response.setContentType("text/html");
        PrintWriter out = _response.getWriter();

        // Generate the HTML content based on the parameter value
        out.println("<html>");
        out.println("<head><title>InfoServlet Output</title></head>");
        out.println("<body>");

        if ("server".equals(parameterValue)) {
            // Display server information
            out.println("<h2>Server Information:</h2>");
            out.println("<p>Server port: " + serverPort + "</p>");
            out.println("<p>Server name: " + serverName + "</p>");
        } else if ("client".equals(parameterValue)) {
            // Display client information
            out.println("<h2>Client Information:</h2>");
            out.println("<p>IP of the client: " + clientIP + "</p>");
            out.println("<p>Browser type of the client: " + browserType + "</p>");
            out.println("<p>Accepted MIME types of the client: " + acceptedMIMETypes + "</p>");
            out.println("<p>Client protocol: " + clientProtocol + "</p>");
        } else {
            // Display default message
            out.println("<h2>Usage:</h2>");
            out.println("<p>Change the parameter to either 'server' or 'client' to view corresponding information.</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }
}
