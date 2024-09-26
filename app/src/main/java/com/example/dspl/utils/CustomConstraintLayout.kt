package com.example.dspl.utils

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Handler
import android.os.Looper
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.widget.RelativeLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginStart
import androidx.core.view.marginTop
import com.example.dspl.R


class CustomConstraintLayout(context: Context, attrs: AttributeSet?) : ConstraintLayout(context, attrs) {

    private var child: View? = null
    var bg_marginTop = 6
        private set
    var bg_marginStart = 6
        private set
    var bg_radius = 3
        private set
    var fg_strokeWidth = 3
        private set
    var pressable = true
        set
    var noEffect = false
        set
    var handlerPress: Handler? = null

    var bg_shadowColor = Color.BLACK
        set(value) {
            field = value
            applyBackground()
        }
    var fg_color = Color.WHITE
        set(value) {
            field = value
            applyForeground()
        }
    var fg_strokeColor = Color.BLACK

    private val fg = GradientDrawable()
    private val bg = GradientDrawable()

    private var layout: ConstraintLayout
    private var bgView: View?

    private var onClick: () -> Unit = {}

    init {
        val a = context.obtainStyledAttributes(
            attrs,
            R.styleable.CustomConstraintLayout, 0, 0
        )
        bg_marginTop = a.getDimensionPixelSize(R.styleable.CustomConstraintLayout_bg_marginTop, 6)
        bg_marginStart = a.getDimensionPixelSize(R.styleable.CustomConstraintLayout_bg_marginStart, 6)
        bg_radius = a.getDimensionPixelSize(R.styleable.CustomConstraintLayout_bg_radius, 3)
        fg_strokeWidth = a.getDimensionPixelSize(R.styleable.CustomConstraintLayout_fg_strokeWidth, 3)
        bg_shadowColor = a.getColor(R.styleable.CustomConstraintLayout_bg_shadowColor, Color.BLACK)
        fg_color = a.getColor(R.styleable.CustomConstraintLayout_fg_color, Color.WHITE)
        fg_strokeColor = a.getColor(R.styleable.CustomConstraintLayout_fg_strokeColor, Color.BLACK)
        pressable = a.getBoolean(R.styleable.CustomConstraintLayout_pressable, true)
        noEffect = a.getBoolean(R.styleable.CustomConstraintLayout_noeffect, true)
        a.recycle()

        inflate(context, R.layout.custom_constraint_layout_view, this)
        layout = findViewById(R.id.layout)
        bgView = findViewById(R.id.bgView)

        layout.viewTreeObserver.addOnGlobalLayoutListener {
            applyBackground()
            fg.apply {
                shape = GradientDrawable.RECTANGLE
                cornerRadius = bg_radius.toFloat()
                setStroke(fg_strokeWidth, ColorStateList.valueOf(fg_strokeColor))
                color = ColorStateList.valueOf(fg_color)
            }
        }

        /*val constraintSet = ConstraintSet()
        constraintSet.clone(this)

        constraintSet.connect(
            bgView.id, ConstraintSet.START,
            ConstraintSet.PARENT_ID, ConstraintSet.START,
            bg_marginStart
        )
        constraintSet.connect(
            bgView.id, ConstraintSet.TOP,
            ConstraintSet.PARENT_ID, ConstraintSet.TOP,
            bg_marginTop
        )
        constraintSet.applyTo(this)*/
    }

    private fun applyBackground() {
        bg.apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = bg_radius.toFloat()
            color = ColorStateList.valueOf(bg_shadowColor)
        }
        bgView?.background = bg
    }

    private fun applyForeground() {
        fg.apply {
            shape = GradientDrawable.RECTANGLE
            cornerRadius = bg_radius.toFloat()
            setStroke(fg_strokeWidth, ColorStateList.valueOf(fg_strokeColor))
            color = ColorStateList.valueOf(fg_color)
        }
        invalidate()
    }


    @SuppressLint("ClickableViewAccessibility")
    override fun addView(child: View, index: Int, params: ViewGroup.LayoutParams) {
        if (child.id != R.id.layout) {
            var runnable = Runnable {
                if (pressable && !noEffect) {
                    child.translationX = 0f
                    child.translationY = 0f
                }
            }
            this.child = child
            child.background = fg
            child.post {
                val constraintSet = ConstraintSet()
                constraintSet.clone(this@CustomConstraintLayout)

                // Set constraints for the child view
                constraintSet.connect(
                    child.id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                )
                constraintSet.connect(
                    child.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                )
                constraintSet.connect(
                    child.id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END,
                    -bg_marginStart // Reduce the width by bg_marginStart
                )
                constraintSet.connect(
                    child.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                    -bg_marginTop // Reduce the height by bg_marginTop
                )
                constraintSet.setMargin(child.id, ConstraintSet.BOTTOM, bg_marginTop)
                constraintSet.setMargin(child.id, ConstraintSet.END, bg_marginStart)
                constraintSet.applyTo(this@CustomConstraintLayout)

                val constraintSetNew = ConstraintSet()
                constraintSetNew.clone(layout)
                constraintSetNew.connect(
                    layout.id,
                    ConstraintSet.START,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.START,
                )
                constraintSetNew.connect(
                    layout.id,
                    ConstraintSet.TOP,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.TOP,
                )
                constraintSetNew.connect(
                    layout.id,
                    ConstraintSet.END,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.END,
                )
                constraintSetNew.connect(
                    layout.id,
                    ConstraintSet.BOTTOM,
                    ConstraintSet.PARENT_ID,
                    ConstraintSet.BOTTOM,
                )
                constraintSetNew.setMargin(layout.id, ConstraintSet.TOP, bg_marginTop)
                constraintSetNew.setMargin(layout.id, ConstraintSet.START, bg_marginStart)
                constraintSetNew.applyTo(this@CustomConstraintLayout)
            }


            child.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        onTouchCustom?.invoke(true)
                        if (pressable) {
                            if(!noEffect) {
                                if (bg_marginStart == 0) {
                                    child.translationX =
                                        context.resources.getDimensionPixelOffset(com.intuit.sdp.R.dimen._2sdp)
                                            .toFloat()
                                    child.translationY =
                                        context.resources.getDimensionPixelOffset(com.intuit.sdp.R.dimen._2sdp)
                                            .toFloat()
                                } else {
                                    child.translationX = bg_marginStart.toFloat()
                                    child.translationY = bg_marginTop.toFloat()
                                }
                            }
                        } else {
                            return@setOnTouchListener true
                        }
                        handlerPress = Handler(Looper.getMainLooper())
                        handlerPress?.postDelayed(runnable, 300)
                    }
                    MotionEvent.ACTION_UP -> {
                        onTouchCustom?.invoke(false)
                        handlerPress?.removeCallbacks(runnable)
                        handlerPress = null
                        if (pressable) {
                            if(!noEffect) {
                                child.translationX = 0f
                                child.translationY = 0f
                            }
                        } else {
                            return@setOnTouchListener true
                        }
                        onClick.invoke()
                        performClick()
                    }
                }
                true
            }
        }
        super.addView(child, index, params)
    }

    fun setCustomClickListener(onClick: () -> Unit) {
        this.onClick = onClick
    }

    fun setBackgroundMargins(marginTop: Int, marginStart: Int) {
        bgView?.let {
            val params = RelativeLayout.LayoutParams(width, height)
            params.setMargins(marginTop, marginStart, 0, 0)
            layoutParams = params
        }
    }

    fun setMarginTop(margin: Int) {
        bg_marginTop = margin
    }

    fun setMarginStart(margin: Int) {
        bg_marginStart = margin
    }

    var onTouchCustom: ((isTouch: Boolean) -> Unit)? = null
}
