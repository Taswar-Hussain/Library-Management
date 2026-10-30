package com.example.librarymangaement;

import android.widget.Adapter;

import java.util.ArrayList;
import java.util.List;

import DataBase.BooksTable;
import model_adapter.SavedBooksAdapter;
import model_adapter.docs;

public interface BookForSave {
    public void bookToSave(BooksTable booksTable); //this one is used by BrowseBookAdapter and implemented in mian acitivity
    public void bookToDelete(BooksTable boook);

    public void bookDetails(docs doc);

    public void bookDetails(BooksTable book);

}
