package stream;

import java.net.InetAddress;

class MultiCast {

    /**
     * Launch a writter and a reder thread for the client
     * notice that name of two client must be different if you want to see the messages
     * @param args name of the client
     * @throws Exception
     */
    public static void main(String[] args) throws Exception {
        String nom = args[0];
        InetAddress groupeIP = InetAddress.getByName("239.255.80.84");
        int port = 8084;
        new WritterMulticast(groupeIP, port, nom);
        new ReaderMulticast(groupeIP, port, nom);
    }
}