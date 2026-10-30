package DataBase;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {BooksTable.class}, version = 1)
public abstract class DataBase extends RoomDatabase {

    public abstract Dao booksDao();

    private static volatile DataBase instance;

    public static DataBase getInstance(Context context) {

        if (instance == null) {
            synchronized (DataBase.class) {

                if (instance == null) {
                    instance = Room.databaseBuilder(
                            context.getApplicationContext(),
                            DataBase.class,
                            "LibraryDatabase"
                    ).build();
                }
            }
        }

        return instance;
    }
}