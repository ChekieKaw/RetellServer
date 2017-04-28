package edu.sysu.reteller;

/**
 * Created by sunny on 17-4-26.
 */
// EchoCommandHandler.java

import org.quickserver.net.server.*;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.sql.*;


public class HearServiceHandler implements ClientEventHandler,ClientCommandHandler,ClientBinaryHandler {

    public static ClientHandler hearHandler;


    public void gotConnected(ClientHandler handler) throws  IOException {
        hearHandler=handler;
        handler.setDataMode(DataMode.BINARY, DataType.IN);
        handler.setDataMode(DataMode.BINARY,DataType.OUT);

    }

    public void lostConnection(ClientHandler handler)

            throws IOException {

        handler.sendSystemMsg("Connection lost : " +

                handler.getSocket().getInetAddress());

    }

    public void closingConnection(ClientHandler handler)

            throws IOException {

        handler.sendSystemMsg("Closing connection : " +

                handler.getSocket().getInetAddress());

    }


    public void handleCommand(ClientHandler handler, String command){
        //no need to implement it
    }
    public void handleBinary(ClientHandler handler, byte[] data){
        System.out.println("4123:");
        Utility.printHexString(data);
        BASE64Encoder mEncoder= new BASE64Encoder();
        String dataEncoded=mEncoder.encode(data);
        try {
            RetellServiceHandler.retellHandler.sendClientBinary(data);
        }catch(Exception e){
            System.out.println("Failed to relay message to port 4124, caching");
            try {
                Statement stmt = SQLUtil.getConnection().createStatement();
                ResultSet rsTables = SQLUtil.getConnection().getMetaData().getTables(null, null, "hear", null);
                if(!rsTables.next()){
                    System.out.println("Creating table hear");
                    stmt.executeUpdate("create table hear(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  data TEXT NOT NULL, time TIMESTAMP);");
                }
                stmt.executeUpdate("INSERT INTO hear VALUES (NULL,'"+dataEncoded+"',CURRENT_TIMESTAMP )");
                SQLUtil.getConnection().commit();

            }catch (SQLException ex){
                System.out.println("Error when connecting to database");
                e.printStackTrace();
            }
        }
    }

}