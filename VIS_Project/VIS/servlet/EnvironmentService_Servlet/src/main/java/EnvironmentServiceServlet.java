//
// Created by Antonia Stieger on 14.01.2024.
//

import at.fhooe.sail.vis.Environment_SocketClient;
import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

/**
 * The EnvironmentServiceServlet class is a servlet that communicates with two servers (C++ Server and RMI Server)
 * to retrieve environmental data. It generates an HTML page with a centered table displaying the timestamp, sensor name,
 * and sensor values obtained from the servers.
 */
@WebServlet(name = "EnvironmentServiceServlet", urlPatterns = {"/", "/environment"})
public class EnvironmentServiceServlet extends HttpServlet {

    /**
     * Handles HTTP GET requests. Sets response headers, content type, and retrieves data from servers to generate an HTML page.
     *
     * @param _request  HttpServletRequest object representing the client's request
     * @param _response HttpServletResponse object representing the servlet's response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException      if an I/O error occurs while handling the request
     */
    public void doGet(HttpServletRequest _request, HttpServletResponse _response) throws ServletException, IOException {
        _response.setIntHeader("Refresh", 5);
        _response.setContentType("text/html");
        PrintWriter out = _response.getWriter();

        out.println("<html>");
        out.println("<head><title>Environment Service Servlet</title></head>");
        out.println("<body>");

        try {
            IEnvService socketClient = new Environment_SocketClient();
            out.println(createTable("C++ Server", socketClient));
        } catch (Exception e){
            System.out.println("Exception in C++ Server Request");
            out.println("<p>C++ Server is offline</p>");
        }

        try {
            String adr = "EnvironmentService";
            Registry reg = LocateRegistry.getRegistry();
            IEnvService lookup = (IEnvService) reg.lookup(adr);
            out.println(createTable("RMI Server", lookup));
        } catch (Exception e){
            System.out.println("Exception in RMI Server Request");
            out.println("<p>RMI Server is offline</p>");
        }

        out.println("</body>");
        out.println("</html>");
    }

    /**
     * Creates an HTML table with environmental data obtained from the specified server.
     *
     * @param _serverName the name of the server (C++ Server or RMI Server)
     * @param _envService IEnvService object representing the server interface for environmental data
     * @return a string containing the HTML representation of the table
     * @throws RemoteException if a communication-related exception occurs during the execution of a remote method call
     */
    private String createTable(String _serverName, IEnvService _envService) throws RemoteException {
        StringBuilder ret = new StringBuilder();
        EnvData[] envData = _envService.requestAll();

        ret.append("<h2 style='text-align: center;'>" + _serverName + "</h2>");
        ret.append("<div style='text-align: center;'>"); // Center the table
        ret.append("<table style='border-collapse: collapse; width: 70%; margin: 20px auto; border: 1px solid #ddd; border-radius: 8px; overflow: hidden;'>");
        ret.append("<tr style='background-color: #f2f2f2;'>");
        ret.append("<th style='padding: 12px; text-align: left; border-bottom: 1px solid #ddd;'>Timestamp</th>");
        ret.append("<th style='padding: 12px; text-align: left; border-bottom: 1px solid #ddd;'>Sensor</th>");
        ret.append("<th style='padding: 12px; text-align: left; border-bottom: 1px solid #ddd; min-width: 200px;'>Value</th>");
        ret.append("</tr>");

        for (EnvData sensData : envData) {
            ret.append("<tr>");
            ret.append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'>" + sensData.getTimestamp() + "</td>");
            ret.append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'>" + sensData.getSensorName() + "</td>");
            ret.append("<td style='padding: 8px; border-bottom: 1px solid #ddd;'>");
            for (int i = 0; i < sensData.getValues().length; i++) {
                ret.append(sensData.getValues()[i]);
                if (i < sensData.getValues().length - 1) {
                    ret.append("; ");
                }
            }
            ret.append("</td>");
            ret.append("</tr>");
        }
        ret.append("</table>");
        ret.append("</div>"); // Close the centering div
        return ret.toString();
    }
}
