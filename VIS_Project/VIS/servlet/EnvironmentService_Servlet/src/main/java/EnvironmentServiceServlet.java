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

    private String createTable(String _serverName, IEnvService _envService) throws RemoteException {
        StringBuilder ret = new StringBuilder();
        EnvData[] envData = _envService.requestAll();

        ret.append("<h2 style='text-align: center;'>" + _serverName + "</h2>");
        ret.append("<table style='border-collapse: collapse; width: 100%; margin-top: 20px;'>");
        ret.append("<tr style='background-color: #f2f2f2;'>");
        ret.append("<th style='padding: 12px; text-align: left; border-bottom: 1px solid #ddd;'>Timestamp</th>");
        ret.append("<th style='padding: 12px; text-align: left; border-bottom: 1px solid #ddd;'>Sensor</th>");
        ret.append("<th style='padding: 12px; text-align: left; border-bottom: 1px solid #ddd; min-width: 200px;'>Value(s)</th>");
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
        return ret.toString();
    }

}
