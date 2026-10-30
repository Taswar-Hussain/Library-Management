package com.example.librarymangaement;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DataBase.BooksTable;
import DataBase.DataBase;

public class DetailsScreen_SavedBooks extends AppCompatActivity {
    ImageView imgCoverDetails;
    TextView title, authorstv, pubslishyear, referenceKey;
    MaterialButton deleteBtn;
    ListAdapterSaved listAdapterSaved;
    ExecutorService executor;

    BookForSave toDelete;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_details_screen_saved_books);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        imgCoverDetails = findViewById(R.id.cover_details_S);
        title = findViewById(R.id.title_details_s);
        authorstv = findViewById(R.id.authors_details_s);
        pubslishyear = findViewById(R.id.publishyear_details_s);
        referenceKey = findViewById(R.id.refernecekey_details_s);
        deleteBtn = findViewById(R.id.deletebtn_details);
        executor = Executors.newSingleThreadExecutor();

        BooksTable book = (BooksTable) getIntent().getSerializableExtra("savedBook");

        title.setText("Title:"+book.getTitle());
        pubslishyear.setText("Fist Publish Year: "+book.getFirst_publish_year());
        referenceKey.setText("Reference Key:"+book.getKey());
        ArrayList<String> authors = new ArrayList<>();
        authors.add(author(book.getAuthor1()));
        authors.add(author(book.getAuthor2()));
        authorstv.setText("Authors: "+formatStringauthors(authors));
        String imageUrl = "https://covers.openlibrary.org/b/id/" +book.getCover_i()+ "-L.jpg";
        Glide.with(this).load(imageUrl).placeholder(R.drawable.ic_launcher_foreground).error(R.drawable.ic_launcher_foreground).centerCrop().into(imgCoverDetails);

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                bookToDeleteS(book);
            }
        });

    }

    private String author(String author){
        String author1;
        if(author != null && !author.isEmpty()) {
            author1 = author;
        }else{
            author1 = "";
        }
        return author1;
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

    public void bookToDeleteS(BooksTable boook) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DetailsScreen_SavedBooks.this);

        builder.setTitle("Delete the book");
        builder.setMessage("Are you sure want to delete the book ?");
        builder.setIcon(android.R.drawable.ic_dialog_alert);

        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        DataBase.getInstance(getApplicationContext()).booksDao().deleteBook(boook);

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(DetailsScreen_SavedBooks.this, "Book Deleted", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        });
                    }
                });
            }
        });

        builder.create().show();

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executor != null) {
            executor.shutdown();
        }
    }
}