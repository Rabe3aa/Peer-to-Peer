/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.Server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.LinkedHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;
import peer2peer.Client.Client;
import peer2peer.util.Hash;
import peer2peer.util.Request;
import peer2peer.util.RequestMan;
import peer2peer.util.Services;
import peer2peer.util.Status;

/**
 *
 * @author Amr
 */
public class UserThread implements Runnable {

    private Thread thread;
    private Socket socket;
    private Server server;
    private Auth auth;
    private ProcessTracker tracker;
    private LinkedHashMap<Integer, String> hash_table;

    UserThread(Socket socket, Auth auth, Server server, ProcessTracker tracker, LinkedHashMap<Integer, String> hash_table) {
        this.socket = socket;
        this.server = server;
        this.auth = auth;
        this.tracker = tracker;
        this.hash_table = hash_table;
        thread = new Thread(this);

    }

    public void Start() {
        thread.start();
    }

    @Override
    public void run() {
        while (true) {
            Request request = ReadRequest();
            System.out.println("new request has come");
            HandleServices(request);
        }
    }

    public void HandleServices(Request request) {

        if (Services.SIGN_UP_CLIENT.equals(request.getService())) {
            boolean status = auth.SignUp(request.getData());
            Request response = new Request();
            response.setData(request.getData());
            response.setService(Services.SIGN_UP_CLIENT);
            if (!status) {
                response.setStatus_code(Status.ERROR);
            } else {
                response.setStatus_code(Status.OK);
            }
            SendRequest(response);

        } else if (Services.SIGN_IN_CLIENT.equals(request.getService())) {
            boolean status = auth.login(request.getData());
            Request response = new Request();
            response.setData(request.getData());
            response.setService(Services.SIGN_IN_CLIENT);
            if (!status) {
                response.setStatus_code(Status.ERROR);
            } else {
                response.setStatus_code(Status.OK);
            }
            SendRequest(response);
        } else if (Services.ADDFILE_CLIENT.equals(request.getService())) {
            // 0 -> file hashed 
            // 1 -> user_id
            // 2 -> file content        

            String[] convertDataToArray = RequestMan.convertDataToArray(request.getData());
            int file_number_hashed = Integer.parseInt(convertDataToArray[0]);
            int user_id = Integer.parseInt(convertDataToArray[1]);
            String file_content = convertDataToArray[2];
            
            hash_table.put(file_number_hashed, file_content);
            tracker.AddUserToFile(file_number_hashed, user_id);
        } else if (Services.DOWNLOADFILE_CLIENT.equals(request.getService())) {
            // 0 -> from_user_id 
            // 1 -> cur_user_id 
            // 2 -> file_number      
            String[] convertDataToArray = RequestMan.convertDataToArray(request.getData());
            int from_user_id = Integer.parseInt(convertDataToArray[0]);
            int cur_user_id = Integer.parseInt(convertDataToArray[1]);
            int file_number = Integer.parseInt(convertDataToArray[2]);
            int incryKey = Hash.incryKey(file_number, from_user_id);
         
            String file_data = hash_table.get(incryKey);
            // send response 
            Request response = new Request();
            response.setData(file_data);
            response.setService(Services.DOWNLOADFILE_CLIENT);
            if (file_data == null) {
                response.setStatus_code(Status.ERROR);
            } else {
                response.setStatus_code(Status.OK);   
                tracker.AddUserToFile(incryKey, cur_user_id);
            }
            SendRequest(response);

        }
    }

    private Request ReadRequest() {
        InputStream input = null;
        Request request = null;
        String read = null;
        try {
            input = socket.getInputStream();
            DataInputStream in = new DataInputStream(input);
            read = in.readUTF();
            request = new Request();
            request.ConvertToRequest(read);
        } catch (IOException ex) {
            Logger.getLogger(UserThread.class.getName()).log(Level.SEVERE, null, ex);
        }
        return request;
    }

    private void SendRequest(Request request) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            out.writeUTF(request.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

}
