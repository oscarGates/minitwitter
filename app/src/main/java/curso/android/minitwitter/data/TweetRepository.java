package curso.android.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import curso.android.minitwitter.common.MyApp;
import curso.android.minitwitter.retrofit.AuthTwitterClient;
import curso.android.minitwitter.retrofit.AuthTwitterService;
import curso.android.minitwitter.retrofit.requests.RequestCreateTweet;
import curso.android.minitwitter.retrofit.response.Tweet;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {
    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<List<Tweet>>  allTweets;
    TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
    }
    public MutableLiveData<List<Tweet>> getAllTweets(){
        if(allTweets == null){
            allTweets = new MutableLiveData<>();
        }
        Call<List<Tweet>> call = authTwitterService.getAllTweets();
        call.enqueue(new Callback<List<Tweet>>() {
            @Override
            public void onResponse(Call<List<Tweet>> call, Response<List<Tweet>> response) {
                if(response.isSuccessful()){
                    allTweets.setValue(response.body());
                }
                Toast.makeText(MyApp.getContext(), "Algo ha ido mal", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Call<List<Tweet>> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión de internet", Toast.LENGTH_SHORT);
            }
        });
        return allTweets;
    }

    public void createTweet(String mensaje){
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet();
        Call<Tweet> call = authTwitterService.createTweet(requestCreateTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listaClone = new ArrayList<>();
                    // Añadimos en primer lugar el nuevo tweet que nos llega
                    listaClone.add(response.body());
                    for (int i = 0; i < allTweets.getValue().size() ; i++) {
                        listaClone.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(listaClone);
                }else{
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal intentalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Tweet> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión intentelo mas tarde.", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
