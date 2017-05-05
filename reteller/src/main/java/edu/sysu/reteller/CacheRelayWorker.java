package edu.sysu.reteller;

import sun.misc.BASE64Decoder;

import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by sunny on 17-4-27.
 */
public class CacheRelayWorker implements Runnable {

    public void run(){
        while(true){
            try{


                Statement stmt = SQLUtil.getConnection().createStatement();

                ResultSet hearSet = stmt.executeQuery("SELECT * FROM hear ORDER BY time");
                BASE64Decoder mDecoder=new BASE64Decoder();
                while (hearSet.next()){
                    try {
                        RetellServiceHandler.retellHandler.sendClientBinary(mDecoder.decodeBuffer(hearSet.getString("data")));
                        stmt.executeUpdate("DELETE FROM hear WHERE id=" + String.valueOf(hearSet.getInt("id")) + ";");
                        SQLUtil.getConnection().commit();
                    }catch(Exception e){
                        //do nothing
                    }
                }
                ResultSet retellSet = stmt.executeQuery("SELECT * FROM retell ORDER BY time");
                while (retellSet.next()){
                    try {
                        HearServiceHandler.hearHandler.sendClientBinary(mDecoder.decodeBuffer(retellSet.getString("data")));
                        stmt.executeUpdate("DELETE FROM retell WHERE id=" + String.valueOf(retellSet.getInt("id")) + ";");
                        SQLUtil.getConnection().commit();
                    }catch(Exception e){
                        //do nothing as well
                    }
                }
                Thread.sleep(5);

            }catch (Exception e){
                //System.out.print("Cache");
                //e.printStackTrace();
            }
        }
    }
}
