package curso.android.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.List;

import curso.android.minitwitter.MyTweetRecyclerViewAdapter;
import curso.android.minitwitter.common.MyApp;
import curso.android.minitwitter.retrofit.AuthTwitterClient;
import curso.android.minitwitter.retrofit.AuthTwitterService;
import curso.android.minitwitter.retrofit.response.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {
    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    private LiveData<List<Tweet>>  allTweets;
    TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
    }
    public LiveData<List<Tweet>> getAllTweets(){
        final MutableLiveData<List<Tweet>> data = new MutableLiveData<>();
        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    data.setValue(response.body());
                }
                Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión de internet", Toast.LENGTH_SHORT);
            }
        });
        return data;
    }
}
