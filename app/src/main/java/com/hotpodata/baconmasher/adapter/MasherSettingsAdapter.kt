package com.hotpodata.baconmasher.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.Typeface
import android.support.v7.app.AlertDialog
import android.support.v7.widget.RecyclerView
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.EditText
import android.widget.LinearLayout
import com.google.android.gms.analytics.HitBuilders
import com.hotpodata.baconmasher.AnalyticsMaster
import com.hotpodata.baconmasher.MashMaster
import com.hotpodata.baconmasher.R
import com.hotpodata.baconmasher.adapter.viewholder.*
import com.hotpodata.baconmasher.data.ActiveStringManager
import com.hotpodata.baconmasher.data.TypefaceCache
import com.hotpodata.baconmasher.view.TextAlignmentSelector
import timber.log.Timber
import java.util.*

/**
 * Created by jdrotos on 11/7/15.
 */
class MasherSettingsAdapter(ctx: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val ROW_TYPE_HEADER = 0
    private val ROW_TYPE_DIV = 1
    private val ROW_TYPE_DIV_INSET = 2
    private val ROW_TYPE_SELECTABLE = 3
    private val ROW_TYPE_SELECTABLE_FONT = 4
    private val ROW_TYPE_ADD_SUBREDDIT = 5
    private val ROW_TYPE_TEXT_ALIGNMENT_SELECTOR = 6


    private var mRows: List<Any>

    private var mPrimColor: Int
    private var mAccentColor: Int
    private var mInactiveColor: Int
    private var mContext: Context

    init {
        mRows = ArrayList()
        mPrimColor = ctx.resources.getColor(R.color.colorPrimary)
        mAccentColor = ctx.resources.getColor(R.color.colorAccent)
        mContext = ctx
        mInactiveColor = mContext.resources.getColor(R.color.material_grey)
        mRows = buildRows()
    }

    public fun setRows(rows: List<Any>) {
        mRows = rows
        notifyDataSetChanged()
    }

    public fun syncWithMashMaster() {
        setRows(buildRows())
    }

    fun buildRows(): List<Any> {
        var rows = ArrayList<Any>()
        rows.add(Heading(mContext.resources.getString(R.string.text_alignment), mAccentColor))
        rows.add(TextAlignmentSelectorData())

        rows.add(Div(false))
        rows.add(Heading(mContext.resources.getString(R.string.image_subreddits), mAccentColor))
        var allImageSubreddits = MashMaster.imageReddits?.all
        Collections.sort(allImageSubreddits)
        for (subreddit in allImageSubreddits!!) {
            rows.add(Selectable(subreddit, subreddit, MashMaster.imageReddits?.isActive(subreddit)!!, SelectableType.IMAGE))
            rows.add(Div(true))
        }
        rows.add(AddSubreddit(SelectableType.IMAGE))

        rows.add(Div(false))
        rows.add(Heading(mContext.resources.getString(R.string.comment_subreddits), mAccentColor))
        var allCommentSubreddits = MashMaster.commentReddits?.all
        Collections.sort(allCommentSubreddits)
        for (subreddit in allCommentSubreddits!!) {
            rows.add(Selectable(subreddit, subreddit, MashMaster.commentReddits?.isActive(subreddit)!!, SelectableType.COMMENT))
            rows.add(Div(true))
        }
        rows.add(AddSubreddit(SelectableType.COMMENT))

        rows.add(Div(false))
        rows.add(Heading(mContext.resources.getString(R.string.fonts), mAccentColor))
        var allFonts = MashMaster.typefaces?.all
        Collections.sort(allFonts)
        for (font in allFonts!!) {
            rows.add(Selectable(font.removeSuffix(".ttf").replace("_", " ").replace("-", " "), font, MashMaster.typefaces?.isActive(font)!!, SelectableType.FONT))
            rows.add(Div(true))
        }
        //Remove unused div
        rows.removeAt(rows.size - 1)
        return rows
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder? {
        val inflater = LayoutInflater.from(parent.context)
        return when (viewType) {
            ROW_TYPE_HEADER -> {
                val v = inflater.inflate(R.layout.row_sidebar_section_header, parent, false)
                SideBarSectionHeaderViewHolder(v)
            }
            ROW_TYPE_DIV, ROW_TYPE_DIV_INSET -> {
                val v = inflater.inflate(R.layout.row_div, parent, false)
                RowDivViewHolder(v)
            }
            ROW_TYPE_SELECTABLE, ROW_TYPE_SELECTABLE_FONT -> {
                val v = inflater.inflate(R.layout.row_checkbox, parent, false)
                RowCheckboxViewHolder(v)
            }
            ROW_TYPE_ADD_SUBREDDIT -> {
                val v = inflater.inflate(R.layout.row_image_one_line, parent, false)
                RowSelectableViewHolder(v)
            }
            ROW_TYPE_TEXT_ALIGNMENT_SELECTOR -> {
                val v = inflater.inflate(R.layout.row_text_alignment_selector, parent, false)
                RowTextAlignmentSelectorViewHolder(v)
            }
            else -> null
        }
    }

    @Suppress("DEPRECATION")
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val type = getItemViewType(position)
        val objData = mRows[position]
        when (type) {
            ROW_TYPE_HEADER -> {
                val vh = holder as SideBarSectionHeaderViewHolder
                val data = objData as Heading
                vh.mTitleTv.text = data.text
                vh.mTitleTv.setTextColor(data.color)
            }
            ROW_TYPE_DIV_INSET, ROW_TYPE_DIV -> {
                val vh = holder as RowDivViewHolder
                val data = objData as Div
                if (data.isInset) {
                    vh.mSpacer.visibility = View.VISIBLE
                } else {
                    vh.mSpacer.visibility = View.GONE
                }
            }
            ROW_TYPE_ADD_SUBREDDIT -> {
                val vh = holder as RowSelectableViewHolder
                val data = objData as AddSubreddit
                vh.mTextOne.text = mContext.resources.getText(R.string.add_subreddit)
                vh.mIcon.setImageResource(R.drawable.ic_action_add)

                vh.itemView.setOnClickListener() {
                    var builder = AlertDialog.Builder(mContext)
                    with(builder) {
                        setTitle(R.string.add_subreddit)
                        var edittext = EditText(mContext);
                        edittext.hint = mContext.resources.getString(R.string.add_subreddit_hint)
                        var lp = LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT);
                        lp.leftMargin = mContext.resources.getDimensionPixelSize(R.dimen.keyline_one)
                        lp.rightMargin = lp.leftMargin
                        edittext.layoutParams = lp
                        builder.setView(edittext)

                        builder.setCancelable(true)
                        builder.setPositiveButton(R.string.add) {
                            dialogInterface, i ->
                            when (data.type) {
                                SelectableType.IMAGE -> {
                                    MashMaster.imageReddits?.add(edittext.text.toString().trim(), true)
                                }
                                SelectableType.COMMENT -> {
                                    MashMaster.commentReddits?.add(edittext.text.toString().trim(), true)
                                }
                            }
                            syncWithMashMaster()
                            try {
                                AnalyticsMaster.getTracker(mContext).send(HitBuilders.EventBuilder()
                                        .setCategory(AnalyticsMaster.CATEGORY_ACTION)
                                        .setAction(AnalyticsMaster.ACTION_ADD_SETTINGS_REDDIT)
                                        .build());
                            } catch(ex: Exception) {
                                Timber.e(ex, "Analytics Exception");
                            }
                        }
                        builder.setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
                        builder.create().show()
                    }
                    true;
                }
            }
            ROW_TYPE_SELECTABLE, ROW_TYPE_SELECTABLE_FONT -> {
                val vh = holder as RowCheckboxViewHolder
                val data = objData as Selectable
                vh.itemView.setOnClickListener() {
                    vh.mCheckBox.isChecked = !vh.mCheckBox.isChecked
                }
                vh.mCheckBox.setOnCheckedChangeListener() { cb: CompoundButton?, b: Boolean ->
                    when (data.type) {
                        SelectableType.IMAGE -> {
                            if (b != MashMaster.imageReddits?.isActive(data.key)) {
                                MashMaster.imageReddits?.setActive(data.key, b)
                                syncWithMashMaster()
                            }
                        }
                        SelectableType.COMMENT -> {
                            if (b != MashMaster.commentReddits?.isActive(data.key)) {
                                MashMaster.commentReddits?.setActive(data.key, b)
                                syncWithMashMaster()
                            }
                        }
                        SelectableType.FONT -> {
                            if (b != MashMaster.typefaces?.isActive(data.key)) {
                                MashMaster.typefaces?.setActive(data.key, b)
                                syncWithMashMaster()
                            }
                        }
                    }
                }
                if (data.type == SelectableType.COMMENT || data.type == SelectableType.IMAGE) {
                    vh.itemView.setOnLongClickListener {
                        var builder = AlertDialog.Builder(mContext)
                        with(builder) {
                            when (data.type) {
                                SelectableType.IMAGE -> {
                                    setMessage(R.string.remove_image_subreddit_confirm)
                                }
                                SelectableType.COMMENT -> {
                                    setMessage(R.string.remove_comment_subreddit_confirm)
                                }
                            }

                            builder.setCancelable(true)
                            builder.setPositiveButton(R.string.remove) {
                                dialogInterface, i ->
                                when (data.type) {
                                    SelectableType.IMAGE -> {
                                        MashMaster.imageReddits?.remove(data.key)
                                    }
                                    SelectableType.COMMENT -> {
                                        MashMaster.commentReddits?.remove(data.key)
                                    }
                                }
                                syncWithMashMaster()
                                try {
                                    AnalyticsMaster.getTracker(mContext).send(HitBuilders.EventBuilder()
                                            .setCategory(AnalyticsMaster.CATEGORY_ACTION)
                                            .setAction(AnalyticsMaster.ACTION_REMOVE_SETTINGS_REDDIT)
                                            .build());
                                } catch(ex: Exception) {
                                    Timber.e(ex, "Analytics Exception");
                                }
                            }
                            builder.setNegativeButton(R.string.cancel) { dialogInterface, i -> dialogInterface.cancel() }
                            builder.create().show()
                        }
                        true;
                    }
                } else if ( data.type == SelectableType.FONT) {
                    vh.mTextOne.typeface = TypefaceCache.getTypeFace(mContext, data.key)
                }
                vh.mTextOne.text = data.title
                vh.mCheckBox.isChecked = data.selected
            }
            ROW_TYPE_TEXT_ALIGNMENT_SELECTOR -> {
                val vh = holder as RowTextAlignmentSelectorViewHolder
                val data = objData as TextAlignmentSelectorData
                vh.mTextAlignmentSelector.selectedBgColor = mAccentColor
                vh.mTextAlignmentSelector.selectedIconColor = Color.WHITE
                vh.mTextAlignmentSelector.selected = MashMaster.textgravity?.active!!
                vh.mTextAlignmentSelector.listener = object : TextAlignmentSelector.TextAlignmentListener {
                    override fun onSelectionChanged(selectedAlignmentCodes: Set<String>) {
                        var allgravs = MashMaster.textgravity?.all
                        for (grav in allgravs!!) {
                            MashMaster.textgravity?.setActive(grav, selectedAlignmentCodes.contains(grav))
                        }
                    }

                }

            }
        }
    }

    override fun getItemCount(): Int {
        return mRows.size
    }

    override fun getItemViewType(position: Int): Int {
        val data = mRows[position]
        return when (data) {
            is Heading -> ROW_TYPE_HEADER
            is Div ->
                if (data.isInset) {
                    ROW_TYPE_DIV_INSET
                } else {
                    ROW_TYPE_DIV
                }
            is Selectable ->
                if (data.type == SelectableType.FONT) {
                    ROW_TYPE_SELECTABLE_FONT
                } else {
                    ROW_TYPE_SELECTABLE
                }
            is AddSubreddit -> ROW_TYPE_ADD_SUBREDDIT
            is TextAlignmentSelectorData -> ROW_TYPE_TEXT_ALIGNMENT_SELECTOR
            else -> super.getItemViewType(position)
        }
    }

    open class SettingsRow {
        var title: String? = null
            private set
        var subTitle: String? = null
            private set
        var onClickListener: View.OnClickListener? = null
            private set
        var iconResId = -1
            private set

        constructor(title: String, subTitle: String, onClickListener: View.OnClickListener) {
            this.title = title
            this.subTitle = subTitle
            this.onClickListener = onClickListener
        }

        constructor(title: String, subTitle: String, onClickListener: View.OnClickListener, iconResId: Int) {
            this.title = title
            this.subTitle = subTitle
            this.onClickListener = onClickListener
            this.iconResId = iconResId
        }
    }

    class Div(val isInset: Boolean)

    class Heading(val text: String, val color: Int)

    class TextAlignmentSelectorData()

    enum class SelectableType {
        IMAGE, COMMENT, FONT
    }

    class Selectable(val title: String, val key: String, val selected: Boolean, val type: SelectableType)

    class AddSubreddit(val type: SelectableType)
}