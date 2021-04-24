package org.dimigo.gui.chatclnt;

import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

public class Main extends Application {
    public static ClntController clntcontroller;
    public static ClntController getClntController() { return clntcontroller;}
    public static ChatController chatController;
    public static boolean in_button_pressed = false;


    @Override
    public void start(Stage primaryStage) throws Exception{
        Stage stage = primaryStage;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Login1.fxml"));
        Parent root = loader.load();
        clntcontroller = loader.getController();
        Scene main = new Scene(root, 700, 400);
        stage.setTitle("LetsTalk");
        stage.setScene(main);
        stage.show();

        ////dlsakdjskdjal



    }


    public static void main(String[] args) {
        launch(args);
    }
}


