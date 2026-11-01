package com.example.librarymangaement;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.card.MaterialCardView;

public class AboutUs extends AppCompatActivity {
    MaterialCardView cardShare, cardRate, cardRepo, cardFeedback;
    MaterialToolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_about_us);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        cardShare = findViewById(R.id.cardActtion_share);
        cardRate= findViewById(R.id.cardAction);
        cardRepo = findViewById(R.id.cardActtion_repo);
        cardFeedback = findViewById(R.id.cardActtion_feedback);
        toolbar = findViewById(R.id.aboutToolbar);

        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        
        cardShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT,"Hey there!, download & install this app. You can manage books and read them online. You can also save your books to organize.");
                Intent intent1 = Intent.createChooser(intent,"Share Via");
                startActivity(intent1);
            }
        });

        cardFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_SENDTO);
                intent.setData(Uri.parse("mailto:"));
                intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"taswarhussain2006@gmail.com"});

                intent.putExtra(Intent.EXTRA_SUBJECT,"Library app inquiry/feedback");
                intent.putExtra(Intent.EXTRA_TEXT,new String[]{"Assalam-o-Alaikum admin, i want ask...."});

                try {
                    startActivity(Intent.createChooser(intent,"Choose Email App"));
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(AboutUs.this, "No email app is installed!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cardRepo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = "https://github.com/Taswar-Hussain/Library-Management";
                Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(url));
                startActivity(intent);
            }
        });

        cardRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(AboutUs.this, "Sorry, App is not deployed on playstore,\nso you can not give rating", Toast.LENGTH_SHORT).show();
            }
        });


    }
}