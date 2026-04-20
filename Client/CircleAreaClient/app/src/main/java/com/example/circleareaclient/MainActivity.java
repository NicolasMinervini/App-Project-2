package com.example.circleareaclient;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

///////////////////////////////////////////////////////////////////////////
//
// This app uses a server to compute the area of a circle with a given
// radius.  The Main Activity requests the hostname and port #
// of the server from the user.  After that, the "Communicate" Activity:
// connects to the server, requests the radius from the user, sends it to
// the server, receives and prints the result.
//
// Author: M. Halper
//
///////////////////////////////////////////////////////////////////////////

public class MainActivity extends AppCompatActivity {

    public final static String HOST_NAME = "com.example.circleareaclient.HOSTNAME";
    public final static String PORT = "com.example.circleareaclient.PORT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Allow for network access on the main thread

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

    } // end onCreate

    public void connectServer(View view)
    {
        EditText et1;
        EditText et2;
        String hostname;
        int port;
        Intent intent = new Intent(this, CommunicateActivity.class);

        // get the hostname and the port

        et1 = (EditText) findViewById(R.id.edit_host);
        hostname = et1.getText().toString();

        et2 = (EditText) findViewById(R.id.edit_port);
        port = Integer.parseInt(et2.getText().toString());

        // start the activity for communicating with the client.  Pass it
        // the hostname and port in the intent

        intent.putExtra(HOST_NAME, hostname);
        intent.putExtra(PORT, port);

        startActivity(intent);

    } // end connectServer

} // end MainActivity
