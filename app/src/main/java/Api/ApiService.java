package Api;

import model_adapter.BookResponseModel;
import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiService {
    @GET("search.json?q=programming&limit=30")
    Call<BookResponseModel> getBooks();

}
