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
public class Hash {

  
    public static int incryKey(int file_number , int user_id ) {
        return (file_number + user_id) * 5;
    }

    public static int decryKey(int hashed_file_number , int user_id) {
        return (hashed_file_number / 5) - user_id;
    }
}
