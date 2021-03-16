package com.example.movieapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.movieapp.R;
import com.example.movieapp.model.MovieModel;

import java.util.List;

public class MovieRecycleViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<MovieModel> movieModels;
    private OnMovieListener onMovieListener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item, parent,false);
        return new MovieViewHolder(view,onMovieListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((MovieViewHolder)holder).title.setText(movieModels.get(position).getTitle());
        ((MovieViewHolder)holder).releaseDate.setText(movieModels.get(position).getRelease_date());
        ((MovieViewHolder)holder).duration.setText(movieModels.get(position).getRuntime());

        ((MovieViewHolder)holder).ratingBar.setRating((movieModels.get(position).getVote_average())/2);

        //Glide
        Glide.with(holder.itemView.getContext())
                .load(movieModels.get(position))
                .into(((MovieViewHolder)holder).imageView);
    }

    @Override
    public int getItemCount() {
        return movieModels.size();
    }

    public void setMovieModels(List<MovieModel> movieModels) {
        this.movieModels = movieModels;
        notifyDataSetChanged();
    }
}
