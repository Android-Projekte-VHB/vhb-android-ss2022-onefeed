package com.example.myapplication.fragment.onboard;

import android.annotation.SuppressLint;
import android.content.res.TypedArray;
import android.graphics.Point;
import android.os.Bundle;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import com.example.myapplication.animation.onboard.SocialMediaAnimation;
import com.example.myapplication.R;

public class SocialMediaFragment extends Fragment {

    private final Point size = new Point();
    private final int xSpeed =1;
    private final int ySpeed =4;
    private final int spacing = 330;
    private final int startPoint =0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_social_media_fragement, container, false);
        initAnimation(view,R.id.imageRedditButton,xSpeed,ySpeed);
        initAnimation(view,R.id.imageTwitterButton,-2*xSpeed,xSpeed-ySpeed);
        return view;
    }




    @SuppressLint("UseCompatLoadingForDrawables")
    private void initAnimation(View view, int id, int xSpeed, int ySpeed){
        WindowManager windowManager = requireActivity().getWindowManager();
        windowManager.getDefaultDisplay().getSize(size);
        SocialMediaAnimation bubbleAnimation = view.findViewById(id);
        // Todo: simplify attr resolution?
        TypedArray a = requireContext().getTheme().obtainStyledAttributes(
                R.style.AppTheme, new int[] {androidx.appcompat.R.attr.colorPrimary}
        );
        int attributeResourceId = a.getResourceId(0, 0);
        DrawableCompat.setTint(bubbleAnimation.getDrawable(), requireContext().getColor(attributeResourceId));
        bubbleAnimation.setX(startPoint,size.x-spacing);
        bubbleAnimation.setY(spacing,size.y-spacing-spacing);
        bubbleAnimation.setXSpeed(xSpeed);
        bubbleAnimation.setYSpeed(ySpeed);
        bubbleAnimation.animateBubbles();
        stopAnimation(bubbleAnimation);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void stopAnimation(SocialMediaAnimation bubbleAnimation) {
        bubbleAnimation.setOnClickListener(view1 -> {
            DrawableCompat.setTint(bubbleAnimation.getDrawable(),ContextCompat.getColor(requireContext(),R.color.white));
            bubbleAnimation.stopAnimation();
            bubbleAnimation.setBackground(getResources().getDrawable(R.drawable.customyesbutton, requireActivity().getTheme()));
        });
    }
}