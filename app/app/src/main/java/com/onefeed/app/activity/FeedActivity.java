package com.onefeed.app.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.browser.customtabs.CustomTabsIntent;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageButton;
import android.widget.Toast;

import com.onefeed.app.R;
import com.onefeed.app.data.feed.FeedViewModel;
import com.onefeed.app.adapter.NewsCardListAdapter;
import com.onefeed.app.fragment.feed.ErrorFragment;
import com.onefeed.app.notification.Service;

public class FeedActivity extends AppCompatActivity {

    private FeedViewModel viewModel;

    ErrorFragment errorFragment;
    SwipeRefreshLayout refreshLayout;
    private NewsCardListAdapter adapter;
    private RecyclerView recycler;
    private ImageButton sourcesNavigationButton;
    private ImageButton insightNavigationButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);

        // ViewModel
        this.viewModel = new ViewModelProvider(this).get(FeedViewModel.class);

        // Title-bar
        setSupportActionBar(findViewById(R.id.toolbar_collapse));
        initializeNavigationButtons();
        //initGestures();

        // Service
        startService();

        // Swipe to refresh
        this.refreshLayout = findViewById(R.id.feed_swipe_refresh);
        refreshLayout.setOnRefreshListener(() -> this.viewModel.loadNewsCards(this));
        refreshLayout.setRefreshing(true); // Refresh for first load

        // Refresh on sources change
        this.viewModel.getSources().observe(this, sources -> {
            this.viewModel.loadNewsCards(this);
        });


        this.viewModel.getInternetConnected().observe(this, connected -> {
            if (!connected) {
                errorFragment = new ErrorFragment();
                errorFragment.setListener(boo -> {
                    viewModel.loadNewsCards(this);
                });
                errorFragment.show(getSupportFragmentManager(),"");
                Log.d("TAG", "onCreate: Disconnected from internet!");
            }
        });

        // Open browser window in app on click
        this.adapter = new NewsCardListAdapter(url -> {
            if (viewModel.getLimitIsEnabled(this)) {
                // Show toast if limit reached
                if (viewModel.getIsLimitIsReached()) {
                    Toast.makeText(
                            this,
                            getString(R.string.feed_limit_reached),
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                // Otherwise count up
                viewModel.addReadArticle(url);
            }

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse(url));
        });

        // News cards recycler
        this.recycler = findViewById(R.id.recycler_news_cards);
        this.recycler.setLayoutManager(new LinearLayoutManager(this));
        this.recycler.setAdapter(this.adapter);
        this.recycler.setItemAnimator(null); // Disables animation

        // Cards listeners
        this.viewModel.getNewsCards().observe(this, newsCards -> {
            if (newsCards.stream().count() > 0) {
                adapter.updateItems(newsCards);
                refreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });

        this.viewModel.getNewsReadList().observe(this, l -> {
            // Set limit reached if user read enough articles
            viewModel.setLimitReached(l.size() >= viewModel.getAmountArticlesReadLimit(this));

            Log.d("TAG", "onCreate: read: " + l.size() + " of " +
                    viewModel.getAmountArticlesReadLimit(this) + " articles.");
        });
    }

    private void startService() {
        Intent intent = new Intent(FeedActivity.this, Service.class);
        if(isMyServiceRunning()){
            getBaseContext().stopService(intent);
        }
        startService(intent);
    }

    // This Method check if the Service is running
    private boolean isMyServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo
                service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (Service.class.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void initializeNavigationButtons() {
        this.sourcesNavigationButton = findViewById(R.id.sources_icon);
        this.sourcesNavigationButton.setOnClickListener(l -> {
            Intent intent = new Intent(this, AddSourceActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
        });

        this.insightNavigationButton = findViewById(R.id.insight_icon);
        this.insightNavigationButton.setOnClickListener(l -> {
            Intent intent = new Intent(this, InsightActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left,R.anim.slide_out_right);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu, this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.empty, menu);
        return true;
    }
}
