package org.dimigo.gui.chatserv;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServerController{
    @FXML private Text txt_server_address;
    @FXML private Text txt_usernum;
    @FXML private VBox vb_chat;
    @FXML private ListView list_userlist;
    @FXML private ListView<String> list_errorname;
    @FXML private ListView<String> list_errordetail;
    private List<Label> messages = new ArrayList<>();
    private int index = 0;
    static List<String> userlist_sample = new ArrayList<>();
    static ObservableList userlist = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        Thread t = new Thread(new ChatServer());
        t.start();
        showServAddress();
    }



    public void showServAddress()
    {
        try {
            InetAddress ip = InetAddress.getLocalHost();
            txt_server_address.setText("Your Server IP is : " + ip.getHostAddress());
        } catch(Exception e) {
        }
    }

    public void setNumUsers(int a) {
        txt_usernum.setText("Users : " + a);
    }

    public void addUserOnList(String nick) {
        userlist_sample.add(nick);

        userlist.addAll(userlist_sample);

        userlist_sample.clear();
        list_userlist.setItems(userlist);

    }

    public void onChat(String message) {
        Label newmsg = new Label(message);
        newmsg.setFont(new Font(17.0));
        messages.add(newmsg);
        Platform.runLater(() -> {
            vb_chat.getChildren().add(messages.get(index));
            index++;
        });


    }







}

class ChatServer implements Runnable {      // javafx가 이미 해당 쓰레드에서 작업하고 있기 때문에 유저들이 연결요청하는 것을 계속 검사하는 반복문을 넣을 수 없었음. 그래서 멀티쓰레드를 구현하여 해결함.
    final int port = 3333;
    ServerSocket server = null;
    Socket sock = null;
    ChatServerThread sr;
    Thread t;
    HashMap<String, PrintWriter> hm;
    static boolean stop = false;


    @Override
    public void run() {
        while (!stop) {  //계속 연결요청 받음
            try {
                sock = server.accept(); //클라이언트의 소켓 연결 수락(연결요청이 없었으면 Null)
            } catch (IOException e) {
                e.printStackTrace();
            }

            if (sock != null) { //클라이언트 소켓과의 연결요청이 있을 경우
                sr = new ChatServerThread(sock, hm); //채팅 스레드 생성
                t = new Thread(sr); //채팅스레드를 시작
                t.start();
            }
        }
    }

    public ChatServer() {
        try {
            server = new ServerSocket(port); //서버소켓을 생성
            hm = new HashMap<String, PrintWriter>();
            System.out.println( "클라이언트의 접속 대기중..." );

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void serverStop()
    {
        stop = true;
    }
}

