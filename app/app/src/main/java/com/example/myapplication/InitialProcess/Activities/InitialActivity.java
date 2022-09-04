package com.example.myapplication.InitialProcess.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.myapplication.InitialProcess.FragementsUI.Interests_fragement;
import com.example.myapplication.InitialProcess.FragementsUI.SocialMedia_Fragement;
import com.example.myapplication.InitialProcess.FragementsUI.Welcome_fragement;
import com.example.myapplication.InitialProcess.InitialData;
import com.example.myapplication.R;

import java.util.ArrayList;

public class InitialActivity extends AppCompatActivity implements Interests_fragement.OnDataPass{

    /*
    This activity is responsible for the entire setup process.
     Here the individual setup steps and the further development of Permissions Activits are completed
     */

     private FragmentManager fragmentManager;
     private ArrayList<String> interests = new ArrayList<>();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_initial);
            initUI();
        }

    // Set up the fragement Manager
    private void initUI() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.frameLayout, Welcome_fragement.class, null)
                .setReorderingAllowed(true)
                .addToBackStack("")
                .commit();
        initButton();
    }

    // This Method ist responsible for the forwarding to the different set up steps
    private void initButton() {
        Button button = findViewById(R.id.buttonWeiter);
        button.setOnClickListener(view -> {
            switch (fragmentManager.getBackStackEntryCount()){
                case 1:
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, Interests_fragement.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("")
                            .commit();
                    return;
                case 2:
                    fragmentManager.beginTransaction()
                            .replace(R.id.frameLayout, SocialMedia_Fragement.class, null)
                            .setReorderingAllowed(true)
                            .addToBackStack("")
                            .commit();
                    return;
                case 3:
                    Intent intent = new Intent(InitialActivity.this, PermissionsActivity.class);
                    intent.putExtra(String.valueOf(InitialData.interestsArrayList),interests);
                    startActivity(intent);
            }
        });
    }


    // This method grabs the Interests ArrayList from the Interests Query
    @Override
    public void onDataPass(ArrayList<String> interessenList) {
        interests = interessenList;
    }
}
