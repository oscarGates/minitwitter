package curso.android.minitwitter.retrofit;

import java.util.List;

import curso.android.minitwitter.retrofit.requests.RequestCreateTweet;
import curso.android.minitwitter.retrofit.response.Tweet;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AuthTwitterService {

    @GET("tweets/all")
    Call<List<Tweet>> getAllTweets();

    @POST("tweets/create")
    Call<Tweet> createTweet(@Body RequestCreateTweet requestCreateTweet);
}
