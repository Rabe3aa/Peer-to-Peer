/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.Server;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import peer2peer.util.Tracker;

/**
 *
 * @author Amr
 */
public class ProcessTracker {

    private ArrayList<Tracker> data;

    public ProcessTracker() {
        data = new ArrayList<>();
        init();
    }

    public void init() {
        try {
            File p2p = new File("p2p");
            if (p2p.exists()) {
                p2p.delete();
            }
            p2p.mkdir();
            File f = new File("p2p/tracker.txt");
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
        } catch (IOException ex) {
            Logger.getLogger(ProcessTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void AddUserToFile(int hashed_filename, int user_id) {
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i).getHashed_file() == hashed_filename) {
                data.get(i).addUserId(user_id);
                build_tracker();
                return;
            }
        }
        Tracker tracker = new Tracker();
        tracker.addUserId(user_id);
        tracker.setHashed_file(hashed_filename);
        data.add(tracker);
        build_tracker();
    }

    private PrintWriter openFileTracker() {
        FileWriter fw = null;
        PrintWriter out = null;
        try {
            File f = new File("p2p/tracker.txt");
            if (f.exists()) {
                f.delete();
            }
            fw = new FileWriter("p2p/tracker.txt", true);
            BufferedWriter bw = new BufferedWriter(fw);
            out = new PrintWriter(bw);

        } catch (IOException ex) {
            Logger.getLogger(ProcessTracker.class.getName()).log(Level.SEVERE, null, ex);
        }
        return out;
    }

    public void build_tracker() {
        PrintWriter file = openFileTracker();
        for (int i = 0; i < data.size(); i++) {
            file.append(data.get(i).getHashed_file() + " ");
            ArrayList<Integer> user_ids = data.get(i).getUser_ids();
            for (int j = 0; j < user_ids.size(); j++) {
                if (j == user_ids.size() - 1) {
                    file.append(user_ids.get(j) + "");
                } else {
                    file.append(user_ids.get(j) + "-");
                }
            }
            file.append("\n");
        }
        file.close();
    }
}
