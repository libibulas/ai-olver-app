package com.aisolver.app

import android.content.Context
import android.graphics.*
import android.view.*
import android.widget.FrameLayout
import androidx.core.content.ContextCompat

class RegionSelectorOverlay(private val context: Context) : FrameLayout(context) {

    private var rect = RectF(200f, 200f, 600f, 400f) // 初始选区
    private var isDragging = false
    private var lastX = 0f
    private var lastY = 0f
    private var onSelectListener: ((Rect) -> Unit)? = null

    private val borderPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.STROKE
        strokeWidth = 4f
        color = Color.BLUE
    }

    private val maskPaint = Paint().apply {
        color = Color.argb(100, 0, 0, 0)
    }

    init {
        setWillNotDraw(false)
        setBackgroundColor(Color.TRANSPARENT)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        // 绘制半透明蒙层
        canvas.drawRect(0f, 0f, width.toFloat(), height.toFloat(), maskPaint)
        // 清除选区部分
        canvas.saveLayerAlpha(0f, 0f, width.toFloat(), height.toFloat(), 255, Canvas.ALL_SAVE_FLAG)
        canvas.drawRect(rect, Paint().apply { xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR) })
        canvas.restore()
        // 绘制边框
        canvas.drawRect(rect, border
