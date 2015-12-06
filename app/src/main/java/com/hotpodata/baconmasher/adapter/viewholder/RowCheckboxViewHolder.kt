package com.hotpodata.baconmasher.adapter.viewholder;

import android.support.v7.widget.AppCompatCheckBox;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by jdrotos on 1/21/15.
 */
public class RowCheckboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mTextOne: TextView
    var mCheckBox: AppCompatCheckBox

    init {
        mTextOne = itemView.findViewById(android.R.id.text1) as TextView
        mCheckBox = itemView.findViewById(android.R.id.checkbox) as AppCompatCheckBox
    }
}
