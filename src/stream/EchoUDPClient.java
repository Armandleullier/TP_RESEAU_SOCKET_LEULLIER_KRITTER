package stream;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.*;

public class EchoUDPClient extends Thread{
    private Boolean isAWritter;
    private DatagramSocket clientSock;
    private InetAddress serverAddress;
    private int serverPort;
    private static Boolean isOpen;
    private String name;

    public EchoUDPClient (Boolean isAWritter, DatagramSocket clientSock, InetAddress serverAddress, int serverPort, String name) {
        this.isAWritter = isAWritter;
        this.clientSock = clientSock;
        this.serverAddress = serverAddress;
        this.serverPort = serverPort;
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
        DatagramPacket packet;
        byte[] buf = new byte[256];
        while (isOpen) {
            try {
                packet = new DatagramPacket(buf, buf.length);
                this.clientSock.receive(packet);
                String received = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Response: " + received);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for "
                        + "the connection to:"+ this.serverAddress.getHostName());
                System.exit(1);
            }
        }
        this.clientSock.close();
    }

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
            line = stdIn.readLine();
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

    public static void main(String[] args) throws IOException {

        EchoUDPClient writter = null;
        EchoUDPClient reader = null;

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
            System.out.println("Response: " + received);

        } catch (UnknownHostException e) {
            System.err.println("Don't know about host:" + args[0]);
            System.exit(1);
        } catch (IOException e) {
            System.err.println("Couldn't get I/O for "
                    + "the connection to:"+ args[0]);
            System.exit(1);
        }

        writter = new EchoUDPClient(true, clientSock,serverAddress, serverPort, args[2]);
        reader = new EchoUDPClient(false,clientSock,serverAddress, serverPort, args[2]);
        writter.start();
        reader.start();

    }
}
