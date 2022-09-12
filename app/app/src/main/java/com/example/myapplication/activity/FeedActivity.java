package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ImageButton;

import com.example.myapplication.R;
import com.example.myapplication.data.card.ArticleCard;
import com.example.myapplication.data.feed.NewsSource;
import com.example.myapplication.data.card.NewsCard;
import com.example.myapplication.data.card.TwitterCard;
import com.example.myapplication.adapter.NewsCardListAdapter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;

public class FeedActivity extends AppCompatActivity {

    private NewsCardListAdapter adapter;
    private RecyclerView recycler;
    private ImageButton sourcesNavigationButton;
    private ImageButton insightNavigationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // Title-bar
        setSupportActionBar(findViewById(R.id.toolbar_collapse));

        initializeNavigationButtons();

        // News cards recycler
        this.adapter = new NewsCardListAdapter();
        this.recycler = findViewById(R.id.recycler_news_cards);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
        this.recycler.setAdapter(this.adapter);

        // Dummy data setup
        setupDummyCards();
    }

    private void initializeNavigationButtons() {
        this.sourcesNavigationButton = findViewById(R.id.sources_icon);
        this.sourcesNavigationButton.setOnClickListener(l -> {
            Intent intent = new Intent(this, AddSourceActivity.class);
            startActivity(intent);
        });

        this.insightNavigationButton = findViewById(R.id.insight_icon);
        this.insightNavigationButton.setOnClickListener(l -> {
            Intent intent = new Intent(this, InsightActivity.class);
            startActivity(intent);
        });
    }

    private void setupDummyCards() {
        NewsSource sampleArticleSource = new NewsSource(
                "Spiegel", "https://www.spiegel.de/"
        );
        NewsSource sampleTwitterSource = new NewsSource(
                "Twitter", "https://twitter.com/"
        );
        ArticleCard sampleArticleCard = new ArticleCard(
                getString(R.string.lorem_ipsum), sampleArticleSource, LocalDateTime.now()
        );
        TwitterCard sampleTwitterCard = new TwitterCard(
            sampleTwitterSource, LocalDateTime.now(), getString(R.string.lorem_ipsum_long),
                "Elon Musk", "@elonmusk"
        );
        ArrayList<NewsCard> sampleCards = new ArrayList<>(Arrays.asList(
                sampleArticleCard, sampleTwitterCard, sampleArticleCard, sampleArticleCard,
                sampleArticleCard, sampleTwitterCard, sampleArticleCard, sampleArticleCard,
                sampleArticleCard, sampleTwitterCard, sampleArticleCard, sampleArticleCard
                ));
        this.adapter.updateItems(sampleCards);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }
}