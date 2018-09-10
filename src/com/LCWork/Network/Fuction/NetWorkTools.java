package com.LCWork.Network.Fuction;

import com.LCWork.Network.Client.Client;
import com.LCWork.Network.Server.Server;
import com.TeamWork.Data.Global.Data;

/**
 * 用来调用网络功能的类
 * @author A
 * @version 1.0
 * @since 1.0
 */

public class NetWorkTools implements Data {

    Client client = new Client();
    Server server = new Server();

    public NetWorkTools(){

    }
    public NetWorkTools(int i){
        if (i == ServerID){
            server.init();
            Thread sth = new Thread(server);
            sth.start();
        }else if (i == ClientID){

        }
    }
}
