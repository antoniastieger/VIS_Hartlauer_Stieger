package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;


public class Environment_SocketClient implements IEnvService {

    @Override
    public String[] requestEnvironmentDataTypes() { // TODO: implement
        return new String[]{"Wind", "Temperature", "Humidity", "Pressure", "Rain", "Light"};
    }

    @Override
    public EnvData requestEnvironmentData(String _type) { // TODO: implement
        return new EnvData(_type, System.currentTimeMillis(), new int[]{1, 2, 3});
    }

    @Override
    public EnvData[] requestAll() { // TODO: implement
        return new EnvData[]{
                new EnvData("Wind", System.currentTimeMillis(), new int[]{1, 2, 3}),
                new EnvData("Temperature", System.currentTimeMillis(), new int[]{4, 5, 6}),
                new EnvData("Humidity", System.currentTimeMillis(), new int[]{7, 8, 9}),
                new EnvData("Pressure", System.currentTimeMillis(), new int[]{10, 11, 12}),
                new EnvData("Rain", System.currentTimeMillis(), new int[]{13, 14, 15}),
                new EnvData("Light", System.currentTimeMillis(), new int[]{16, 17, 18})
        };
    }

    public static void main(String[] args) {
        try {
            Socket socket = new Socket("127.0.0.1", 4949);

            OutputStream out = socket.getOutputStream();
            InputStream in = socket.getInputStream();

            Scanner consoleReader = new Scanner(System.in);

            String msg;

            while(true) {
                System.out.print("Enter a message: ");
                msg = consoleReader.nextLine() + "\n";

                if("exit\n".equalsIgnoreCase(msg)) {
                    break;
                }

                System.out.println("Sending: " + msg);

                out.write((msg).getBytes());
                out.flush();

                BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                String response = reader.readLine();

                System.out.println("Received: " + response);
            }

            in.close();
            socket.close();
            consoleReader.close();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        IEnvService service = new Environment_SocketClient();
        while (true) {
            String[] sensors = service.requestEnvironmentDataTypes();

            for (String sensor : sensors) {
                EnvData dataO = service.requestEnvironmentData(sensor);
                System.out.print(dataO);
                System.out.println();
                System.out.println("*****************************");
            } // for sensor

            System.out.println();
            System.out.println();

            EnvData[] dataOs = service.requestAll();
            for (EnvData dataO : dataOs) {
                System.out.println(dataO);
            } // for data

            try {
                Thread.sleep(1000000);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
        } // while true
    }
}