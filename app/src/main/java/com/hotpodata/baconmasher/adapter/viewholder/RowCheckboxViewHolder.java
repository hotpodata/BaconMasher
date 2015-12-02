package com.hotpodata.baconmasher.adapter.viewholder;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.AppCompatCheckedTextView;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.hotpodata.baconmasher.R;
import com.hotpodata.baconmasher.view.CircleImageView;


/**
 * Created by jdrotos on 1/21/15.
 */
public class RowCheckboxViewHolder extends RecyclerView.ViewHolder {
    public TextView mTextOne;
    public AppCompatCheckBox mCheckBox;

    public RowCheckboxViewHolder(View itemView) {
        super(itemView);
        mTextOne = (TextView) itemView.findViewById(android.R.id.text1);
        mCheckBox = (AppCompatCheckBox) itemView.findViewById(android.R.id.checkbox);
    }
}
