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
    private static List<String> history = new LinkedList<String>();
    private Socket clientSocket;

    ClientThread(Socket s) {
        socketList.add(s);
        clientSocket = s;
    }

    /**
     * receives a request from client then sends an echo to the client
     **/
    public void run() {
        try {
            //configuration of the input stream where the client writes his messages
            loadHistory();
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
                    history.add(line);
                    addMessageToHistoryFile(line);
                    sendMessage(line);
                }
            }
        } catch (Exception e) {
            System.err.println("Error in Server:" + e);
        }
        System.out.println(clientSocket.getInetAddress() + "end of the thread");
    }

    public void loadHistory () throws IOException, InterruptedException {
        File file = new File("history.txt");
        BufferedReader br = new BufferedReader(new FileReader(file));
        String message;
        PrintStream socOut = new PrintStream(clientSocket.getOutputStream());
        while ((message = br.readLine()) != null) {
            socOut.println(message);
            socOut.flush();
        }
    }

    public void addMessageToHistoryFile (String message) throws IOException {
        FileWriter fw = new FileWriter("history.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(message);
        bw.newLine();
        bw.close();
    }

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

  