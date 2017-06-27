package com.smartmirror.sys.applications;

import com.smartmirror.sys.applications.widgets.AudioClient;
import com.smartmirror.sys.applications.widgets.RadioWidget;
import com.smartmirror.sys.view.AbstractSystemApplication;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.player.advanced.AdvancedPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.*;

/**
 * Created by basva on 20-6-2017.
 */
public class Radio extends AbstractSystemApplication {

    private final static int SOCKET_PORT = 8093;//int to store the port of the client
    public final static String SERVER = "192.168.1.1";

    private static AdvancedPlayer advancedPlayer;//Advanced player used to play the audio stream sent by the AudioServer
    private static Socket sock = null;//socket used to connect with the server

    private boolean serverConnection = false;

    JButton play;
    JButton stop;


    @Override
    protected void setup() {
        SYSTEM_Widget = new RadioWidget();
        setSYSTEM_Icon("img/radio-icon.png");
        addWidgetToSystemWindow = true;

        play = new JButton("Play");
        stop = new JButton("Stop");

        play.addActionListener(e -> {
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
        });

        stop.addActionListener(e -> {
            stop();
        });

        SYSTEM_Screen.setLayout(new BorderLayout());

        JPanel content = new JPanel();
        content.setLayout(new BoxLayout(content, BoxLayout.Y_AXIS));

        content.add(play);
        content.add(stop);

        content.setBackground(Color.BLACK);

        focusManager.addComponent(play);
        focusManager.addComponent(stop);

        SYSTEM_Screen.add(content, BorderLayout.CENTER);
    }

    @Override
    public void init() {


    }

    public static void play(BufferedInputStream bis) throws JavaLayerException, InterruptedException {
        advancedPlayer = new AdvancedPlayer(bis);//create new advanced player with the bufferd inputstream as argument
        advancedPlayer.play();//call the play method in order to play the audio stream
    }

    public void stop() {
        if(sock != null)
        {
            try {
                sock.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBackButton() {
        stop();
        SYSTEM_closeScreen();
    }
}
