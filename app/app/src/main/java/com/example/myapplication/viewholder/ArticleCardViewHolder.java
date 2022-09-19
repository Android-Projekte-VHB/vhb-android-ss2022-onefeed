package com.example.myapplication.viewholder;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.data.card.ArticleCard;

public class ArticleCardViewHolder extends RecyclerView.ViewHolder {
    private final TextView titleView;
    private final ImageView imageView;
    private final TextView sourceTextView;
    private final ImageView sourceIconImageView;

    public ArticleCardViewHolder(View itemView) {
        super(itemView);
        this.titleView = itemView.findViewById(R.id.article_card_title);
        this.imageView = itemView.findViewById(R.id.article_card_image);
        this.sourceTextView = itemView.findViewById(R.id.source_name);
        this.sourceIconImageView = itemView.findViewById(R.id.source_icon);
    }

    public void bind(ArticleCard data) {
        // Card content
        titleView.setText(data.getTitle());
        // Todo: Implement cards without image
        if (data.getImage() != null) {
            this.imageView.setImageBitmap(data.getImage());
        }

        // Source label
        sourceTextView.setText(data.getSource().getName());
        sourceIconImageView.setImageBitmap(data.getSource().getIcon());
    }

    public static ArticleCardViewHolder create(ViewGroup parent) {
        View view = LayoutInflater.from(
                parent.getContext()).inflate(R.layout.component_card_article, parent,  false
        );
        return new ArticleCardViewHolder(view);
    }
}
