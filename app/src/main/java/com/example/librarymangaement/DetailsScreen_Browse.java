package com.example.librarymangaement;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DataBase.Dao;
import DataBase.DataBase;
import model_adapter.docs;
import DataBase.BooksTable;

public class DetailsScreen_Browse extends AppCompatActivity {

    ImageView imgCoverDetails;
    TextView title, authorstv, pubslishyear, referenceKey;
    MaterialButton saveBtn,readOnlineBtn;

    ExecutorService executor;
    ListAdapterSaved updateList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_screen_browse);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        executor = Executors.newSingleThreadExecutor();

        imgCoverDetails = findViewById(R.id.cover_details);
        title = findViewById(R.id.title_details);
        authorstv = findViewById(R.id.authors_details);
        pubslishyear = findViewById(R.id.publishyear_details);
        referenceKey = findViewById(R.id.refernecekey_details);
        saveBtn = findViewById(R.id.savebtn_details);
        readOnlineBtn = findViewById(R.id.previewbtn_details);

        docs book = (docs) getIntent().getSerializableExtra("book_data");

        title.setText("Title:"+book.getTitle());
        pubslishyear.setText("Fist Publish Year: "+book.getFirst_publish_year());
        referenceKey.setText("Reference Key:"+book.getKey());
        List<String> authors = book.getAuthor();
        authorstv.setText("Authors: "+formatStringauthors(authors));
        String imageUrl = "https://covers.openlibrary.org/b/id/" +book.getCover_i()+ "-L.jpg";

        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).centerCrop().into(imgCoverDetails);

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveBook(book);
            }
        });

        readOnlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String key = book.getKey();

                if(key != null && !key.isEmpty()){
                    String url = "https://openlibrary.org"+key;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                }else{
                    Toast.makeText(DetailsScreen_Browse.this, "Book is not avialable", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private String formatStringauthors(List<String> authorsl){
        String formattedAuthors;
        if(authorsl != null && !authorsl.isEmpty()){
            formattedAuthors = TextUtils.join(", ",authorsl);
        }else{
            formattedAuthors = "Unknown Authors";
        }
        return formattedAuthors;
    }
    private void saveBook(docs doc){
        String author2= "",author1="";
        List<String> authors = doc.getAuthor();

        if (authors != null && authors.size() > 0
                && authors.get(0) != null) {
            author1 = authors.get(0);
        }

        if (authors != null && authors.size() > 1
                && authors.get(1) != null) {
            author2 = authors.get(1);
        }
        BooksTable book = new BooksTable(doc.getKey(),doc.getTitle(),doc.getCover_i(),doc.getFirst_publish_year(),author1,author2);

        executor.execute(new Runnable() {
            @Override
            public void run() {

                Dao dao =  DataBase.getInstance(getApplicationContext()).booksDao();

                boolean exists = dao.bookExists(book.getKey());

                if(!exists){
                    dao.insertBook(book);
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(exists){
                            Toast.makeText(DetailsScreen_Browse.this, "Already Exists!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(DetailsScreen_Browse.this, "Book Added", Toast.LENGTH_SHORT).show();
                            if(updateList != null) {
                                updateList.bookToShowInadapterList(book);
                            }
                        }
                    }
                });
            }
        });
    }
}