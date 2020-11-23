package stream;

import java.io.IOException;
import java.net.*;
import java.util.*;


public class EchoUDPServer {

    static void doService(DatagramSocket socket) {
        byte[] buffer = new byte[1500];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        Set<Integer> clientsConnected = new HashSet<Integer>();
        while(true) {
            try {
                socket.receive(packet);
                InetAddress clientAddress = packet.getAddress();
                int port = packet.getPort();
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Reception de : " + port + " : " + message);
                handleReception(message,socket,clientAddress,port,clientsConnected);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void handleReception(String messsage, DatagramSocket socket,InetAddress clientAddress,int port,Set<Integer> clientsConnected) {
        String response = "";
        if (messsage.equals("connect") && !clientsConnected.contains(port)) {
            clientsConnected.add(port);
            response =  "A new participant has join the chat";
        } else if (messsage.equals("disconnect") && clientsConnected.contains(port)) {
            clientsConnected.remove(port);
            response =  "A participant has left the chat";
        } else {
            response = messsage;
        }
        try {
            byte[] b = response.getBytes();
            for (Integer clientPort :clientsConnected) {
                DatagramPacket clientPacket = new DatagramPacket(b, b.length, clientAddress, clientPort);
                socket.send(clientPacket);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String args[]) {
        DatagramSocket socket = null;
        if (args.length != 1) {
            System.out.println("Usage: java EchoServer <EchoServer port>");
            System.exit(1);
        }
        try {
            socket = new DatagramSocket(Integer.parseInt(args[0]));
        } catch (SocketException e) {
            e.printStackTrace();
        }
        doService(socket);
    }
}
