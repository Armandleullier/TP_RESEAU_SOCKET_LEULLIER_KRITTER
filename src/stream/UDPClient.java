package stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;

public class UDPClient extends Thread{
    private Boolean isAWritter;
    private DatagramSocket clientSock;
    private InetAddress serverAddress;
    private int serverPort;
    private static Boolean isOpen;
    private String name;

    /**
     * @param isAWritter if the tread was meant to write
     * @param name name of the client
     * @param clientSock socket of the client
     * @param serverAddress ip of the server
     * @param serverPort port of the server
     */
    public UDPClient(Boolean isAWritter, DatagramSocket clientSock, InetAddress serverAddress, int serverPort, String name) {
        this.isAWritter = isAWritter;
        this.clientSock = clientSock;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
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
        DatagramPacket packet;
        byte[] buf = new byte[256];
        while (isOpen) {
            try {
                packet = new DatagramPacket(buf, buf.length);
                this.clientSock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                if(!received.startsWith(name)) {
                    System.out.println(received);
                }
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for "
                        + "the connection to:"+ this.serverAddress.getHostName());
                System.exit(1);
            }
        }
        this.clientSock.close();
    }

    /**
     * Send a message when the client press enter in the System.in
     * @throws IOException
     */
    public void writter () throws IOException {
        String line;
        BufferedReader stdIn = null;
        DatagramPacket packet;
        byte[] buf;

        try {
            stdIn = new BufferedReader(new InputStreamReader(System.in));
        } catch (Exception e) {
            System.out.println("writting error " + e);
        }
        while (true) {
            line = name + ": " + stdIn.readLine();
            if (line.equals(".")) {
                isOpen = false;
                break;
            }
            buf = line.getBytes();
            try {
                packet = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
                this.clientSock.send(packet);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for "
                        + "the connection to:"+ this.serverAddress.getHostName());
                System.exit(1);
            }
        }
        clientSock.close();
    }

    /**
     *  main method
     * @param args 0: ipAddress of the server 1: portNumber 2: Name of the client
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {

        UDPClient writter = null;
        UDPClient reader = null;

        if (args.length != 3) {
            System.out.println("Usage: java EchoClient <EchoServer host> <EchoServer port> <EchoServer ClientName>");
            System.exit(1);
        }

        String connectMessage = "connect";
        DatagramSocket clientSock = new DatagramSocket();
        byte[] buf;
        InetAddress serverAddress = InetAddress.getByName(args[0]);
        int serverPort = new Integer(args[1]).intValue();

        try {
            buf = connectMessage.getBytes();
            DatagramPacket packet = new DatagramPacket(buf, buf.length, serverAddress, serverPort);
            clientSock.send(packet);

            buf = new byte[256];
            packet = new DatagramPacket(buf, buf.length);
            clientSock.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println(received);

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        writter = new UDPClient(true, clientSock,serverAddress, serverPort, args[2]);
        reader = new UDPClient(false,clientSock,serverAddress, serverPort, args[2]);
        writter.start();
        reader.start();

    }
}
