package com.example.movieapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.movieapp.model.MovieModel;
import com.example.movieapp.request.MovieApi;
import com.example.movieapp.request.Service;
import com.example.movieapp.response.MovieSearchResponse;
import com.example.movieapp.util.Constants;
import com.example.movieapp.viewmodels.MovieListViewModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MovieListActivity extends AppCompatActivity {

    MovieApi movieApi = Service.getMovieApi();
    Button btn;

    private MovieListViewModel mViewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = findViewById(R.id.button_main);

        mViewModel = new ViewModelProvider(this).get(MovieListViewModel.class);

        observeChange();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchMovieApi("Batman",2);
            }
        });


    }

    private void observeChange(){
        mViewModel.getMovies().observe(this, new Observer<List<MovieModel>>() {
            @Override
            public void onChanged(List<MovieModel> movieModels) {

                if(movieModels != null){
                    for(MovieModel movieModel: movieModels){
                        Log.v("Tag","onChanged: " + movieModel.getTitle());
                    }
                }

            }
        });
    }

    private void searchMovieApi(String query, int pageNumber){
        mViewModel.searchMovieApi(query, pageNumber);
    }

//    private void getRetrofitResponse() {
//
//
//        Call<MovieSearchResponse> responseCall = movieApi.searchMovie(
//                Constants.API_KEY,
//                "Batman superman",
//                "1"
//        );
//
//        responseCall.enqueue(new Callback<MovieSearchResponse>() {
//            @Override
//            public void onResponse(Call<MovieSearchResponse> call, Response<MovieSearchResponse> response) {
//                if(response.code() == 200){
////                    Log.d("MovieListActivity", "onResponse: "+response.body().toString());
//
//                    List<MovieModel> movies = new ArrayList<>(response.body().getMovies());
//
//                    for(MovieModel movie : movies){
//                        Log.v("MovieListActivity","Title "+ movie.getTitle());
//                    }
//                } else{
//                    try{
//                        Log.v("MovieListActivity", "Error" + response.errorBody().toString());
//                    } catch (Exception e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieSearchResponse> call, Throwable t) {
//
//            }
//        });
//
//    }
//
//    private void getRetrofitResponseId(){
//
//        Call<MovieModel> responseCall = movieApi.getMovie(
//                550,
//                Constants.API_KEY
//        );
//
//        responseCall.enqueue(new Callback<MovieModel>() {
//            @Override
//            public void onResponse(Call<MovieModel> call, Response<MovieModel> response) {
//                if(response.code() == 200){
//                    MovieModel movieModel = response.body();
//                    Log.v("MovieId","The repsonse "+ movieModel.getTitle());
//                }else{
//                    try {
//                        Log.v("MovieId","Error: "+response.errorBody().string());
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//
//            @Override
//            public void onFailure(Call<MovieModel> call, Throwable t) {
//
//            }
//        });
//
//    }


}

