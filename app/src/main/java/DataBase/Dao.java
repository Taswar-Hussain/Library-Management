package DataBase;

import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@androidx.room.Dao
public interface Dao {
    @Insert()
    void insertBook(BooksTable book);

    @Update()
    void updateBook(BooksTable bookToUpdate);

    @Delete()
    void  deleteBook(BooksTable bookToDelete);

    @Query("Select * from books")
    List<BooksTable> getAllBooks();
    @Query("SELECT EXISTS(SELECT 1 FROM books WHERE `key` = :bookKey)")
    boolean bookExists(String bookKey);
}
