package org.dimigo.gui.chatserv;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.ResourceBundle;

public class Main_Serv extends Application {
    public static FXMLLoader loader;
    public static Parent root;
    public static ServerController controller;

    public static ServerController getServerController() {
        return controller;
    }
    @Override
    public void start(Stage primaryStage) throws Exception{
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chatting_Server.fxml"));
        Parent root = loader.load();
        controller = loader.getController();

        primaryStage.setTitle("ChatServer");
        primaryStage.setScene(new Scene(root, 775, 440));
        primaryStage.show();




    }


    public static void main(String[] args) {

       launch(args);
    }
}


