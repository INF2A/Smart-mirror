package main.java;

import java.sql.*;
import java.util.ArrayList;

class DBConnect {

    Connection connection;
    ArrayList<ArrayList<String>> feedback = new ArrayList<ArrayList<String>>();
    ArrayList<String> feed = null;
    static int id;// static user id
    static String pw = "slimme spiegel";
    static String username = "Slimme Spiegel";

    DBConnect() throws SQLException {
          connection= DriverManager.getConnection("jdbc:mariadb://127.0.0.1:3306/cluster_slimme_spiegel?user=root&password=qwer");
          id = 1;
    }
    public static void main(String args[]) throws SQLException {
        DBConnect connect = new DBConnect();
        //connect.selectQuery("SELECT * FROM time_zones");// select alles van tijdzones
        //connect.selectQuery("SELECT time_zone FROM time_zones INNER JOIN time ON time_zones.ID = time.Timezone_ID WHERE time.User_ID = " + id);// select tijdzone van de gebruiker
        //connect.selectQuery("SELECT Name FROM weather_pref INNER JOIN weather ON weather_pref.ID = weather.Weather_pref_ID WHERE weather.User_ID =" + id);//select metric van weer pref van de gebruiker
        //connect.selectQuery("SELECT location FROM weather WHERE User_ID =" +id);//select de locatie van het weer
        //connect.selectQuery("SELECT Name FROM news_pref_item INNER JOIN news_pref ON news_pref_item.ID = news_pref.News_pref_item_ID WHERE news_pref.User_ID= "+id);// selecteer de news pref items van de gebruiker
        //connect.selectQuery("SELECT Name FROM channel INNER JOIN radio_fav ON channel.ID = radio_fav.Channel_ID WHERE radio_fav.User_ID =" +id);//select de favorite radio channels van de user
        //connect.selectQuery("SELECT * FROM wifi_settings WHERE User_ID ="+id);//select wifi instellingen
        //connect.selectQuery("select * from user where Password='"+ pw +"' AND Username='"+username+"'");//gebruik dit om in te loggen. check of de user en pass correct zijn.
        connect.deleteQuery("DELETE FROM `news_pref` WHERE `User_ID`= "+ id);//delete news pref items van de user


    }

    private void selectQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
        ResultSet rs = stmt.getResultSet();
        ResultSetMetaData rsm = rs.getMetaData();
        feed = new ArrayList<String>();
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
            stmt.close();
            connection.close();
        }
    }
    private void deleteQuery(String query) throws SQLException {
        Statement stmt = connection.createStatement();
        stmt.execute(query);
    }

}
