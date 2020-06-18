package curso.android.minitwitter.data;

import android.widget.Toast;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import curso.android.minitwitter.common.Constantes;
import curso.android.minitwitter.common.MyApp;
import curso.android.minitwitter.common.SharedPreferencesManager;
import curso.android.minitwitter.retrofit.AuthTwitterClient;
import curso.android.minitwitter.retrofit.AuthTwitterService;
import curso.android.minitwitter.retrofit.requests.RequestCreateTweet;
import curso.android.minitwitter.retrofit.response.Like;
import curso.android.minitwitter.retrofit.response.Tweet;
import curso.android.minitwitter.retrofit.response.TweetDeleted;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TweetRepository {
    AuthTwitterService authTwitterService;
    AuthTwitterClient authTwitterClient;
    MutableLiveData<List<Tweet>>  allTweets;
    MutableLiveData<List<Tweet>>  favTweets;
    String username;


    TweetRepository() {
        authTwitterClient = AuthTwitterClient.getInstance();
        authTwitterService = authTwitterClient.getAuthTwitterService();
        allTweets = getAllTweets();
        username = SharedPreferencesManager.getSomeStringValue(Constantes.PREF_USER);
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
        RequestCreateTweet requestCreateTweet = new RequestCreateTweet(mensaje);

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

    public void deleteTweet(final int idTweet){
        Call<TweetDeleted> call = authTwitterService.deleteTweet(idTweet);

        call.enqueue(new Callback<TweetDeleted>() {
            @Override
            public void onResponse(Call<TweetDeleted> call, Response<TweetDeleted> response) {
                if(response.isSuccessful()){
                    List<Tweet> clonedTweets = new ArrayList<>();
                    for(int i = 0; i < allTweets.getValue().size(); i++){
                        if(allTweets.getValue().get(i).getId() != idTweet){
                            clonedTweets.add(allTweets.getValue().get(i));
                        }
                    }
                    allTweets.setValue(clonedTweets);
                    getFavsTweets();
                } else {
                    Toast.makeText(MyApp.getContext(), "Algo ha ido mal intentalo de nuevo", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<TweetDeleted> call, Throwable t) {
                Toast.makeText(MyApp.getContext(), "Error en la conexión intentelo mas tarde.", Toast.LENGTH_SHORT).show();

            }
        });
    }

    public void likeTweet(int idTweet){
        Call<Tweet> call = authTwitterService.likeTweet(idTweet);
        call.enqueue(new Callback<Tweet>() {
            @Override
            public void onResponse(Call<Tweet> call, Response<Tweet> response) {
                if(response.isSuccessful()){
                    List<Tweet> listaClone = new ArrayList<>();
                    // Añadimos en primer lugar el nuevo tweet que nos llega
                    for (int i = 0; i < allTweets.getValue().size() ; i++) {
                        if(allTweets.getValue().get(i).getId().equals(response.body().getId())){
                            listaClone.add(response.body());
                        }
                        else
                            listaClone.add(new Tweet(allTweets.getValue().get(i)));
                    }
                    allTweets.setValue(listaClone);
                    getFavsTweets();
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

    public MutableLiveData<List<Tweet>> getFavsTweets() {
        if(favTweets == null){
            favTweets = new MutableLiveData<>();
        }
        List<Tweet> newFavList = new ArrayList<>();
        Iterator itTweets = allTweets.getValue().iterator();

        while(itTweets.hasNext()){
            Tweet current = (Tweet) itTweets.next();
            Iterator likes = current.getLikes().iterator();
            boolean enc = false;
            while(likes.hasNext() && !enc){
                Like like = (Like) likes.next();
                if(like.getUsername().equals(username)){
                    enc = true;
                    newFavList.add(current);
                }
            }

        }
        favTweets.setValue(newFavList);
        return favTweets;
    }


    }
