/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.Server;

import java.util.ArrayList;
import peer2peer.util.RequestMan;
import peer2peer.util.UserData;

/**
 *
 * @author Amr
 */
public class Auth {

    private ArrayList<UserData> user_list;

    public Auth() {
        user_list = new ArrayList<>();
    }

    public boolean SignUp(String data) {
        // id|password
        String[] convertDataToArray = RequestMan.convertDataToArray(data);
        UserData user = new UserData();
        user.setId(Integer.parseInt(convertDataToArray[0]));
        user.setPassword(convertDataToArray[1]);
        if (findUser(user)) {
            return false;
        }
        user_list.add(user);
        return true;
    }

    private boolean findUser(UserData user) {
        for (int i = 0; i < user_list.size(); i++) {
            if (user.getId() == user_list.get(i).getId()) {
                return true;
            }
        }
        return false;
    }

    public boolean login(String data) {
        // id|password
        String[] convertDataToArray = RequestMan.convertDataToArray(data);
        for (int i = 0; i < user_list.size(); i++) {
            if (user_list.get(i).getId() == Integer.parseInt(convertDataToArray[0])
                    && user_list.get(i).getPassword().equals(convertDataToArray[1])) {
                return true;

            }
        }
        return false;

    }

}
