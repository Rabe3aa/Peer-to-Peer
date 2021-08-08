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
public class RequestMan {


    public static String[] convertDataToArray(String data) {
        return data.split("\\|");
    }

    public static String ConvertArrayToData(String[] data) {
        String data_str = "";
        for (int i = 0; i < data.length; i++) {
            if (i == data.length - 1) {
                data_str += data[i];
            } else {
                data_str += data[i] + "|";
            }
        }
        return data_str;
    }

}
