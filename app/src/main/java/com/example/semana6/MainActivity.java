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
    }

    public void initClient()
    {
        new Thread(
                () ->
                {
                    try {
                        System.out.println("Connecting to server...");
                        socket = new Socket("192.168.1.9", 2000);
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

    public void sendMessage(String msg)
    {
        new Thread(
                () ->
                {
                    try {
                        writer.write(msg + "\n");
                        writer.flush();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                }).start();
    }
}