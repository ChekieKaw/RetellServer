package edu.sysu.reteller;

import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

public class RelayService {
    public static Thread mWorker = new Thread(new CacheRelayWorker());
    public static void main(String s[]) {


        QuickServer hearServer=new QuickServer("edu.sysu.reteller.HearServiceHandler");
        hearServer.setClientBinaryHandler("edu.sysu.reteller.HearServiceHandler");
        hearServer.setPort(4123);
        hearServer.setName("HearServer");
        hearServer.setTimeout(3600000);
        QuickServer retellServer=new QuickServer("edu.sysu.reteller.RetellServiceHandler");
        retellServer.setClientBinaryHandler("edu.sysu.reteller.RetellServiceHandler");
        retellServer.setPort(4124);
        retellServer.setName("Retell Server");
        retellServer.setTimeout(3600000);

        try {
            SQLUtil.connect();
            mWorker.start();
            hearServer.startServer();
            retellServer.startServer();
        } catch(AppException e){

            System.err.println("Error in server : "+e);

        }catch (Exception e){
            e.getStackTrace();
        }

    }

}