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
public class Request {
    
    private String service ;
    
    private int status_code ;
    
    private  String data ;

    public String getService() {
        return service;
    }

    public void setService(String message) {
        this.service = message;
    }

    public int getStatus_code() {
        return status_code;
    }

    public void setStatus_code(int status_code) {
        this.status_code = status_code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return  service + "-" + status_code + "-" + data;
    }
    
    
    public void ConvertToRequest(String data_request) {
        String[] split = data_request.split("-");
        setService(split[0]);
        setStatus_code(Integer.parseInt(split[1]));
        setData(split[2]);
    }
}
