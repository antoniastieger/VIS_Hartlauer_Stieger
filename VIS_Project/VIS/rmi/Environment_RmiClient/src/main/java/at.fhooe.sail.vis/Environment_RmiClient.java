package at.fhooe.sail.vis;

import at.fhooe.sail.vis.general.EnvData;
import at.fhooe.sail.vis.general.IEnvService;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class Environment_RmiClient implements IEnvService {

    private PrintWriter mOut;
    private Scanner mScanner;

    public Environment_RmiClient() {
        try {
            Socket socket = new Socket("127.0.0.1", 4949);

            // Initialize mOut and mScanner using the socket's streams
            mOut = new PrintWriter(socket.getOutputStream(), true);
            mScanner = new Scanner(socket.getInputStream());

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String[] requestEnvironmentDataTypes() throws RemoteException {
        mOut.print("getSensortypes()#");
        mOut.flush();

        String response = mScanner.next();
        if (response.charAt(0) == 0) {
            response = response.substring(1);
        }

        response = response.replaceAll("#", "");

        return response.split(";");
    }

    @Override
    public EnvData requestEnvironmentData(String _type) throws RemoteException {
        mOut.print("getSensor(" + _type + ")#");
        mOut.flush();

        String response = mScanner.next();

        // Remove trailing '#' character
        response = response.replaceAll("#", "");

        String[] parts = response.split("\\|");

        long timestamp = Long.parseLong(parts[0].substring(1));
        String[] values = parts[1].split(";");

        int getLength = values.length;
        int[] intValues = new int[getLength];

        for (int i = 0; i < getLength; i++) {
            intValues[i] = Integer.parseInt(values[i]);
        }

        return new EnvData(_type, timestamp, intValues);
    }

    @Override
    public EnvData[] requestAll() throws RemoteException {
        String[] allSensorTypes = requestEnvironmentDataTypes();

        EnvData[] allData = new EnvData[allSensorTypes.length];
        for (int i = 0; i < allData.length; i++) {
            allData[i] = requestEnvironmentData(allSensorTypes[i]);
        }

        return allData;
    }

    public static void main(String[] _args) throws RemoteException {
        IEnvService service = new Environment_RmiClient();
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
                Thread.sleep(10000);
            } catch (Exception _e) {
                _e.printStackTrace();
            }
        } // while true
    }
}