package com.smartmirror.sys.applications.widgets;

import com.smartmirror.core.view.AbstractWidget;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;
import org.json.simple.JSONObject;

import javax.sound.sampled.*;
import java.io.*;
import java.net.Socket;

public class AudioClient extends AbstractWidget {

    private final static int SOCKET_PORT = 8093;//int to store the port of the client
    public final static String SERVER = "192.168.1.1";
//    private final static String SERVER = "127.0.0.1";//string to store the server adress, in this calse local host

    private static AdvancedPlayer advancedPlayer;//Advanced player used to play the audio stream sent by the AudioServer
    private static Socket sock = null;//socket used to connect with the server

    public AudioClient() {
        location = location.CENTER_LEFT;

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        sock = new Socket(SERVER, SOCKET_PORT);//create a new socket with localhost and port 8093 as arguments
                        System.out.println("Connecting...");
                        InputStream is = sock.getInputStream();//get the audio stream sent by the Audio server
                        BufferedInputStream bis = new BufferedInputStream(is);//convert the inputstream to a buffered input stream so the advanced player can use it to play the stream
                        while (true) {//create a loop to play the audio stream
                            play(bis);//play the audio stream
                        }
                    } catch (Exception e) {
                        e.printStackTrace();//connection failed
                    }
                }
            }
        }).start();
    }

    /***
     * main method, connects with the Audio server and plays the audio stream with the advanced player
     * @param args
     * @throws IOException
     * @throws UnsupportedAudioFileException
     * @throws LineUnavailableException
     * @throws InterruptedException
     */
//    public static void main (String [] args ) throws IOException, UnsupportedAudioFileException, LineUnavailableException, InterruptedException {
//            try {
//                sock = new Socket(SERVER, SOCKET_PORT);//create a new socket with localhost and port 8093 as arguments
//                System.out.println("Connecting...");
//                InputStream is = sock.getInputStream();//get the audio stream sent by the Audio server
//                BufferedInputStream bis = new BufferedInputStream(is);//convert the inputstream to a buffered input stream so the advanced player can use it to play the stream
//                while (true) {//create a loop to play the audio stream
//                    play(bis);//play the audio stream
//                }
//            } catch (Exception e) {
//                e.printStackTrace();//connection failed
//            }
//    }

    public static void play(BufferedInputStream bis) throws JavaLayerException, InterruptedException {
        advancedPlayer = new AdvancedPlayer(bis);//create new advanced player with the bufferd inputstream as argument
        advancedPlayer.play();//call the play method in order to play the audio stream
    }

    @Override
    public void update() {

    }

    @Override
    public JSONObject requestJson() {
        return null;
    }
}
