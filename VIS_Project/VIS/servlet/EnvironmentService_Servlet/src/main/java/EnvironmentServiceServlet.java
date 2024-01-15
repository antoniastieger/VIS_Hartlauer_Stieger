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

@WebServlet(name = "EnvironmentServiceServlet", urlPatterns = {"/", "/environment"})
public class EnvironmentServiceServlet extends HttpServlet {

    public void doGet(HttpServletRequest _request, HttpServletResponse _response) throws ServletException, IOException {
        _response.setIntHeader("Refresh", 5);
        _response.setContentType("text/html");
        PrintWriter out = _response.getWriter();

        out.println("<html>");
        out.println("<head><title>Environment Service Servlet</title></head>");
        out.println("<body>");

        // C++ Server
        try {
            IEnvService socketClient = new Environment_SocketClient();
            out.println(createTable("C++ Server", socketClient));

        } catch (Exception e){
            System.out.println("Exception in C++ Server Request");
            out.println("<p>C++ Server is offline</p>");
        }

        // RMI Server
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

    private String createTable(String serverName, IEnvService envService) throws RemoteException {
        StringBuilder ret = new StringBuilder();

        EnvData[] envData = envService.requestAll();

        ret.append("<h2>" + serverName + "</h2>");
        ret.append("<table>");
        ret.append("<tr>");
        ret.append("<th>Timestamp</th>");
        ret.append("<th>Sensor</th>");
        ret.append("<th>Value(s)</th>");
        ret.append("</tr>");

        for (EnvData sensData : envData){
            ret.append("<tr>");
            ret.append("<td>" + sensData.getTimestamp() + "</td>");
            ret.append("<td>" + sensData.getSensorName() + "</td>");
            ret.append("<td>");
            for (int i = 0; i < sensData.getValues().length; i++){
                ret.append(sensData.getValues()[i]);
                if(i < sensData.getValues().length - 1){
                    ret.append("; ");
                }
            }
            ret.append("</td>");
            ret.append("</tr>");
        }

        ret.append("</table>");

        return ret.toString();
    }
}
