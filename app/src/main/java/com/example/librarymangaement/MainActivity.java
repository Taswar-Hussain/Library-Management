package com.example.librarymangaement;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Adapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import DataBase.BooksTable;
import DataBase.DataBase;
import DataBase.Dao;
import model_adapter.AdapterBooks;
import model_adapter.OnClickedListner;
import model_adapter.SavedBooksAdapter;
import model_adapter.docs;

public class MainActivity extends AppCompatActivity implements BookForSave {
   BottomNavigationView bottomNavigationView;
    ExecutorService executor;
    ListAdapterSaved listAdapterSaved;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        bottomNavigationView = findViewById(R.id.bottomNavigationView);



        if(savedInstanceState == null){
            loadFragment(new savedBooks());
        }

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();

                if(id == R.id.browseM){
                    loadFragment(new BrowseBooks());
                } else if (id == R.id.saveM) {
                    loadFragment(new savedBooks());
                }else{
                    return false;
                }
                return true;
            }
        });

        executor = Executors.newSingleThreadExecutor();

    }

    private void loadFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(R.id.frameLayout,fragment).commit();
    }

    @Override
    public void bookToSave(BooksTable book){
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
                            Toast.makeText(MainActivity.this, "Already Exists!", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(MainActivity.this, "Book Added", Toast.LENGTH_SHORT).show();
                        }
                        if(listAdapterSaved != null){
                            listAdapterSaved.bookToShowInadapterList(book);
                        }else{
                            Toast.makeText(MainActivity.this, "Book saved in Device", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    @Override
    public void bookToDelete(BooksTable boook) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

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
                                if(listAdapterSaved != null){
                                    listAdapterSaved.DeletedBook(boook);
                                }
                            }
                        });
                    }
                });
            }
        });

        builder.create().show();

    }

    @Override
    public void bookDetails(docs doc) {
        Intent intent = new Intent(MainActivity.this,DetailsScreen_Browse.class);
        intent.putExtra("book_data",doc);
        startActivity(intent);
    }

    @Override
    public void bookDetails(BooksTable book) {
        Intent intent = new Intent(MainActivity.this, DetailsScreen_SavedBooks.class);
        intent.putExtra("savedBook",book);
        startActivity(intent);
    }
}