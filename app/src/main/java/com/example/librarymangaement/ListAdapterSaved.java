package com.example.librarymangaement;

import DataBase.BooksTable;

public interface ListAdapterSaved {
    public void bookToShowInadapterList(BooksTable booksTable);

    public void DeletedBook(BooksTable book);
}
