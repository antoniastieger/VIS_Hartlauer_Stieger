//
// Created by Antonia Stieger on 14.01.2024.
//

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Servlet implementation for the SessionServlet.
 * This servlet takes a magic number as a URL parameter and
 * constructs a webpage displaying the browser information along
 * with the provided magic number.
 */
@WebServlet(name = "SessionServlet", urlPatterns = {"/session"})
public class SessionServlet extends HttpServlet {

    /**
     * Handles the GET request to the servlet.
     *
     * @param _request  The HttpServletRequest object.
     * @param _response The HttpServletResponse object.
     * @throws IOException If an I/O error occurs while handling the request.
     */
    protected void doGet(HttpServletRequest _request, HttpServletResponse _response) throws IOException {
        // Get browser information from the User-Agent header
        String userAgent = _request.getHeader("User-Agent");

        // Extract only the browser name from the User-Agent string
        String browser = extractBrowser(userAgent);

        // Get the magic number from the URL parameter
        String magicNumberParam = _request.getParameter("magicNumber");

        // Get or create the session
        HttpSession session = _request.getSession();

        // Retrieve the last magic number from the session
        String lastMagicNumber = (String) session.getAttribute("lastMagicNumber");

        _response.setContentType("text/html");
        PrintWriter out = _response.getWriter();

        // Generate the HTML content based on the information
        out.println("<html>");
        out.println("<head><title>SessionServlet Output</title></head>");
        out.println("<body>");
        out.println("<h2>Session Information:</h2>");
        out.println("<p>You are currently using " + browser + " (magic number: " + magicNumberParam + ").</p>");

        if (lastMagicNumber != null) {
            out.println("<p>Last time you visited, your magic number was " + lastMagicNumber + "!</p>");
        }

        // Store the current magic number in the session
        session.setAttribute("lastMagicNumber", magicNumberParam);

        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Extracts the browser name from the User-Agent string.
     *
     * @param _userAgent The User-Agent string.
     * @return The extracted browser name.
     */
    private String extractBrowser(String _userAgent) {
        Pattern pattern = Pattern.compile("(?i)\\b(Firefox|Chrome|Safari|Edge|IE|Opera)\\b");
        Matcher matcher = pattern.matcher(_userAgent);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return "Unknown Browser";
        }
    }
}
