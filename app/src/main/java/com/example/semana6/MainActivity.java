package com.example.semana6;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button btnDown, btnUp, btnLeft, btnRight, btnMiddle;
    int xCo, yCo;

    private Socket socket;

    BufferedWriter writer;
    BufferedReader reader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnDown = findViewById(R.id.buttonDown);
        btnUp = findViewById(R.id.buttonUp);
        btnLeft = findViewById(R.id.buttonLeft);
        btnRight = findViewById(R.id.buttonRight);
        btnMiddle = findViewById(R.id.buttonMiddle);
        initClient();

        btnDown.setOnClickListener(
                (view) ->
                {
                    String y = "5";
                    String x = "0";
                    sendMessage(x, y);
                }
        );

        btnUp.setOnClickListener(
                (view) ->
                {
                    String y = "-5";
                    String x = "0";
                    sendMessage(x, y);
                }
        );

        btnLeft.setOnClickListener(
                (view) ->
                {
                    String y = "0";
                    String x = "-5";
                    sendMessage(x, y);
                }
        );

        btnRight.setOnClickListener(
                (view) ->
                {
                    String y = "0";
                    String x = "5";
                    sendMessage(x, y);
                }
        );

        btnMiddle.setOnClickListener(
                (view) ->
                {
                    int r = new Random().nextInt(255);
                    int g = new Random().nextInt(255);
                    int b = new Random().nextInt(255);

                    String rString = String.valueOf(r);
                    String gString = String.valueOf(g);
                    String bString = String.valueOf(b);

                    sendMessageColor(rString, bString, gString);
                }
        );
    }

    public void initClient()
    {
        new Thread(
                () ->
                {
                    try {
                        System.out.println("Connecting to server...");
                        socket = new Socket("192.168.1.9", 4000);
                        System.out.println("Established connection to server");

                        InputStream is = socket.getInputStream();
                        InputStreamReader isr = new InputStreamReader(is);
                        reader = new BufferedReader(isr);

                        OutputStream os = socket.getOutputStream();
                        OutputStreamWriter osw = new OutputStreamWriter(os);
                        writer = new BufferedWriter(osw);

                        while(true)
                        {
                            System.out.println("Awaiting message...");
                            String line = reader.readLine();
                            System.out.println("Received message: " + line);
                        }
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
        ).start();
    }

    public void sendMessage(String msg, String msg2)
    {
        new Thread(
                () ->
                {
                    try {
                        writer.write(msg + ":" + msg2 + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }).start();
    }

    public void sendMessageColor(String msg, String msg2, String msg3)
    {
        new Thread(
                () ->
                {
                    try {
                        writer.write(msg + ":" + msg2 + ":" + msg3 + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }).start();
    }

    public void moveVer(int dir)
    {
        try {
            if(dir == 1)
            {
                writer.write("up" + "\n");
                writer.flush();
            }
            if(dir == 0)
            {
                writer.write("down" + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void moveHor(int dir) {
        try {
            if (dir == 1) {
                writer.write("right" + "\n");
                writer.flush();
            }
            if (dir == 0) {
                writer.write("left" + "\n");
                writer.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}