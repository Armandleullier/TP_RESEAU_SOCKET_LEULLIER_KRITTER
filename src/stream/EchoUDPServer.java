package stream;

import java.io.IOException;
import java.net.*;
import java.util.*;


public class EchoUDPServer {

    static void doService(DatagramSocket socket) {
        byte[] buffer = new byte[1500];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(true) {
            try {
                socket.receive(packet);
                InetAddress clientAddress = packet.getAddress();
                int port = packet.getPort();
                String message = new String(packet.getData(), 0, packet.getLength());
                System.out.println("Reception de : " + clientAddress.getHostName() + " : " + message);
                handleReception(message,socket,clientAddress,port);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    static void handleReception(String messsage, DatagramSocket socket,InetAddress clientAddress,int port) {
        Map<InetAddress,Integer> clientsConnected = new HashMap<InetAddress,Integer>();
        String response = "";
        if (messsage.equals("connect") && !clientsConnected.containsKey(clientAddress)) {
            clientsConnected.put(clientAddress,port);
            response = clientAddress + "has join the chat";
        } else if (messsage.equals("disconnect") && clientsConnected.containsKey(clientAddress)){
            clientsConnected.remove(clientAddress);
            response = clientAddress + "has left the chat";
        } else {
            response = messsage;
        }
        try {
            byte[] b = messsage.getBytes();
            for (Map.Entry<InetAddress,Integer> client :clientsConnected.entrySet()) {
                DatagramPacket clientPacket = new DatagramPacket(b, b.length, client.getKey(), client.getValue());
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
