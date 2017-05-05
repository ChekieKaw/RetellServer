package edu.sysu.reteller;

import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

public class RelayService {

    public static void main(String s[]) {


        QuickServer hearServer=new QuickServer("edu.sysu.reteller.HearServiceHandler");
        hearServer.setClientBinaryHandler("edu.sysu.reteller.HearServiceHandler");
        hearServer.setPort(4123);
        hearServer.setName("HearServer");

        QuickServer retellServer=new QuickServer("edu.sysu.reteller.RetellServiceHandler");
        retellServer.setClientBinaryHandler("edu.sysu.reteller.RetellServiceHandler");
        retellServer.setPort(4124);
        retellServer.setName("Retell Server");

        try {

            hearServer.startService();
            retellServer.startServer();
        } catch(AppException e){

            System.err.println("Error in server : "+e);

        }catch (Exception e){
            e.getStackTrace();
        }

    }

}