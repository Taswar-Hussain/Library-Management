package model_adapter;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class BookResponseModel {
    int numFound;

    @SerializedName("docs")
    List<docs> books;

    public List<docs> getBooks(){
        return books;
    }
}
