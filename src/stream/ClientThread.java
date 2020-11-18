/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.util.LinkedList;
import java.util.List;

public class ClientThread
        extends Thread {

    private static List<Socket> socketList = new LinkedList<Socket>();
    private Socket clientSocket;
    ClientThread(Socket s) {
        socketList.add(s);
        clientSocket = s;
    }

    /**
     * receives a request from client then sends an echo to the client
     * @param clientSocket the client socket
     **/
    public void run() {
        try {
            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            while (true) {
                String line = socIn.readLine();
                sendMessage(line);
            }
        } catch (Exception e) {
            System.err.println("Error in EchoServer:" + e);
        }
    }

    private void sendMessage(String message) {
        for (Socket s : socketList) {
            try {
                PrintStream socOut = new PrintStream(s.getOutputStream());
                socOut.println(message);
            } catch (Exception e) {
                System.err.println("Error in EchoServer:" + e);
            }
        }
    }
}

  