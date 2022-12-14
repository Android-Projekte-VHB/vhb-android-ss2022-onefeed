package com.onefeed.app.viewholder;

 import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.onefeed.app.R;
import com.onefeed.app.adapter.AdapterListAddActivity;
import com.onefeed.app.animation.addSource.AnimateLinearLayout;
 import com.onefeed.app.data.addSource.Constants;
 import com.onefeed.app.data.addSource.SourceAdd;

import java.util.Objects;

public class ViewHolderAddActivity extends RecyclerView.ViewHolder{

    /*
    Constants
    */
    private final ImageView imageView;
    private final TextView textView;
    private final LinearLayout linearLayout;
    private boolean longSourceClick;
    private boolean setAnimation;
    private final AnimateLinearLayout animateLinearLayout = itemView
            .findViewById(R.id.frameLayout_icons_source);

    /*
    Constructor
     */
    public ViewHolderAddActivity(@NonNull View itemView) {
        super(itemView);
        imageView = itemView.findViewById(R.id.source_image);
        textView = itemView.findViewById(R.id.source_text);
        linearLayout = itemView.findViewById(R.id.deleteLayout);
        linearLayout.setVisibility(View.GONE);

    }

    /*
    Bind Method
     */
    public void bind(final SourceAdd source, final AdapterListAddActivity.OnItemClickListener listener) {
        itemView.setOnClickListener( view -> listener.onItemClick(source));

        // Check if Source is a ADD Button
        if(Objects.equals(source.getName(), Constants.ADDButton.name())){
            setAddButton(source);
            return;
        }
        // set Picture and Text of the source Item
        if(source.getImageRessourceID() != 0){
           imageView.setImageResource(source.getImageRessourceID());
        }
        textView.setText(source.getName());

        // this if clauses give the possibility to stop and start the Animation
        if(source.getAnimation()){
                animateLinearLayout.animateItems();
                linearLayout.setVisibility(View.VISIBLE);
                setAnimation = true;
            }
            if(!source.getAnimation()&&setAnimation){
                animateLinearLayout.stopAnimation();
                linearLayout.setVisibility(View.GONE);
            }

                textView.setText(source.getName());
    }

    /*
    This Method initial the ADD Button
     */
    private void setAddButton(SourceAdd source) {
        textView.setText("");
        imageView.setImageDrawable(source.getImage());
    }


    /*
    This Method is responsible if the user pressed the Item Long
     */
    public void bindLong(SourceAdd source, AdapterListAddActivity.
            longItemClickListener longItemClickListener) {
        /*
        if someone pressed Long on the ADD button there should be no Animation, that's the reason
        why an ADD Button source Element will be returned
         */
        if (Objects.equals(source.getName(), Constants.ADDButton.name())){
            return;
        }

        /*
        Set on all Source Items an on Long Click Listener
         */
        itemView.setOnLongClickListener(view -> {
            // This if clause is responsible if the Item was not long clicked before
            if(!longSourceClick){
                longItemClickListener.onLongClick(source);
                if(!Objects.equals(source.getName(), Constants.ADDButton.name())){
                    linearLayout.setVisibility(View.VISIBLE);
                }
                longSourceClick = true;
                return true;
            }
            /*
            if the item was long clicked again the delete Process will be stopped
             */
            longSourceClick = false;
            longItemClickListener.onLongClick(source);
            return false;
        });
    }
}


