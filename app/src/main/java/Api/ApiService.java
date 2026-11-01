package Api;

import model_adapter.BookResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("search.json?q=programming&has_fulltext=true&limit=29")
    Call<BookResponseModel> getBooks();

    @GET("search.json?has_fulltext=true&limt=50")
    Call<BookResponseModel> getBooksSearch(@Query("q") String query);

}
