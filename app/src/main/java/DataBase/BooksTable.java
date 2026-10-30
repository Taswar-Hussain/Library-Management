package DataBase;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

@Entity(tableName = "books")
public class BooksTable implements Serializable {

    @PrimaryKey(autoGenerate = true)
    int id;
    String key,title;
    int cover_i, first_publish_year;
    String author1,author2;

    public BooksTable(String key, String title, int cover_i, int first_publish_year, String author1, String author2) {
        this.key = key;
        this.title = title;
        this.cover_i = cover_i;
        this.first_publish_year = first_publish_year;
        this.author1 = author1;
        this.author2 = author2;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCover_i() {
        return cover_i;
    }

    public void setCover_i(int cover_i) {
        this.cover_i = cover_i;
    }

    public int getFirst_publish_year() {
        return first_publish_year;
    }

    public void setFirst_publish_year(int first_publish_year) {
        this.first_publish_year = first_publish_year;
    }

    public String getAuthor1() {
        return author1;
    }

    public void setAuthor1(String author1) {
        this.author1 = author1;
    }

    public String getAuthor2() {
        return author2;
    }

    public void setAuthor2(String author2) {
        this.author2 = author2;
    }
}
