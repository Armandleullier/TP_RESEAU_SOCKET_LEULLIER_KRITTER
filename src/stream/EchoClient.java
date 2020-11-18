/***
 * EchoClient
 * Example of a TCP client 
 * Date: 10/01/04
 * Authors:
 */
package stream;

import java.io.*;
import java.net.*;



public class EchoClient extends Thread {
    private Boolean isAWritter;
    private Socket echoSocket;
    private static Boolean isOpen;
    private String name;

    public EchoClient (Boolean isAWritter, Socket echoSocket, String name) {
        this.isAWritter = isAWritter;
        this.echoSocket = echoSocket;
        this.isOpen = true;
        this.name = name;
    }

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
  *  accepts a connection, receives a message from client then sends an echo to the client
  **/
    public static void main(String[] args) throws IOException {

        Socket echoSocket = null;
        String name = "unknown";
        EchoClient writter = null;
        EchoClient reader = null;

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

        writter = new EchoClient(true, echoSocket,name);
        reader = new EchoClient(false,echoSocket,name);
        writter.start();
        reader.start();
    }
}


