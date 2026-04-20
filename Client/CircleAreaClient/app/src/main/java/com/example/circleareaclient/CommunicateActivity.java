package com.example.circleareaclient;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

public class CommunicateActivity extends AppCompatActivity {

    private Socket socket = null;
    private PrintWriter out = null;
    private Scanner in = null;
    private TextView tv;

    // In onCreate, connect to the server, and then wait for the
    // user to input the radius and press the button

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_communicate);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        int port;
        String hostname;

        // Get the hostname from the intent

        Intent intent = getIntent();
        hostname = intent.getStringExtra(MainActivity.HOST_NAME);

        // Get the port from the intent.  Default port is 4000

        port = intent.getIntExtra(MainActivity.PORT, 4000);

        // get a handle on the textview for displaying the area

        tv = (TextView) findViewById(R.id.text_answer);

        // Try to open the connection to the server

        try
        {
            socket = new Socket(hostname, port);

            out = new PrintWriter(socket.getOutputStream(), true);
            in = new Scanner(new InputStreamReader(socket.getInputStream()));

            tv.setText("Connected to server.");
        }
        catch(IOException e)  // socket problems
        {
            tv.setText("Problem: " + e.toString());
            socket = null;
        }

    } // end onCreate

    // send the radius to the server, and receive the result (area of the
    // circle) in return

    public void sendOperation(View view)
    {
        EditText et;
        EditText et2;
        EditText et3;

        String input;

        String result;

        // are we connected?

        if(socket == null)
        {
            tv.setText("Not connected.");
        }
        else
        {
            // get information to send to the server

            et = (EditText) findViewById(R.id.operation_type);
            input = et.getText().toString().replaceAll(",", "");

            et2 = (EditText) findViewById(R.id.number_one);
            input += "," + et2.getText().toString().replaceAll(",", "");

            et3 = (EditText) findViewById(R.id.number_two);
            input += "," + et3.getText().toString().replaceAll(",", "");

            try
            {
                // send the data to the server

                out.println(input);

                // get result

                result = in.next();

                // display the result

                tv.setText(result);

                // close connection to server

                out.close();
                in.close();
                socket.close();

                // indicate that we're no longer connected

                socket = null;

            }
            catch (IOException e)  // socket problems
            {
                tv.setText("Problem: " + e.toString());
            }

        }

    } // end sendRadius

} // end CommunicateActivity
