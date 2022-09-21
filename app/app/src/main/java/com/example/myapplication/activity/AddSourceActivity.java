package com.example.myapplication.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ImageButton;



import com.example.myapplication.R;
import com.example.myapplication.data.addSource.Category;
import com.example.myapplication.data.addSource.SourceAdd;
import com.example.myapplication.data.addSource.AddActivityIcons;
import com.example.myapplication.database.GetData;
import com.example.myapplication.fragment.addSource.DeleteSourceFragment;
import com.example.myapplication.fragment.addSource.EditSourceFragment;
import com.example.myapplication.adapter.AdapterListAddActivity;
import com.example.myapplication.fragment.addSource.InformationFragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class AddSourceActivity extends AppCompatActivity implements
        AdapterListAddActivity.OnItemClickListener,
        AdapterListAddActivity.longItemClickListener,
        DeleteSourceFragment.InputDeleteSourceFragment,
        EditSourceFragment.EditSourceFragmentChanges {

    /*
    Constants
     */
    private HashMap<Category,ArrayList<SourceAdd>> arrayListHashMap = new HashMap<>();
    private final HashMap<Category,ArrayList<SourceAdd>> selectedHashMap = new HashMap<>();
    private AdapterListAddActivity adapterNews;
    private AdapterListAddActivity adapterSocialMedia;
    private AdapterListAddActivity adapterInterests;
    private boolean longSourceClick = false;
    private GetData data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        data = new GetData(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addactivity);
        initUI();
    }

    /*
    in this method all elements are initialized
     */
    private void initUI() {
        setSupportActionBar(findViewById(R.id.toolbar_collapse));
        initHashMap();
        addAddButtonToSelectedHashMap();
        initButton();
    }

    /*
    This Method initialized the HashMap
     */

    private void initHashMap() {
        selectedHashMap.put(Category.Interests,data.getCategory(Category.Interests));
        selectedHashMap.put(Category.SocialMedia,data.getCategory(Category.SocialMedia));
        selectedHashMap.put(Category.Newspaper,data.getCategory(Category.Newspaper));
        AddActivityIcons addActivityIcons = new AddActivityIcons(this);
        arrayListHashMap = addActivityIcons.getArrayListHashMap();
    }

    /*
     This Method initialise the Buttons to close the Activity and show the Information Fragment
     */
    private void initButton() {
        ImageButton buttonInformation = findViewById(R.id.addInfo);
        ImageButton backButton = findViewById(R.id.addback);
        buttonInformation.setOnClickListener(view -> {
            InformationFragment informationFragment = new InformationFragment();
            informationFragment.show(getSupportFragmentManager(),"");
        });
        backButton.setOnClickListener(view -> finish());
    }

    /*
    in this method the recycler view is initialized and passed to the initRecyclerView method
     */
    private void declareRecyclerView(){
        RecyclerView recyclerSocialMedia = findViewById(R.id.recyclerViewQuellenSM);
        RecyclerView recyclerNewsPaper = findViewById(R.id.recyclerViewQuellenNP);
        RecyclerView recyclerInterests = findViewById(R.id.recyclerViewQuellenIn);

        adapterNews = initRecyclerView(
                recyclerNewsPaper,
                selectedHashMap.get(Category.Newspaper));

        adapterInterests = initRecyclerView(
                recyclerInterests,
                selectedHashMap.get(Category.Interests));

        adapterSocialMedia = initRecyclerView(
                recyclerSocialMedia,
                selectedHashMap.get(Category.SocialMedia));

        recyclerInterests.setAdapter(adapterInterests);
        recyclerSocialMedia.setAdapter(adapterSocialMedia);
        recyclerNewsPaper.setAdapter(adapterNews);
    }

    //@TODO add image Path to DataBase to delete Loop
    @SuppressLint("UseCompatLoadingForDrawables")
    private void addAddButtonToSelectedHashMap(){
        /*
        TO add an ADD Button to each RecyclerView this three ADD Buttons will be
        add to the ARRAYList.
        */

        Objects.requireNonNull(selectedHashMap.get(Category.Newspaper))
                .add(new SourceAdd(Category.ADDButton.name(),
                        getDrawable(R.drawable.add),
                        Category.Newspaper));

        Objects.requireNonNull(selectedHashMap.get(Category.SocialMedia))
                .add(new SourceAdd(Category.ADDButton.name(),
                        getDrawable(R.drawable.add ),
                        Category.SocialMedia));

        Objects.requireNonNull(selectedHashMap.get(Category.Interests))
                .add(new SourceAdd(Category.ADDButton.name(),
                        getDrawable(R.drawable.add),
                        Category.Interests));
        declareRecyclerView();
    }



    /*
    In this method, depending on a RecyclerView, the recycler view is
    processed and connected to the adapter
    */
    private AdapterListAddActivity initRecyclerView(
            RecyclerView recyclerView, ArrayList<SourceAdd> arrayList) {

        recyclerView.setLayoutManager(new GridLayoutManager(this,4));
        return new AdapterListAddActivity(this, this,arrayList);
    }

    /*
    This Method will set the Animation if one Item was Long Pressed
     */
    private void setAnimation(boolean boo){
        for(Category categories:selectedHashMap.keySet()){
            for(SourceAdd source: Objects.requireNonNull(selectedHashMap.get(categories))){
                source.setSetAnimation(boo);
            }
            selectedHashMap.put(categories,selectedHashMap.get(categories));
        }
        adapterSocialMedia.setSourceArrayList(Objects.requireNonNull(
                selectedHashMap.get(Category.SocialMedia)));

        adapterInterests.setSourceArrayList(Objects.requireNonNull(
                selectedHashMap.get(Category.Interests)));

        adapterNews.setSourceArrayList(Objects.requireNonNull(
                selectedHashMap.get(Category.Newspaper)));
    }

    /*
    This Method initialize the EditSourceFragment
     */
    private void initEditSourceFragment(SourceAdd source){
        EditSourceFragment editSourceFragment = new EditSourceFragment(
                source,
                selectedHashMap.get(source.getCategories()),
                this,
                data);

        editSourceFragment.setFullList(arrayListHashMap.get(source.getCategories()));
        editSourceFragment.setDataChanged(this);
        editSourceFragment.show(getSupportFragmentManager(),"");
    }


    //this method is inherited from the onclick listener here and
    // using it we can open the fragment depending on the button clicked
    @Override
    public void onItemClick(SourceAdd source) {
        /*
        this if condition checks if there was set a long Click to differentiate between Delete
         an Item and change the Settings of one
         */
        if(!longSourceClick){
            initEditSourceFragment(source);
            return;
        }
        if(source.getName().equals(Category.ADDButton.name())) return;
        DeleteSourceFragment dSf = new DeleteSourceFragment(this);
        dSf.setSource(source);
        dSf.show(getSupportFragmentManager(),"");
    }

    /*
    This Method recognizes from the DeleteSourceFragment if the User Deleted one Item
    */
    @Override
    public void inputDeleteSource(boolean result, SourceAdd source) {
        /*
        set Constants falls to show the User the Process has end
         */
        if (result) {
            /*
            If he pressed yes the Item will be deleted. To show him the difference the Icon will be
            deleted in the Adapter Array List too
             */
            data.removeSource(source);
            updateAdapterList(source);
            Objects.requireNonNull(selectedHashMap
                    .get(source.getCategories())).remove(source);
            declareRecyclerView();
        }
        longSourceClick = false;
        setAnimation(false);
    }

    /*
    This Method will set the new ArrayList to the different Adapters
     */
    private void updateAdapterList(SourceAdd source){
        if(source.getCategories()== Category.Interests){
            adapterInterests.setSourceArrayList(Objects.requireNonNull(
                    selectedHashMap.get(source.getCategories())));
            return;
        }

            if(source.getCategories()== Category.SocialMedia){
        adapterSocialMedia.setSourceArrayList(Objects.requireNonNull(
                selectedHashMap.get(source.getCategories())));
        return;
        }
            adapterNews.setSourceArrayList(Objects.requireNonNull(
                    selectedHashMap.get(source.getCategories())));
    }

    /*
    This Method recognize, if the User press long on one Item
     */
    @Override
    public void onLongClick(SourceAdd source) {
        /*
        This If Clause checks if he pressed the item long before,
        Important to stop the Animation and the Delete Progress
         */
        if(!longSourceClick){
            longSourceClick = true;
            setAnimation(true);
            return;
        }
        longSourceClick = false;
        setAnimation(false);
    }


    @Override
    public void dataHasChanged(Boolean b, SourceAdd sourceAdd) {
        if(b == null){
            return;
        }
        if(!b){
            initHashMap();
            addAddButtonToSelectedHashMap();
            Objects.requireNonNull(selectedHashMap
                    .get(sourceAdd.getCategories())).remove(sourceAdd);
            declareRecyclerView();
            return;
        }
        /*
        Update Selected HashMap from the DataBase to update the Recycler viewer
         */
        initHashMap();
        addAddButtonToSelectedHashMap();
        declareRecyclerView();
    }
}