package edu.sysu.reteller;

/**
 * Created by sunny on 17-4-26.
 */
// EchoCommandHandler.java

import org.quickserver.net.server.*;

import java.io.IOException;
import java.net.SocketTimeoutException;



public class HearServiceHandler implements ClientEventHandler,ClientCommandHandler,ClientBinaryHandler {

    public static ClientHandler hearHandler;

    public void sendBinary(byte[] data){
        try{
            hearHandler.sendClientBinary(data);
        }catch(IOException e){
            System.out.println("Error relaying data!");
        }

    }

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
    public void handleBinary(ClientHandler handler, byte[] data) throws SocketTimeoutException, IOException {
        System.out.println("4123:");
        Utility.printHexString(data);
        //BASE64Encoder mEncoder= new BASE64Encoder();
        RetellServiceHandler.retellHandler.sendClientBinary(data);

    }

}