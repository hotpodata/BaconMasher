package com.hotpodata.baconmasher.view

import android.content.Context
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.PorterDuff
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.hotpodata.baconmasher.R
import java.util.*

/**
 * Created by jdrotos on 12/1/15.
 */
class TextAlignmentSelector : LinearLayout {
    interface TextAlignmentListener {
        public fun onSelectionChanged(selectedAlignmentCodes: Set<String>)
    }

    var listener: TextAlignmentListener? = null

    var tl: ImageView? = null
    var tc: ImageView? = null
    var tr: ImageView? = null
    var cl: ImageView? = null
    var c: ImageView? = null
    var cr: ImageView? = null
    var bl: ImageView? = null
    var bc: ImageView? = null
    var br: ImageView? = null

    var views: List<ImageView?> = ArrayList<ImageView>()

    var deselectedBgColor: Int = Color.TRANSPARENT
    var deslectedIconColor: Int = Color.BLACK
    var selectedBgColor: Int = Color.BLACK
    var selectedIconColor: Int = Color.WHITE

    public var selected: MutableSet<String> = HashSet<String>()
        set(sel) {
            field = sel
            syncState()
        }

    constructor(context: Context?) : super(context) {
        init(context, null)
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context?, attrs: AttributeSet?) {
        var inflater = context?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater;
        inflater.inflate(R.layout.view_text_alignment_selector, this);

        tl = findViewById(R.id.tl) as ImageView
        tc = findViewById(R.id.tc) as ImageView
        tr = findViewById(R.id.tr) as ImageView
        cl = findViewById(R.id.cl) as ImageView
        c = findViewById(R.id.c) as ImageView
        cr = findViewById(R.id.cr) as ImageView
        bl = findViewById(R.id.bl) as ImageView
        bc = findViewById(R.id.bc) as ImageView
        br = findViewById(R.id.br) as ImageView

        views = listOf(tl, tc, tr, cl, c, cr, bl, bc, br)

        for (view in views) {
            view?.setOnClickListener() {
                if (selected.contains(it.tag)) {
                    selected.remove(it.tag)
                } else {
                    selected.add(it.tag as String)
                }
                selectedChanged()
            }
        }
    }

    private fun selectedChanged() {
        listener?.onSelectionChanged(selected)
        syncState()
    }

    private fun syncState() {
        for (iv in views) {
            if (iv != null) {
                setSelected(iv, selected.contains(iv.tag))
            }
        }
    }

    private fun setSelected(iv: ImageView, isSelected: Boolean) {
        iv.setBackgroundColor(if (isSelected) selectedBgColor else deselectedBgColor)
        iv.setColorFilter(if (isSelected) selectedIconColor else deslectedIconColor, PorterDuff.Mode.SRC_ATOP)
    }


}