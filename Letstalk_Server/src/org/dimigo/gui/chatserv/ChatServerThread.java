package org.dimigo.gui.chatserv;

import org.dimigo.gui.chatserv.Main_Serv;
import org.dimigo.gui.chatserv.ServerController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.HashMap;

class ChatServerThread implements Runnable {
    Socket sock;
    BufferedReader bfr;
    PrintWriter prwriter;

    String user_id;
    HashMap<String, PrintWriter> hm;
    InetAddress ip;
    String msg;
    ServerController controller = Main_Serv.getServerController();

    public ChatServerThread(Socket s, HashMap<String, PrintWriter> h) {

        sock = s;
        hm = h;
        try {
            bfr = new BufferedReader(new InputStreamReader(sock.getInputStream()));
            prwriter = new PrintWriter(sock.getOutputStream());
            user_id = bfr.readLine();
            ip = sock.getInetAddress();

            System.out.println(ip + "로부터 " + user_id + "님이 접속하였습니다.");
            controller.onChat(ip + "로부터 " + user_id + "님이 접속하였습니다.");
            controller.addUserOnList(user_id);
            broadcast(user_id + "님이 접속하셨습니다.");

            synchronized (hm) {
                hm.put(user_id, prwriter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    public void run() {
        String receiveData;
        try {
            while ((receiveData = bfr.readLine()) != null) {

                if (receiveData.equals("/quit")) {
                    synchronized (hm) {
                        hm.remove(user_id);
                    }

                    break;
                } else if (receiveData.indexOf("/to") >= 0) {
                    sendMsg(receiveData);
                } else {
                    System.out.println(user_id + " >> " + receiveData); // 메시지 출력하는곳
                    controller.onChat(user_id + " >> " + receiveData);
                    broadcast(user_id + " >> " + receiveData);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            synchronized (hm) {
                hm.remove(user_id);
            }

            broadcast(user_id + "님이 퇴장했습니다.");
            System.out.println(user_id + "님이 퇴장했습니다.");

            try {
                if (sock != null) {
                    bfr.close();
                    prwriter.close();
                    sock.close();
                }
            } catch (Exception e) {
            }
        }
    }


    public void broadcast(String message) {  //전체채팅
        synchronized (hm) {
            try {
                for (PrintWriter prwriter : hm.values()) {
                    prwriter.println(message);
                    prwriter.flush();
                }
            } catch (Exception e) {
            }
        }
    }


    public void sendMsg(String message) {
        int begin = message.indexOf(" ") + 1;
        int end = message.indexOf(" ", begin);

        if (end != -1) {
            String id = message.substring(begin, end);
            String msg = message.substring(end + 1);
            PrintWriter prwriter = hm.get(id);

            try {
                if (prwriter != null) {
                    prwriter.println(user_id + "님이 다음과 같은 귓속말을 보내셨습니다. : " + msg);
                    prwriter.flush();
                }

            } catch (Exception e) {

            }
        }
    }
}