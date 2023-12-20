package at.fhooe.sail.vis;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Environment_SocketClient {
    /*
    @Override
    public String[] requestEnvironmentDataTypes() {
        // Implementation
        return new String[0];
    }

    @Override
    public EnvData requestEnvironmentData(String _type) {
        // Implementation
        return null;
    }

    @Override
    public EnvData[] requestAll() {
        // Implementation
        return new EnvData[0];
    }*/
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
    }
}