package com.bramindra.kstpwithdiversity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import java.io.IOException;

public class GraphActivity extends AppCompatActivity {

    private InputFragment inputFragment;
    private GraphFragment graphFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), "Connected to server", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_graph);

        BottomNavigationView mMainNav = findViewById(R.id.navigation);

        inputFragment = new InputFragment();
        graphFragment = new GraphFragment();


        setFragment(graphFragment);


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.navigation_home){
                    setFragment(graphFragment);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_input){
                    setFragment(inputFragment);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }
//
//    @Override
//    public void onStop(){
//        super.onStop();
//        try {
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    MainActivity.out.write("DisconnectedstopReadBuffer");
//                    MainActivity.out.flush();
//                }
//            }).start();
//            MainActivity.socket.close();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        MainActivity.myClientTask.cancel(true);
//        Toast.makeText(getApplicationContext(), "Disconnected from server", Toast.LENGTH_SHORT).show();
//    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    MainActivity.out.write("DisconnectedstopReadBuffer");
                    MainActivity.out.flush();
                }
            }).start();
            MainActivity.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        MainActivity.myClientTask.cancel(true);
        Toast.makeText(getApplicationContext(), "Disconnected from server", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent();
        setResult(RESULT_OK, intent);
//        MainActivity.myClientTask.cancel(true);
//        MainActivity.myClientTask.onCancelled();
//        Toast.makeText(getApplicationContext(), "Disconnected from server", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void setFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}