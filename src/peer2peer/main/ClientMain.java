/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.main;

import peer2peer.Client.Client;
import peer2peer.util.Host;

/**
 *
 * @author Amr
 */
public class ClientMain {
 
    
    public static void main(String[] args) {
        Client client = new Client(Host.HOST , Host.PORT);
        client.Start();
    }
}
