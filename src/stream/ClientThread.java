/***
 * ClientThread
 * Example of a TCP server
 * Date: 14/12/08
 * Authors:
 */

package stream;

import java.io.*;
import java.net.*;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedList;
import java.util.List;

public class ClientThread extends Thread {

    private static List<Socket> socketList = new LinkedList<Socket>();
    private Socket clientSocket;

    /**
     * Add the client socket s to the socketList and set the client socket with s
     * @param s Client socket
     */
    ClientThread(Socket s) {
        socketList.add(s);
        clientSocket = s;
    }

    /**
     * receives a request from client then sends an echo to the clients connected in socketList
     */
    public void run() {
        try {
            loadHistory();
            //configuration of the input stream where the client writes his messages
            BufferedReader socIn = null;
            socIn = new BufferedReader(
                    new InputStreamReader(clientSocket.getInputStream()));
            //the server starts to listen to the client
            while (true) {
                String line = socIn.readLine();
                if(line.contains("disconnect")) {
                    PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
                    socOut.println("disconnect");
                    break;
                } else {
                    //add the message to the history
                    addMessageToHistoryFile(line);
                    sendMessage(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in Server:" + e);
        }
        System.out.println(clientSocket.getInetAddress() + "end of the thread");
    }

    /**
     * Load the history located at ../dataBase/history.txt
     * @throws IOException
     * @throws InterruptedException
     */
    public void loadHistory () throws IOException, InterruptedException {
        File file = new File("../dataBase/history.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String message;
        PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
        while ((message = br.readLine()) != null) {
            socOut.println(message);
            socOut.flush();
        }
    }

    /**
     * Add message to the history located at ../dataBase/history.txt
     * @param message message to save
     * @throws IOException
     */
    public void addMessageToHistoryFile (String message) throws IOException {
        FileWriter fw = new FileWriter("../dataBase/history.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(message);
        bw.newLine();
        bw.close();
    }

    /**
     * Send message to all the socketList
     * @param message message to send
     */
    private void sendMessage(String message) {
        //send the message to all the connexion of the server
        for (Socket s : socketList) {
            try {
                PrintStream socOut = new PrintStream(s.getOutputStream());
                socOut.println(message);
            } catch (Exception e) {
                System.err.println("Error in Server:" + e);
            }
        }
    }
}

  