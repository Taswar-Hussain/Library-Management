package model_adapter;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;
public class docs implements Serializable {
    String key,title;
    int cover_i, first_publish_year;
    @SerializedName("author_name")
    List<String> author;

    public docs(String title, List<String> author, int first_publish_year, int cover_i) {
        this.title = title;
        this.author = author;
        this.first_publish_year = first_publish_year;
        this.cover_i = cover_i;
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

    public List<String> getAuthor() {
        return author;
    }

    public void setAuthor(List<String> author) {
        this.author = author;
    }

    public int getFirst_publish_year() {
        return first_publish_year;
    }

    public void setFirst_publish_year(int first_publish_year) {
        this.first_publish_year = first_publish_year;
    }

    public int getCover_i() {
        return cover_i;
    }

    public void setCover_i(int cover_i) {
        this.cover_i = cover_i;
    }
}
