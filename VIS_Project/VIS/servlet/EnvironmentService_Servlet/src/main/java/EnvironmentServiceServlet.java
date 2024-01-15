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
@WebServlet(name = "EnvironmentServiceServlet", urlPatterns = {"/environment"})
public class EnvironmentServiceServlet extends HttpServlet {

    /**
     * Handles GET requests and fetches environmental data from RMI and Socket clients.
     * Displays the data on a JSP page.
     *
     * @param _request  The HttpServletRequest object.
     * @param _response The HttpServletResponse object.
     * @throws ServletException If a servlet-specific error occurs.
     * @throws IOException      If an I/O error occurs.
     */
    protected void doGet(HttpServletRequest _request, HttpServletResponse _response) throws ServletException, IOException {
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
            _request.setAttribute("rmiSensors", rmiSensors);
            _request.setAttribute("rmiData", rmiData);
            _request.setAttribute("socketSensors", socketSensors);
            _request.setAttribute("socketData", socketData);

            // Forward the request to a JSP page for rendering
            _request.getRequestDispatcher("/webapp/environment.jsp").forward(_request, _response);

        } catch (RemoteException _e) {
            _e.printStackTrace();
            _response.getWriter().println("Error: Remote server communication failed.");
        }
    }
}
