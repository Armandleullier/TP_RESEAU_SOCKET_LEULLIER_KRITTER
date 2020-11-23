/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;



public class TCPClient extends Thread {
    private Boolean isAWritter;
    private Socket echoSocket;
    private static Boolean isOpen;
    private String name;

    /**
     * @param isAWritter if the tread was meant to write
     * @param echoSocket server socket connexion
     * @param name name of the client
     */
    public TCPClient(Boolean isAWritter, Socket echoSocket, String name) {
        this.isAWritter = isAWritter;
        this.echoSocket = echoSocket;
        this.isOpen = true;
        this.name = name;
    }

    /**
     * lauchn writter if the thread was meant to write or reader if the thread was meant to read
     */
    public void run () {
        if(isAWritter) {
            try {
                this.writter();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                this.reader();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Display messages when received
     * @throws IOException
     */
    public void reader () throws IOException {
        String line;
        BufferedReader socIn = null;
        try {
            socIn = new BufferedReader(
                    new InputStreamReader(echoSocket.getInputStream()));
        } catch (Exception e) {
            System.out.println("reading error " + e);
        }
        while (true && isOpen) {
            String s = socIn.readLine();
            System.out.println(s);
        }
        socIn.close();
    }

    /**
     * Send a message when the client press enter in the System.in
     * @throws IOException
     */
    public void writter () throws IOException {
        String line;
        PrintStream socOut = null;
        BufferedReader stdIn = null;

        try {
            socOut= new PrintStream(echoSocket.getOutputStream());
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.out.println("writting error " + e);
        }
        while (true) {
            line = stdIn.readLine();
            if (line.equals(".")) {
                isOpen = false;
                break;
            }
            socOut.println('\r'+ name + ": " + line);
        }
        socOut.close();
        echoSocket.close();
    }

    /**
     *  main method
     * @param args 0: ipAddress of the server 1: portNumber 2: Name of the client
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        String name = "unknown";
        TCPClient writter = null;
        TCPClient reader = null;

        if (args.length != 3) {
          System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port> <EchoServer ClientName>");
          System.exit(1);
        }
        try {
      	    // creation socket ==> connexi
            echoSocket = new Socket(args[0],new Integer(args[1]).intValue());
            name = args[2];
        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                               + "the connection to:"+ args[0]);
            System.exit(1);
        }

        writter = new TCPClient(true, echoSocket,name);
        reader = new TCPClient(false,echoSocket,name);
        writter.start();
        reader.start();
    }
}


