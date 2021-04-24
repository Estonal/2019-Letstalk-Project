package org.dimigo.gui.chatclnt;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class ChatController implements Initializable {
    @FXML
    private BorderPane bp_pane;
    @FXML
    public VBox vb_chat;
    @FXML
    private TextField tf_message;
    @FXML
    private Button btn_enter;
    @FXML
    private Text txt_ip;
    @FXML
    private Text txt_nick;
    @FXML
    private Tab tab_chat;
    @FXML
    private Tab tab_users;

    @FXML
    private ScrollPane sp_chat;
    @FXML
    private SplitPane spl_chat;


    private List<Label> messages = new ArrayList<>();
    private int index = 0;



    @FXML
    public void showIP(String ip) {
        txt_ip.setText(ip);
    }


    @FXML
    public void btn_Enter() {
        String s = tf_message.getText();
        if(!s.equals("")) {
            System.out.println("sendmessge");
            Main.in_button_pressed = true;
            Main.clntcontroller.chatClient.sendMessage(s);
            tf_message.setText("");
        }
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

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }
}
