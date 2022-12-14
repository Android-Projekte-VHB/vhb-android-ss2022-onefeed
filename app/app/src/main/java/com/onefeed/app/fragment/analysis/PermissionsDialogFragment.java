package com.onefeed.app.fragment.analysis;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.onefeed.app.R;
import com.onefeed.app.activity.FeedActivity;
import com.onefeed.app.data.addSource.Constants;

import java.util.Objects;

public class PermissionsDialogFragment extends DialogFragment {

    private InsightSettingsListener listener;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater,
            @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragement_consumption_permission, container, false);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        initButtons(view);
        return view;
    }

    /*
    This Method initialise the Data and sets the onClick Listener
     */
    private void initButtons(View view) {
        Button buttonYes = view.findViewById(R.id.button_yes_permission);
        Button buttonNo = view.findViewById(R.id.button_no_permission);
        setButtonChanges(buttonYes,true);
        setButtonChanges(buttonNo,false);
    }

    /*
    This Method set an OnClickListener to the Buttons
     */
    private void setButtonChanges(Button button, boolean decision){
        button.setOnClickListener(view -> {
            if (decision){
                /*
                calls the editSharedPreferences() Method
                    stops the Fragment
                    and return the Method
                 */
                editSharedPreferences(true);
                onStop();
                return;
            }
            /*
            If you click NO the Intent will start a new Activity and stops the Fragment
             */
            Intent intent = new Intent(getContext(), FeedActivity.class);
            startActivity(intent);
            onStop();
        });
    }

    /*
    This Method edit the Shared Preferences
     */
    private void editSharedPreferences(boolean decision) {
        SharedPreferences pref = requireContext().getSharedPreferences(
                Constants.initial.ConsumptionAnalyse.name(), Context.MODE_PRIVATE);
        pref.edit()
                .putBoolean(Constants.insightSettings.limitationIsEnabled.name(), decision)
                .apply();
        if (this.listener != null) {
            this.dismiss();
            listener.onResult(decision);
        }
    }

    public void setResultListener(InsightSettingsListener listener) {
        this.listener = listener;
    }

    public interface InsightSettingsListener {
        void onResult(boolean enabled);
    }
}
