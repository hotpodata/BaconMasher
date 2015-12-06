package com.hotpodata.baconmasher.adapter.viewholder

import android.support.v7.widget.RecyclerView
import android.view.View

import com.hotpodata.baconmasher.R
import com.hotpodata.baconmasher.view.TextAlignmentSelector


/**
 * Created by jdrotos on 1/21/15.
 */
class RowTextAlignmentSelectorViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var mTextAlignmentSelector: TextAlignmentSelector

    init {
        mTextAlignmentSelector = itemView.findViewById(R.id.text_alignment_selector) as TextAlignmentSelector
    }
}
