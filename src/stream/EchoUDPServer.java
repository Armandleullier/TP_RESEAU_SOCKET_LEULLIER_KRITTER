package stream;

import java.io.IOException;
import java.net.*;
import java.util.Enumeration;


public class EchoUDPServer {



    public static void main(String args[]) {

        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket(300);
        } catch (SocketException e) {
            e.printStackTrace();
        }
        byte[] buffer = new byte[1500];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
        while(true) {
            try {
                socket.receive(packet);
                InetAddress clientAddress = packet.getAddress();
                int port = packet.getPort();
                System.out.println("Reception de : " + clientAddress.getHostName() + " : " + new String(packet.getData(), 0, packet.getLength()));
                String g = "PONG";
                byte[] b = g.getBytes();
                DatagramPacket clientPacket = new DatagramPacket(b, b.length, clientAddress, port);
                socket.send(clientPacket);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
