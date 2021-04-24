package org.dimigo.gui.chatclnt;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ClntController implements Initializable {

    public static ChatController chatController;

    @FXML
    private Button btn_login;
    @FXML
    private Button btn_register;
    @FXML
    private Button btn_help;
    @FXML
    private TextField tf_nick;
    @FXML
    private TextField tf_ip;

    public ChatClient chatClient;



    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    public void tryLogin(ActionEvent event) throws IOException {
        chatClient = new ChatClient(tf_nick.getText(), tf_ip.getText());
//        Thread t = new Thread(chatClient);
//        t.start();

        Stage stage = (Stage) btn_login.getScene().getWindow();
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Chatting_Client.fxml"));
        Parent root = loader.load();
        chatController = loader.getController();
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();


    }




    @FXML
    public void tryRegister(ActionEvent event) { // 이름을 잘못입력함. 사실 DoNothing 버튼. 시간비면 수정함

    }

    @FXML
    public void login_help(ActionEvent event) { // help버튼 누를 시

    }


}

class ChatClient {
    String ipAddress;
    static final int port = 3333;
    Socket client = null;
    BufferedReader read;
    PrintWriter oos;
    BufferedReader ois;
    String sendData;
    String receiveData;

    String user_id;
    ReceiveDataThread rt;
    boolean endflag = false;


    public void sendMessage(String message) {
        if(!Main.in_button_pressed) {return;}
        sendData = message;

        if (sendData.equals("/quit")) {
            endflag = true;
            System.out.print("클라이언트의 접속을 종료합니다. ");
            try {
                ois.close();
                oos.close();
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.exit(0);
        }

        System.out.println(sendData);
        oos.println(sendData);
        oos.flush();

        Main.in_button_pressed = false;


    }

//    @Override
//    public void run() {
//        try {
//            while (true) {
//                if(!Main.in_button_pressed) {continue;}
//                System.out.println("Hey are u there");
////                sendData = ClntController.chatController.getMessageText(); // 버튼을 누르면, 전송되도록
//
//                oos.println(sendData);
//                oos.flush();
//
//                Main.in_button_pressed = false;
//
//                if (sendData.equals("/quit")) {
//                    endflag = true;
//                    break;
//                }
//
//            }
//            System.out.print("클라이언트의 접속을 종료합니다. ");
//            System.exit(0);
//        } catch (Exception e) {
//            if (!endflag) e.printStackTrace();
//        } finally {
//            try {
//                ois.close();
//                oos.close();
//                client.close();
//                System.exit(0);
//            } catch (IOException e2) {
//                e2.printStackTrace();
//            }
//        }
//    }


    public ChatClient(String id, String ip) {
        user_id = id;
        ipAddress = ip;
        try {
            System.out.println("**** 클라이언트*****");
            client = new Socket(ipAddress, port);

            read = new BufferedReader(new InputStreamReader(System.in));
            ois = new BufferedReader(new InputStreamReader(client.getInputStream()));
            oos = new PrintWriter(client.getOutputStream());

            oos.println(user_id);
            oos.flush();

            rt = new ReceiveDataThread(client, ois);
            Thread t = new Thread(rt);
            t.start();

        } catch (Exception e) {
            if (!endflag) e.printStackTrace();
        }
    }

}