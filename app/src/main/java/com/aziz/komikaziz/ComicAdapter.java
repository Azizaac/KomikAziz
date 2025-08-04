package com.aziz.komikaziz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ComicAdapter extends RecyclerView.Adapter<ComicAdapter.ComicViewHolder> {

    private Context context;
    private List<Comic> comicList;
    private OnComicClickListener listener;

    public ComicAdapter(Context context, List<Comic> comicList, OnComicClickListener listener) {
        this.context = context;
        this.comicList = comicList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ComicViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic, parent, false);
        return new ComicViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ComicViewHolder holder, int position) {
        Comic comic = comicList.get(position);

        holder.tvTitle.setText(comic.getTitle());
        holder.tvAuthor.setText(comic.getAuthor());
        holder.tvGenre.setText(comic.getGenre());
        holder.tvRating.setText(String.format("%.1f", comic.getRating()));

        Picasso.get()
                .load(comic.getImageUrl())
                .placeholder(R.drawable.placeholder_comic)
                .error(R.drawable.placeholder_comic)
                .into(holder.ivCover);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onComicClick(comic);
            }
        });
    }

    @Override
    public int getItemCount() {
        return comicList.size();
    }

    public void updateList(List<Comic> newList) {
        this.comicList = newList;
        notifyDataSetChanged();
    }

    public class ComicViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCover;
        TextView tvTitle, tvAuthor, tvGenre, tvRating;

        public ComicViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCover = itemView.findViewById(R.id.ivComicCover);
            tvTitle = itemView.findViewById(R.id.tvComicTitle);
            tvAuthor = itemView.findViewById(R.id.tvComicAuthor);
            tvGenre = itemView.findViewById(R.id.tvComicGenre);
            tvRating = itemView.findViewById(R.id.tvComicRating);
        }
    }

    public interface OnComicClickListener {
        void onComicClick(Comic comic);
    }
}
