package org.giwi.networkgraph;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {

    private EditText editTextAddress;

    public static MyClientTask myClientTask;
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
//                    Client client = new Client(editTextAddress.getText().toString(),8888);
//                    client.run();
                myClientTask = new MyClientTask(
                        editTextAddress.getText().toString(),
                        8888);
                myClientTask.execute();

//                    while(GraphActivity.graphString!=null){
//                        startActivity(new Intent(getActivity(), GraphActivity.class));
//                    }
            }
        };


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
        protected Void doInBackground(Void... arg0) {

            socket = null;

            try {
                socket = new Socket(dstAddress, dstPort);

                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(2048);
                int bytesRead;
                byte[] buffer = new byte[2048];
                boolean end=false;
                String resp = "";

                out = new PrintWriter(socket.getOutputStream());
                in = socket.getInputStream();

                while (!end) {
                    bytesRead = in.read(buffer);
                    byteArrayOutputStream.write(buffer, 0, bytesRead);
                    resp += byteArrayOutputStream.toString("UTF-8");
                    if(resp.substring(resp.length()-14).equals("stopReadBuffer")){
                        end=true;
                    }
                }

                Intent myIntent = new Intent(MainActivity.this, GraphActivity.class);
                myIntent.putExtra("graphString", resp);
//                Bundle bundle =  new Bundle();
//                bundle.putString("graphString", byteArrayOutputStream.toString("UTF-8"));
//                FragmentManager fragmentManager = this.getSupportFragmentManager();
//                FragmentTransaction ft = fragmentManager.beginTransaction();
//                GraphFragment graphFragment = new GraphFragment();
//                graphFragment.setArguments(bundle);
//                ft.replace(R.id.main_frame, new GraphFragment());
//                ft.commit();
                startActivity(myIntent);
//                GraphFragment.graphString = byteArrayOutputStream.toString("UTF-8");

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