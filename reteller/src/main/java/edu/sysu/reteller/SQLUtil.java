package edu.sysu.reteller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Created by sunny on 17-4-27.
 */
public class SQLUtil {
    private static Connection sqlConn;
    public static void connect(){
        try {
            Class.forName("org.sqlite.JDBC");
            sqlConn = DriverManager.getConnection("jdbc:sqlite:cache.db", null, null);
            sqlConn.setAutoCommit(false);
            Statement stmt = sqlConn.createStatement();
            ResultSet rsTables = sqlConn.getMetaData().getTables(null, null, "hear", null);
            if(!rsTables.next()){
                System.out.println("Creating table hear");
                stmt.executeUpdate("create table hear(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  data TEXT NOT NULL, time TIMESTAMP);");
            }
            rsTables = sqlConn.getMetaData().getTables(null, null, "retell", null);
            if(!rsTables.next()){
                System.out.println("Creating table retell");
                stmt.executeUpdate("create table retell(id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,  data TEXT NOT NULL, time TIMESTAMP);");
            }
            sqlConn.commit();
        }catch(Exception e){
            System.out.println("Database connecting error!");
        }
    }
    public static void disconnect(){
        try {
            sqlConn.close();
        }catch (Exception e){
            System.out.println("Database disconnect error!");
        }
    }
    public static Connection getConnection(){
        return sqlConn;
    }

}
