package curso.android.minitwitter.retrofit;

import java.util.List;

import curso.android.minitwitter.retrofit.response.Tweet;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AuthTwitterService {

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();
}