package com.example.movieapp.request;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.movieapp.AppExecutors;
import com.example.movieapp.model.MovieModel;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.util.Constants;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import retrofit2.Call;
import retrofit2.Response;

public class MovieApiClient {

    private MutableLiveData<List<MovieModel>> mMovies;

    private static MovieApiClient instance;

    //Making Global Runnable
    private RetrieveMoviesRunnable retrieveMoviesRunnable;

    public static MovieApiClient getInstance() {
        if (instance == null) {
            instance = new MovieApiClient();
        }

        return instance;
    }

    private MovieApiClient() {
        mMovies = new MutableLiveData<>();
    }

    public LiveData<List<MovieModel>> getMovies() {
        return mMovies;
    }

    public void searchMoviesApi(String query, int pageNumber) {

        if(retrieveMoviesRunnable!= null){
            retrieveMoviesRunnable = null;
        }

        retrieveMoviesRunnable = new RetrieveMoviesRunnable(query,pageNumber);

        final Future myHandler = AppExecutors.getInstance().networkIO().submit(retrieveMoviesRunnable);

        AppExecutors.getInstance().networkIO().schedule(new Runnable() {
            @Override
            public void run() {
                //Cancelling the retrofit request
                myHandler.cancel(true);

            }
        }, 4000, TimeUnit.MILLISECONDS);
    }

    private class RetrieveMoviesRunnable implements Runnable {

        private String query;
        private int pageNumber;
        boolean cancelRequest;

        public RetrieveMoviesRunnable(String query, int pageNumber) {
            this.query = query;
            this.pageNumber = pageNumber;
            cancelRequest = false;
        }

        @Override
        public void run() {

            try{
                Response response = getMovies(query,pageNumber).execute();

                if(cancelRequest){
                    return;
                }

                if(response.code() == 200){
                    List<MovieModel> list = new ArrayList<>(((MovieSearchResponse)response.body()).getMovies());

                    if(pageNumber == 1){
                        mMovies.postValue(list);

                    }else {

                        List<MovieModel> currentMovies = mMovies.getValue();
                        currentMovies.addAll(list);
                        mMovies.postValue(currentMovies);
                    }
                } else{
                    String error = response.errorBody().string();
                    Log.v("MovieApiClient","Error" + error);

                    mMovies.postValue(null);
                }


            } catch (IOException e) {
                e.printStackTrace();
                mMovies.postValue(null);
            }


            if (cancelRequest) {
                return;
            }
        }

        private Call<MovieSearchResponse> getMovies(String query, int pageNumber) {
            return Service.getMovieApi().searchMovie(Constants.API_KEY,
                    query,
                    String.valueOf(pageNumber));
        }


        private void cancelRequest(){
            Log.v("MovieApiClent", "Cancelling Search Request");
            cancelRequest = true;
        }

    }

}


