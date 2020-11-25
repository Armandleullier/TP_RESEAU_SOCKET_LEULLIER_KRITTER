package view;

import client.Client;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class Controller {
    @FXML
    private TextField ipAddressArea;
    @FXML
    private TextField portNumberArea;
    @FXML
    private TextField nameArea;
    @FXML
    private Label informationArea;
    @FXML
    private TextArea dialogArea;
    @FXML
    private TextArea messageArea;

    Client client = new Client();
    Socket serverSocket = null;

    /**
     * Display Alert box with the errorMessage
     * @param errorMessage
     */
    private void showAlert(String errorMessage) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(errorMessage);
        alert.showAndWait();
    }

    /**
     * When Se connecter clicked this method get a connexion with the server and create the client thread
     * @param mouseEvent
     */
    public void connect(MouseEvent mouseEvent) {
        boolean elementMissing = false;
        String ipAddressString = ipAddressArea.getText();
        String portNumberString = portNumberArea.getText();
        String nameString = nameArea.getText();

        if(serverSocket != null) {
            try {
                informationArea.setTextFill(Color.LIGHTGREY);
                informationArea.setText("Disconnected");
                client.stopConnexion();
                dialogArea.setText("");
                client.join();
                serverSocket.close();
                client = new Client();
                serverSocket = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (ipAddressString.isBlank()) {
            System.out.println("IP address missing");
            elementMissing = true;
        }
        if (portNumberString.isBlank()) {
            System.out.println("Port Number missing");
            elementMissing = true;
        }
        if (nameString.isBlank()) {
            nameString = "Anonymous";
        }
        if (!elementMissing) {
            System.out.println("Connecting to ip : " + ipAddressString + " port : " + portNumberString);
            try {
                // creation socket ==> connexion
                serverSocket = new Socket(ipAddressString,new Integer(portNumberString));
                if(serverSocket.isConnected()) {
                    client.connexion(serverSocket,nameString);
                    client.setController(this);
                    client.start();
                    informationArea.setTextFill(Color.GREEN);
                    informationArea.setText("Connected");
                    System.out.println("Connected to ip : " + ipAddressString + " port : " + portNumberString);
                }
            } catch (UnknownHostException e) {
                System.err.println("Don't know about host:" + ipAddressString);
                showAlert("Don't know about host:" + ipAddressString);
            } catch (IOException e) {
                System.err.println("Couldn't get I/O for " + "the connection to:"+ ipAddressString);
                showAlert("Couldn't get I/O for " + "the connection to:"+ ipAddressString);
            }
        } else {
            showAlert("Error some informations are missing to get a connexion");
        }
    }

    /**
     * When envoyer clicked tell the client to send a message to the server
     * @param mouseEvent
     */
    public void envoyer(MouseEvent mouseEvent) {
        client.sendMessage(messageArea.getText());
        messageArea.setText("");
    }

    /**
     * Set text to the Dialog area
     * @param text
     */
    public void setDialog (String text) {
        dialogArea.appendText(text);
    }
}
