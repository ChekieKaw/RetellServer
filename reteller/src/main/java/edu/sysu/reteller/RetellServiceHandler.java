package edu.sysu.reteller;

import org.quickserver.net.server.*;
import sun.misc.BASE64Encoder;

import java.io.IOException;
import java.sql.*;

/**
 * Created by sunny on 17-4-26.
 */
public class RetellServiceHandler implements ClientEventHandler,ClientCommandHandler,ClientBinaryHandler {
    public static ClientHandler retellHandler;

    public void sendBinary(byte[] data){
        try{
            retellHandler.sendClientBinary(data);
        }catch(IOException e){
            System.out.println("Error relaying data!");
        }

    }

    public void gotConnected(ClientHandler handler) throws  IOException {
        retellHandler=handler;
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
    public void handleBinary(ClientHandler handler, byte[] data) {
        System.out.println("4124");
        Utility.printHexString(data);
        BASE64Encoder mEncoder= new BASE64Encoder();
        String dataEncoded=mEncoder.encode(data);
        try {
            HearServiceHandler.hearHandler.sendClientBinary(data);

        }catch(Exception e){
            System.out.println("Failed to relay message to port 4123, caching");
            try {
                Statement stmt = SQLUtil.getConnection().createStatement();
                ResultSet rsTables = SQLUtil.getConnection().getMetaData().getTables(null, null, "retell", null);
                if(!rsTables.next()){
                    System.out.println("Creating table retell");
                    stmt.executeUpdate("create table retell(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  data TEXT NOT NULL, time TIMESTAMP);");
                }
                stmt.executeUpdate("INSERT INTO retell VALUES (NULL,'"+dataEncoded+"',CURRENT_TIMESTAMP )");
                SQLUtil.getConnection().commit();
            }catch (SQLException ex){
                System.out.println("Error when accessing database");
                ex.printStackTrace();
            }
        }

    }
}
