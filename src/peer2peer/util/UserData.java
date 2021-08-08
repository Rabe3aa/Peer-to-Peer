/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.util;

/**
 *
 * @author Amr
 */
public class UserData {

    private int id;
    private String password;

    
    
    public UserData(){
        
    }
    public UserData(int id) {
        this.id = id;
    }

   
    
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String[] ToArray(){
        String[] ar = new  String[2];
        ar[0] = id+"";
        ar[1] = password ;
        return ar ;
    }
    
}
