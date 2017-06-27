package com.smartmirror.sys;

import java.sql.*;
import java.util.ArrayList;

/**
 * Created by Erwin on 6/12/2017.
 */
public class DB {
    static int id;// static user id
    static String pw = "slimme spiegel";
    static String username = "Slimme Spiegel";


    public static ArrayList<ArrayList<String>> feedback = new ArrayList<ArrayList<String>>();
    public static ArrayList<String> feed = null;


    private static Connection connection;

    public static Connection getConnection() {
        try {
            if(connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/cluster_slimme_spiegel?user=root&password=qwer");
                id = 1;
                return connection;
            }
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connection;
    }

    private DB() {}


    public void query_test() throws SQLException {
     //   DB.getConnection();

        //connect.selectQuery("SELECT * FROM news_pref_item");// select alles van news_pref_item
        //connect.selectQuery("SELECT * FROM time_zones");// select alles van tijdzones
        //connect.selectQuery("SELECT * FROM channel");// select alles van channel
        //connect.selectQuery("SELECT * FROM time_zones where time_zone LIKE '% input %'" );// select alles van channel

        //connect.selectQuery("SELECT * FROM wifi_settings WHERE User_ID =" + id);//select wifi informatie op basis van user id
        //connect.selectQuery("SELECT time_zone FROM time_zones INNER JOIN time ON time_zones.ID = time.Timezone_ID WHERE time.User_ID = " + id);// select tijdzone van de gebruiker
        //connect.selectQuery("SELECT Name FROM weather_pref INNER JOIN weather ON weather_pref.ID = weather.Weather_pref_ID WHERE weather.User_ID =" + id);//select metric van weer pref van de gebruiker
        //connect.selectQuery("SELECT location FROM weather WHERE User_ID =" +id);//select de locatie van het weer
        //connect.selectQuery("SELECT Name FROM news_pref_item INNER JOIN news_pref ON news_pref_item.ID = news_pref.News_pref_item_ID WHERE news_pref.User_ID= "+id);// selecteer de news pref items van de gebruiker
        //connect.selectQuery("SELECT Name FROM channel INNER JOIN radio_fav ON channel.ID = radio_fav.Channel_ID WHERE radio_fav.User_ID =" +id);//select de favorite radio channels van de user
        //connect.selectQuery("SELECT * FROM wifi_settings WHERE User_ID ="+id);//select wifi instellingen
        //connect.selectQuery("select * from user where Password='"+ pw +"' AND Username='"+username+"'");//gebruik dit om in te loggen. check of de user en pass correct zijn.
        //DB.deleteQuery("DELETE FROM `news_pref` WHERE `User_ID`= "+ id);//delete news pref items van de user
        //query("INSERT INTO `news_pref`(`User_ID`, `News_pref_item_ID`) VALUES ("+id  +" ,item"); //insert nieuwe news pref item
        //query("UPDATE radio_fav c set User_ID = "+ id +", Channel_ID =  channel");//insert radio favs
        //query("UPDATE time set User_ID ="+  id + ", Timezone_ID = timezoneID");//insert nieuwe timezone
        //query("UPDATE weather SET User_ID =" +id+ ", location = ' location_hier', Weather_pref_ID = pref_id_hier");//insert nieuwe weather location
        //query("UPDATE wifi_settings set User_ID = "+ id +", ssid = 'ssid hier', password = 'passwoord hier'");//set nieuwe wifi settings




  }

    public static void selectQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData rsm = rs.getMetaData();
        feed = new ArrayList<>();
        for (int y = 1; y < rsm.getColumnCount(); y++) {
            feed.add(rsm.getColumnName(y));
        }
        feedback.add(feed);
        while (rs.next()) {
            feed = new ArrayList<String>();
            for (int i = 1; i <= rsm.getColumnCount(); i++) {
                feed.add(rs.getString(i));
                System.out.println(rs.getString(i));
            }
            feedback.add(feed);
        }
        stmt.close();
        connection.close();
    }

    public static void query(String query) throws SQLException {
        getConnection();
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        connection.close();
    }
}
