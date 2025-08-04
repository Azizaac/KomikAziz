package com.aziz.komikaziz;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import java.util.List;

public class ComicPageAdapter extends RecyclerView.Adapter<ComicPageAdapter.PageViewHolder> {
    private Context context;
    private List<String> pageUrls;

    public ComicPageAdapter(Context context, List<String> pageUrls) {
        this.context = context;
        this.pageUrls = pageUrls;
    }

    @NonNull
    @Override
    public PageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comic_page, parent, false);
        return new PageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageViewHolder holder, int position) {
        String pageUrl = pageUrls.get(position);

        Picasso.get()
                .load(pageUrl)
                .placeholder(R.drawable.placeholder_comic)
                .error(R.drawable.placeholder_comic)
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return pageUrls.size();
    }

    public class PageViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;

        public PageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivComicPage);
        }
    }
}