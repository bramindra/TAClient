package com.bramindra.kstpwithdiversity;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

public class GraphActivity extends AppCompatActivity {

    public static String graphString;
    private InputFragment inputFragment;
    private GraphFragment graphFragment;
    private Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(), "Connected to server", Toast.LENGTH_SHORT).show();
        setContentView(R.layout.activity_graph);


        BottomNavigationView mMainNav = findViewById(R.id.navigation);

        bundle = new Bundle();
        bundle.putString("graphString", graphString);

        inputFragment = new InputFragment();
        graphFragment = new GraphFragment();

        setFragment(graphFragment, bundle);


        mMainNav.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId() == R.id.navigation_home){
                    setFragment(graphFragment, bundle);
                    return true;
                }
                else if(menuItem.getItemId() == R.id.navigation_input){
                    setFragment(inputFragment, bundle);
                    return true;
                }
                else{
                    return false;
                }
            }
        });
    }

    public void onBackPressed() {
        MainActivity.myClientTask.cancel(true);
        MainActivity.myClientTask.onCancelled();
        Toast.makeText(getApplicationContext(), "Disconnected from server", Toast.LENGTH_SHORT).show();
        finish();
    }

    public void setFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}