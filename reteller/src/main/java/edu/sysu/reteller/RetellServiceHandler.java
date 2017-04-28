package edu.sysu.reteller;

import org.quickserver.net.server.*;

import java.io.IOException;
import java.net.SocketTimeoutException;

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
    public void handleBinary(ClientHandler handler, byte[] data) throws SocketTimeoutException, IOException {
        System.out.println("4124");
        Utility.printHexString(data);
        //BASE64Encoder mEncoder= new BASE64Encoder();
        HearServiceHandler.hearHandler.sendClientBinary(data);

    }
}
