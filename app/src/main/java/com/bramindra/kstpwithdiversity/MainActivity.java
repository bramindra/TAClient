package com.bramindra.kstpwithdiversity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextAddress;

    public static MyClientTask myClientTask;
    public static String globalPreference = "com.bramindra.kstpwithdiversity";
    public static Socket socket;
    public static InputStream in;
    public static PrintWriter out;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextAddress = findViewById(R.id.ipAddressinput);
        Button buttonConnect = findViewById(R.id.buttonConnect);

        buttonConnect.setOnClickListener(buttonConnectOnClickListener);
    }

    View.OnClickListener buttonConnectOnClickListener =
        new View.OnClickListener(){

            @Override
            public void onClick(View arg0) {
                myClientTask = new MyClientTask(
                        editTextAddress.getText().toString(),
                        8888);
                myClientTask.execute();
            }
        };

//    @Override
//    public void onStop(){
//        super.onStop();
//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    out.write("DisconnectedstopReadBuffer");
//                    out.flush();
//                }
//            }).start();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        myClientTask.cancel(true);
//        Toast.makeText(getApplicationContext(), "Disconnected from server", Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void onDestroy(){
//        super.onDestroy();
//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    out.write("DisconnectedstopReadBuffer");
//                    out.flush();
//                }
//            }).start();
//            socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        myClientTask.cancel(true);
//        Toast.makeText(getApplicationContext(), "Disconnected from server", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==1){
            if(resultCode==RESULT_OK){
                try {
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            out.write("DisconnectedstopReadBuffer");
                            out.flush();
                        }
                    }).start();
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                myClientTask.cancel(true);
                Toast.makeText(getApplicationContext(), "Disconnected from server", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @SuppressLint("StaticFieldLeak")
    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress;
        int dstPort;
        String response = "";

        MyClientTask(String addr, int port){
            dstAddress = addr;
            dstPort = port;
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
            try {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        out.write("DisconnectedstopReadBuffer");
                        out.flush();
                    }
                }).start();
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        protected Void doInBackground(Void... arg0) {

            try {
                if (socket==null){
                    socket = new Socket(dstAddress, dstPort);
                }else {
                    socket.close();
                    socket = new Socket(dstAddress, dstPort);
                }

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(2048);
                int bytesRead;
                byte[] buffer = new byte[2048];
                boolean end=false;
                StringBuilder resp = new StringBuilder();

                out = new PrintWriter(socket.getOutputStream());
                in = socket.getInputStream();

                while (!end) {
                    bytesRead = in.read(buffer);
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    resp.append(byteArrayOutputStream.toString("UTF-8"));
                    if(resp.substring(resp.length()-14).equals("stopReadBuffer")){
                        end=true;
                    }
                }
//                graphString = resp.toString();
                Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
//                myIntent.putExtra("graphString", resp.toString());
                SharedPreferences.Editor editor = getSharedPreferences(globalPreference, MODE_PRIVATE).edit();
                editor.putString("graphString", resp.substring(0, resp.length()-14));
                editor.apply();
                startActivityForResult(myIntent, 1);

            } catch (UnknownHostException e) {
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                e.printStackTrace();
                response = "IOException: " + e.toString();
            }
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
        }

    }
}