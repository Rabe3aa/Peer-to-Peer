/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.util;

import java.util.ArrayList;

/**
 *
 * @author Amr
 */
public class Tracker {

    private int hashed_file;
    private ArrayList<Integer> user_ids;

    public Tracker(){
        user_ids = new ArrayList<>();
    }
    
    public void addUserId(int id){
        user_ids.add(id);
    }
    
    public int getHashed_file() {
        return hashed_file;
    }

    public void setHashed_file(int hashed_file) {
        this.hashed_file = hashed_file;
    }

    public ArrayList<Integer> getUser_ids() {
        return user_ids;
    }

}
