package com.example.myapplication.addNewQuelle.Adapter;

import android.annotation.SuppressLint;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.addNewQuelle.Quellen;

public class ViewholderEditQuellenFragement extends RecyclerView.ViewHolder{

    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private final Switch aSwitch;
    private final TextView textView;

    public ViewholderEditQuellenFragement(@NonNull View itemView) {
        super(itemView);
        aSwitch = itemView.findViewById(R.id.editQuellenIconSwitch);
        textView = itemView.findViewById(R.id.editQuellenIconText);
    }

    public void bind(Quellen quellen, AdapterEditQuellenFragement.QuelleSettingsChanged quelleSettingsChanged, int size) {
        //@TODO Bug wenn nur ein Element in Reihe Add Button zeigt nicht das richtige an
        //@TODO switch bearbeiten switch eintrag zu speicern
        if(size<=1){
            String setNotification = "Notification";
            textView.setText(setNotification);
                aSwitch.setChecked(quellen.isNotification());
                aSwitch.setOnClickListener(view -> quelleSettingsChanged.changedQuelle(quellen));
                return;
        }
        textView.setText(quellen.getName());
        aSwitch.setChecked(quellen.isEnabeld());
        aSwitch.setOnClickListener(view -> quelleSettingsChanged.changedQuelle(quellen));
    }
}
