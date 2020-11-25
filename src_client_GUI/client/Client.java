package client;

import javafx.application.Platform;
import view.Controller;

import java.io.*;
import java.net.*;


public class Client extends Thread {
    private Socket serverSocket;
    private Boolean isOpen;
    private String name;
    private Controller controller;

    /**
     * DefaultConstructor
     */
    public Client () {
        this.serverSocket = null;
        this.isOpen = false;
        this.name = null;
    }

    /**
     * set client attributes to receive and send messages
     * @param echoSocket serverSocket
     * @param name name of the client
     */
    public void connexion (Socket echoSocket, String name) {
        this.serverSocket = echoSocket;
        this.isOpen = true;
        this.name = name;
    }

    /**
     * The client thread listen to the server
     */
    public void run () {
        System.out.println("Start a new client thread");
        BufferedReader socIn = null;
        try {
            socIn = new BufferedReader(new InputStreamReader(serverSocket.getInputStream()));
            while (isOpen) {
                String received = socIn.readLine();
                String text = "";
                System.out.println(received);
                text = received + '\n';
                if(received.contains("disconnect")) {
                    break;
                }
                String finalText = text;
                Platform.runLater( () -> controller.setDialog(finalText));
            }
        } catch (Exception e) {
            System.out.println("reading error " + e);
        }
        System.out.println("End of previous Client Thread");
    }

    /**
     * Send a messsage to the server
     * @param message message receive frome the server
     */
    public void sendMessage(String message) {
        PrintStream socOut = null;
        try {
            socOut= new PrintStream(serverSocket.getOutputStream());
            socOut.println(name + ": " + message);
        } catch (Exception e) {
            System.out.println("writting error " + e);
        }
    }

    /**
     * Stop properly the connexion with the server
     */
    public void stopConnexion () {
        this.isOpen = false;
        this.interrupt();
        sendMessage("disconnect");
    }

    /**
     * set the Client controller to tell him to display the messages
     * @param c
     */
    public void setController (Controller c) {
        this.controller = c;
    }
}


