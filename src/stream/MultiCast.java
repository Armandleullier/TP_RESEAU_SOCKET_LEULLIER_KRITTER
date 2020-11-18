package stream;

import java.net.InetAddress;

class MultiCast {
    public static void main(String[] args) throws Exception {
        String nom = args[0];
        InetAddress groupeIP = InetAddress.getByName("239.255.80.84");
        int port = 8084;
        new WritterMulticast(groupeIP, port, nom);
        new ReaderMulticast(groupeIP, port, nom);
    }
}