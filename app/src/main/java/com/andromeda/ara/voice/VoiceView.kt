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
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import com.andromeda.ara.R


/**
 * Created by yugy on 2014/3/23.
 */
class VoiceView : View {
    private var mNormalBitmap: Bitmap? = null
    private var mPressedBitmap: Bitmap? = null
    private var mRecordingBitmap: Bitmap? = null
    private var mPaint: Paint? = null
    private val mAnimatorSet = AnimatorSet()
    private var mOnRecordListener: OnRecordListener? = null
    private var mState = STATE_NORMAL
    private var mIsRecording = false
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
        mNormalBitmap = BitmapFactory.decodeResource(resources, R.drawable.mic)
        mPressedBitmap = BitmapFactory.decodeResource(resources, R.drawable.mic)
        mRecordingBitmap = BitmapFactory.decodeResource(resources, R.drawable.mic)
        mPaint = Paint()
        mPaint!!.isAntiAlias = true
        mPaint!!.color = Color.argb(255, 219, 219, 219)
        mMinRadius = dp2px(context, 68) / 2f
        mCurrentRadius = mMinRadius
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mMaxRadius = Math.min(w, h) / 2.toFloat()
        Log.d(TAG, "MaxRadius: $mMaxRadius")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = canvas.width
        val height = canvas.height
        if (mCurrentRadius > mMinRadius) {
            canvas.drawCircle(width / 2.toFloat(), height / 2.toFloat(), mCurrentRadius, mPaint!!)
        }
        when (mState) {
            STATE_NORMAL -> canvas.drawBitmap(mNormalBitmap!!, width / 2 - mMinRadius, height / 2 - mMinRadius, mPaint)
            STATE_PRESSED -> canvas.drawBitmap(mPressedBitmap!!, width / 2 - mMinRadius, height / 2 - mMinRadius, mPaint)
            STATE_RECORDING -> canvas.drawBitmap(mRecordingBitmap!!, width / 2 - mMinRadius, height / 2 - mMinRadius, mPaint)
        }
    }

    fun animateRadius(radius: Float) {
        var radius = radius
        if (radius <= mCurrentRadius) {
            return
        }
        if (radius > mMaxRadius) {
            radius = mMaxRadius
        } else if (radius < mMinRadius) {
            radius = mMinRadius
        }
        if (radius == mCurrentRadius) {
            return
        }
        if (mAnimatorSet.isRunning) {
            mAnimatorSet.cancel()
        }
        mAnimatorSet.playSequentially(
                ObjectAnimator.ofFloat(this, "CurrentRadius", currentRadius, radius).setDuration(50),
                ObjectAnimator.ofFloat(this, "CurrentRadius", radius, mMinRadius).setDuration(600)
        )
        mAnimatorSet.start()
    }

    var currentRadius: Float
        get() = mCurrentRadius
        set(currentRadius) {
            mCurrentRadius = currentRadius
            invalidate()
        }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                Log.d(TAG, "ACTION_DOWN")
                mState = STATE_PRESSED
                invalidate()
                true
            }
            MotionEvent.ACTION_UP -> {
                Log.d(TAG, "ACTION_UP")
                if (mIsRecording) {
                    mState = STATE_NORMAL
                    if (mOnRecordListener != null) {
                        mOnRecordListener!!.onRecordFinish()
                    }
                } else {
                    mState = STATE_RECORDING
                    if (mOnRecordListener != null) {
                        mOnRecordListener!!.onRecordStart()
                    }
                }
                mIsRecording = !mIsRecording
                invalidate()
                true
            }
            else -> super.onTouchEvent(event)
        }
    }

    fun setOnRecordListener(onRecordListener: OnRecordListener?) {
        mOnRecordListener = onRecordListener
    }

    interface OnRecordListener {
        fun onRecordStart()
        fun onRecordFinish()
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