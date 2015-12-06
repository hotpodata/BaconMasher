package com.hotpodata.baconmasher.data

import android.view.Gravity

/**
 * Created by jdrotos on 12/1/15.
 */
object GravityStringer {
    //NOTE THAT THESE MATCH TAGS SET IN THE SELECTOR VIEW, SO CHANGING THEM REQUIRES WORK
    val TL = "TL"
    val TC = "TC"
    val TR = "TR"
    val CL = "CL"
    val C = "C"
    val CR = "CR"
    val BL = "BL"
    val BC = "BC"
    val BR = "BR"


    val strToGrav = hashMapOf(
            Pair(TL, Gravity.TOP or Gravity.LEFT),
            Pair(TC, Gravity.TOP or Gravity.CENTER_HORIZONTAL),
            Pair(TR, Gravity.TOP or Gravity.RIGHT),
            Pair(CL, Gravity.CENTER_VERTICAL or Gravity.LEFT),
            Pair(C, Gravity.CENTER),
            Pair(CR, Gravity.CENTER_VERTICAL or Gravity.RIGHT),
            Pair(BL, Gravity.BOTTOM or Gravity.LEFT),
            Pair(BC, Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL),
            Pair(BR, Gravity.BOTTOM or Gravity.RIGHT)
    )
    val gravToStr = hashMapOf(
            Pair(Gravity.TOP or Gravity.LEFT, TL),
            Pair(Gravity.TOP or Gravity.CENTER_HORIZONTAL, TC),
            Pair(Gravity.TOP or Gravity.RIGHT, TR),
            Pair(Gravity.CENTER_VERTICAL or Gravity.LEFT, CL),
            Pair(Gravity.CENTER, C),
            Pair(Gravity.CENTER_VERTICAL or Gravity.RIGHT, CR),
            Pair(Gravity.BOTTOM or Gravity.LEFT, BL),
            Pair(Gravity.BOTTOM or Gravity.CENTER_HORIZONTAL, BC),
            Pair(Gravity.BOTTOM or Gravity.RIGHT, BR)
    )
}