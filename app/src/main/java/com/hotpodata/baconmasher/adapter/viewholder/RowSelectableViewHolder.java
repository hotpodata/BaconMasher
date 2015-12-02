package com.hotpodata.baconmasher.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hotpodata.baconmasher.view.CircleImageView;


/**
 * Created by jdrotos on 1/21/15.
 */
public class RowSelectableViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextOne;
    public ImageView mIcon;

    public RowSelectableViewHolder(View itemView) {
        super(itemView);
        mTextOne = (TextView) itemView.findViewById(android.R.id.text1);
        mIcon = (ImageView) itemView.findViewById(android.R.id.icon);
    }
}
