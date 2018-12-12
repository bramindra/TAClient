package org.giwi.networkgraph;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.Toast;

public class GraphActivity extends AppCompatActivity {

    private Bundle bundle;
//    private HomeFragment homeFragment;
    private InputFragment inputFragment;
    private GraphFragment graphFragment;
//    private AnswerFragment answerFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);


        FrameLayout mMainFrame = findViewById(R.id.main_frame);
        BottomNavigationView mMainNav = findViewById(R.id.navigation);

        bundle = getIntent().getExtras();
        assert bundle != null;

//        homeFragment = new HomeFragment();
        inputFragment = new InputFragment();
        graphFragment = new GraphFragment();
//        answerFragment = new AnswerFragment();

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
        Toast.makeText(getApplicationContext(), "asas", Toast.LENGTH_SHORT).show();
        MainActivity.myClientTask.cancel(true);
        finish();
    }

    public void setFragment(Fragment fragment, Bundle bundle) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.main_frame, fragment);
        fragmentTransaction.commit();
    }
}