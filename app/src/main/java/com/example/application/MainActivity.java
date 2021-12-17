package com.example.application;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.application.certificate.CertificateFragment;
import com.example.application.board.BoardFragment;
import com.example.application.news.NewsFragment;
import com.example.application.video.VideoFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;
    private FragmentTransaction ft;
    private FragmentManager fm;
    private BoardFragment boardFragment;
    private NewsFragment newsFragment;
    private VideoFragment videoFragment;
    private CertificateFragment certificateFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_board:
                        setFrag(0);
                        break;
                    case R.id.action_news:
                        setFrag(1);
                        break;
                    case R.id.action_video:
                        setFrag(2);
                        break;
                    case R.id.action_certificate:
                        setFrag(3);
                        break;
                }

                return true;
            }
        });

        boardFragment = new BoardFragment();
        newsFragment = new NewsFragment();
        videoFragment = new VideoFragment();
        certificateFragment = new CertificateFragment();
        setFrag(0);


    }

    private void setFrag(int n) {
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        switch (n) {
            case 0:
                ft.replace(R.id.mainFrame, boardFragment);
                ft.commit();
                break;
            case 1:
                ft.replace(R.id.mainFrame, newsFragment);
                ft.commit();
                break;
            case 2:
                ft.replace(R.id.mainFrame, videoFragment);
                ft.commit();
                break;
            case 3:
                ft.replace(R.id.mainFrame, certificateFragment);
                ft.commit();
                break;

        }

    }
}