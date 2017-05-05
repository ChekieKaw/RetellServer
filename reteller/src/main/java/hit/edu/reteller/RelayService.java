package hit.edu.reteller;

import org.quickserver.net.AppException;
import org.quickserver.net.server.QuickServer;

public class RelayService {

    public static void main(String s[]) {


        QuickServer hearServer=new QuickServer("hit.edu.reteller.HearServiceHandler");
        hearServer.setClientBinaryHandler("hit.edu.reteller.HearServiceHandler");
        hearServer.setPort(4123);
        hearServer.setName("hit.edu.reteller.HearServer");
        hearServer.setTimeout(3600000);
        QuickServer retellServer=new QuickServer("hit.edu.reteller.RetellServiceHandler");
        retellServer.setClientBinaryHandler("hit.edu.reteller.RetellServiceHandler");
        retellServer.setPort(4124);
        retellServer.setName("Retell Server");
        retellServer.setTimeout(3600000);

        try {
            SQLUtil.connect();
            hearServer.startServer();
            retellServer.startServer();
        } catch(AppException e){

            System.err.println("Error in server : "+e);

        }catch (Exception e){
            e.getStackTrace();
        }

    }

}