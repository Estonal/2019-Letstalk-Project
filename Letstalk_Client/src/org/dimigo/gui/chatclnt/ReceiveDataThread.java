package org.dimigo.gui.chatclnt;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.Socket;

public class ReceiveDataThread implements Runnable{
    Socket client;
    BufferedReader bfr;
    String receiveData;

    public ReceiveDataThread(Socket s, BufferedReader ois){
        client = s;
        this.bfr = ois;
    }
    @Override
    public void run(){
        try{
            while( ( receiveData = bfr.readLine() ) != null ) {
                System.out.println(receiveData);
                ClntController.chatController.onChat(receiveData);
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        finally{
            try{
                bfr.close();
                client.close();
            }catch(IOException e2){
                e2.printStackTrace();
            }
        }
    }
}