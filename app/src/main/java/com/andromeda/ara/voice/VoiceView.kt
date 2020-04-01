/*
 * Copyright (c) 2020. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.andromeda.ara.voice

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.TypedValue
import com.andromeda.ara.R
import com.google.android.material.floatingactionbutton.FloatingActionButton


/**
 * Created by yugy on 2014/3/23.
 */
class VoiceView : FloatingActionButton {
    private var mNormalBitmap: Bitmap? = null
    private var mPressedBitmap: Bitmap? = null
    private var mRecordingBitmap: Bitmap? = null
    private var mPaint: Paint? = null
    private val mAnimatorSet = AnimatorSet()
    private var mMinRadius = 0f
    private var mMaxRadius = 0f
    private var mCurrentRadius = 0f

    constructor(context: Context?) : super(context) {
        init()
    }

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    private fun init() {
        mNormalBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_foreground)
        mPressedBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_foreground)
        mRecordingBitmap = BitmapFactory.decodeResource(resources, R.mipmap.ic_launcher_foreground)

        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.color = Color.argb(255, 219, 219, 219)
        mMinRadius = dp2px(context, 68) / 2f
        mCurrentRadius = mMinRadius
    }



    fun animateRadius(radius: Float) {
        var radius = radius
        if (radius == mCurrentRadius) {
            println("equal")
            return
        }
        if (mAnimatorSet.isRunning) {
            return
        }
        mAnimatorSet.playSequentially(
                ObjectAnimator.ofFloat(this, "CurrentRadius", mCurrentRadius, radius).setDuration(50))
        mAnimatorSet.start()
    }

    var currentRadius: Float
        get() = mCurrentRadius
        set(currentRadius) {
            mCurrentRadius = currentRadius
            invalidate()
            println("set")
            println(currentRadius)
        }

    companion object {
        private val TAG = VoiceView::class.java.name
        private const val STATE_NORMAL = 0
        private const val STATE_PRESSED = 1
        private const val STATE_RECORDING = 2
        @JvmStatic
        fun dp2px(context: Context, dp: Int): Int {
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp.toFloat(), context.resources.displayMetrics).toInt()
        }
    }
}