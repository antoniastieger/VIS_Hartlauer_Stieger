//
// Created by Antonia Stieger on 14.01.2024.
//

import at.fhooe.sail.vis.Environment_RmiClient;
import at.fhooe.sail.vis.Environment_SocketClient;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;
import java.io.IOException;
import java.rmi.RemoteException;

/**
 * Servlet implementation class EnvironmentServiceServlet.
 * This servlet fetches environmental data from RMI and Socket clients and displays it on a JSP page.
 */
@WebServlet("/EnvironmentServiceServlet")
public class EnvironmentServiceServlet extends HttpServlet {

    /**
     * Handles GET requests and fetches environmental data from RMI and Socket clients.
     * Displays the data on a JSP page.
     *
     * @param request  The HttpServletRequest object.
     * @param response The HttpServletResponse object.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            // Instantiate the RMI client
            IEnvService rmiClient = new Environment_RmiClient();

            // Instantiate the Socket client
            IEnvService socketClient = new Environment_SocketClient();

            // Fetch data from RMI client
            String[] rmiSensors = rmiClient.requestEnvironmentDataTypes();
            EnvData[] rmiData = rmiClient.requestAll();

            // Fetch data from Socket client
            String[] socketSensors = socketClient.requestEnvironmentDataTypes();
            EnvData[] socketData = socketClient.requestAll();

            // Process the data and set it as a request attribute
            request.setAttribute("rmiSensors", rmiSensors);
            request.setAttribute("rmiData", rmiData);
            request.setAttribute("socketSensors", socketSensors);
            request.setAttribute("socketData", socketData);

            // Forward the request to a JSP page for rendering
            request.getRequestDispatcher("/webapp/environment.jsp").forward(request, response);

        } catch (RemoteException e) {
            e.printStackTrace();
            // Handle RemoteException appropriately
            response.getWriter().println("Error: Remote server communication failed.");
        }
    }
}
