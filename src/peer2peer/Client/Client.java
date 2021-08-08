/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.Client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import peer2peer.Server.UserThread;
import peer2peer.util.Hash;
import peer2peer.util.Request;
import peer2peer.util.RequestMan;
import peer2peer.util.Services;
import peer2peer.util.Status;
import peer2peer.util.UserData;

/**
 *
 * @author Amr
 */
public class Client {

    private String ip;
    private int port;
    private Socket socket;
    private Scanner cin;
    private int cur_user_id;

    public Client(String ip, int port) {
        this.ip = ip;
        this.port = port;
        cin = new Scanner(System.in);
    }

    public void Start() {
        try {
            socket = new Socket(ip, port);
            AuthMenu();
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AuthMenu() {
        while (true) {
            System.out.println("1- Signup");
            System.out.println("2- Login");
            System.out.println("3- Exit");
            System.out.print("Your choice: ");
            int choice = cin.nextInt();
            if (choice == 1) {
                System.out.println("--- sign up ---");
                SignUp();
            } else if (choice == 2) {
                System.out.println("--- Login ---");
                Login();
            } else {
                System.exit(0);
            }

        }
    }

    public void SignUp() {
        System.out.print("Your Id : ");
        int id = cin.nextInt();
        System.out.print("Your pasword : ");
        String password = cin.next();
        UserData user = new UserData();
        user.setId(id);
        user.setPassword(password);
        // init request 
        Request r = new Request();
        r.setService(Services.SIGN_UP_CLIENT);
        r.setStatus_code(200);
        r.setData(RequestMan.ConvertArrayToData(user.ToArray()));
        //send request to server
        SendRequest(r);
        // read request from server
        Request request = ReadRequest();
        if (request.getStatus_code() != Status.OK) {
            System.out.println("This account is already exist");
        } else {
            System.out.println("Congratulation , You have an account");
        }

    }

    public void Login() {
        System.out.print("Your Id : ");
        int id = cin.nextInt();
        System.out.print("Your pasword : ");
        String password = cin.next();
        UserData user = new UserData();
        user.setId(id);
        user.setPassword(password);
        // init request 
        Request r = new Request();
        r.setService(Services.SIGN_IN_CLIENT);
        r.setStatus_code(200);
        r.setData(RequestMan.ConvertArrayToData(user.ToArray()));
        //send request to server
        SendRequest(r);
        // read request from server
        Request request = ReadRequest();
        if (request.getStatus_code() != Status.OK) {
            System.out.println("Your id or password isn't correct");
        } else {
            cur_user_id = user.getId();
            FilesMenu(user);
        }
    }

    public void FilesMenu(UserData user) {
        while (true) {
            System.out.println("1- Add file");
            System.out.println("2- Download file");
            System.out.println("3- Update file");
            System.out.println("4- Exit");
            System.out.print("Your choice: ");
            int choice = cin.nextInt();
            if (choice == 1) {
                System.out.println("--- add file ---");
                System.out.print("input your file content : ");
                String line = cin.next();
                String ar[] = addFile(user, line);
                String[] ret = new String[3];
                ret[0] = ar[0];
                ret[1] = ar[1];
                ret[2] = ar[2];
                SendFileRequest(ret);
            } else if (choice == 2) {
                System.out.println("--- download file ---");
                DownloadFile();
            } else if (choice == 3) {
                System.out.println("--- update file ---");
                UpdateFile();
            } else {
                break;
            }

        }

    }

    private void UpdateFile() {

        System.out.print("Your file id : ");
        String file_number = cin.next();
        System.out.print("input your file content : ");
        String content = cin.next();
        String path = "p2p/peer" + cur_user_id + "/file" + file_number+".txt";
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(content);
            myWriter.close();
            System.out.println("The file is updated successfully.");
        } catch (IOException ex) {
            System.out.println("this file is not exist");
        }
    }

    public void DownloadFile() {
        System.out.print("Your user id : ");
        int from_user_id = cin.nextInt();
        System.out.print("Your file id : ");
        String file_number = cin.next();

        String req[] = new String[3];
        req[0] = from_user_id + "";
        req[1] = cur_user_id + "";
        req[2] = file_number + "";
        // init request 
        Request r = new Request();
        r.setService(Services.DOWNLOADFILE_CLIENT);
        r.setStatus_code(200);
        r.setData(RequestMan.ConvertArrayToData(req));
        //send request to server
        SendRequest(r);
        // read request from server
        Request request = ReadRequest();
        if (request.getStatus_code() != Status.OK) {
            System.out.println("This File you request isn't exist");
        } else {
            String[] content = RequestMan.convertDataToArray(request.getData());
            String[] ret = addFile(new UserData(cur_user_id), content[0]);
            printDataTofile(ret[3], ret[2]);
        }
    }

    private void printDataTofile(String path, String content) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void SendFileRequest(String ar[]) {
        Request request = new Request();
        request.setStatus_code(Status.OK);
        request.setData(RequestMan.ConvertArrayToData(ar));
        request.setService(Services.ADDFILE_CLIENT);
        SendRequest(request);
    }

    public String[] addFile(UserData user, String content) {
        String[] ret = new String[4];
        int counter = 1;
        String path = null;
        try {
            String parent_path = "p2p";
            File parent = new File(parent_path);
            if (!parent.exists()) {
                parent.mkdir();
            }
            String sub_parent_path = parent_path + "/peer" + user.getId();
            File sub = new File(sub_parent_path);
            if (!sub.exists()) {
                sub.mkdir();
            }

            while (true) {
                path = sub_parent_path + "/file" + counter + ".txt";
                File file = new File(path);
                if (file.exists()) {
                    counter++;
                } else {
                    file.createNewFile();
                    break;
                }
            }
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(content);
            myWriter.close();

            System.out.println("You have created a file");
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
        ret[0] = Hash.incryKey(counter, user.getId()) + "";
        ret[1] = user.getId() + "";
        ret[2] = content;
        ret[3] = path;
        return ret;
    }

    public void SendRequest(Request request) {
        DataOutputStream out = null;
        try {
            out = new DataOutputStream(socket.getOutputStream());
            Request re = new Request();
            re.setData(request.getData());
            re.setService(request.getService());
            re.setStatus_code(request.getStatus_code());
            out.writeUTF(re.toString());
        } catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
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

}
