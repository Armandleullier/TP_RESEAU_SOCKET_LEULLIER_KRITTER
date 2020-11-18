package stream;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class ReaderMulticast extends Thread {
    InetAddress groupeIP;
    int port;
    String nom;
    MulticastSocket socketReception;

    ReaderMulticast (InetAddress groupeIP, int port, String nom)  throws Exception {
        this.groupeIP = groupeIP;
        this.port = port;
        this.nom = nom;
        socketReception = new MulticastSocket(port);
        socketReception.joinGroup(groupeIP);
        start();
    }

    public void run() {
        DatagramPacket message;
        byte[] contenuMessage;
        String texte;
        ByteArrayInputStream lecteur;

        while(true) {
            contenuMessage = new byte[1024];
            message = new DatagramPacket(contenuMessage, contenuMessage.length);
            try {
                socketReception.receive(message);
                texte = (new DataInputStream(new ByteArrayInputStream(contenuMessage))).readUTF();
                if (!texte.startsWith(nom)) {
                    System.out.println(texte);
                }
            }
            catch(Exception exc) {
                System.out.println(exc);
            }
        }
    }
}
