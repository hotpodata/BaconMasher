package com.hotpodata.baconmasher.adapter.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hotpodata.baconmasher.R;
import com.hotpodata.baconmasher.view.TextAlignmentSelector;


/**
 * Created by jdrotos on 1/21/15.
 */
public class RowTextAlignmentSelectorViewHolder extends RecyclerView.ViewHolder {
    public TextAlignmentSelector mTextAlignmentSelector;

    public RowTextAlignmentSelectorViewHolder(View itemView) {
        super(itemView);
        mTextAlignmentSelector = (TextAlignmentSelector) itemView.findViewById(R.id.text_alignment_selector);
    }
}
