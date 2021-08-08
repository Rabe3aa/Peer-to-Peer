/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package peer2peer.main;

import peer2peer.Server.Server;
import peer2peer.util.Host;

/**
 *
 * @author Amr
 */
public class ServerMain {
    
    public static void main(String[] args) {
        Server server = new Server(Host.HOST , Host.PORT);
        server.Start();
    }
}
