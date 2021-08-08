/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.Server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Amr
 */
public class Server {

    private int port;
    private String ip;
    private ServerSocket serverSocket;
    private ArrayList<Socket> client_list;
    private Auth auth;
    private ProcessTracker tracker;
    private LinkedHashMap<Integer, String> hash_table;

    public Server(String ip, int port) {
        this.ip = ip;
        this.port = port;
        auth = new Auth();
        client_list = new ArrayList<>();
        hash_table = new LinkedHashMap<>();
        tracker = new ProcessTracker();
    }

    public void Start() {
        try {
            System.out.println("Starting Server ...");
            serverSocket = new ServerSocket(port);
            while (true) {
                Socket client = serverSocket.accept();
                client_list.add(client);
                UserThread user = new UserThread(client, auth, this , tracker , hash_table);
                user.Start();
            }
        } catch (IOException ex) {
            Logger.getLogger(Server.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
