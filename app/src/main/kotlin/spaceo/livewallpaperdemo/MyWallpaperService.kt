package spaceo.livewallpaperdemo

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.os.Handler
import android.preference.PreferenceManager
import android.service.wallpaper.WallpaperService
import android.view.MotionEvent
import android.view.SurfaceHolder
import java.util.*


/**
 * Created by sotsys055 on 3/5/17.
 */

class MyWallpaperService : WallpaperService() {

    override fun onCreateEngine(): WallpaperService.Engine {
        return MyWallpaperEngine()
    }

    private inner class MyWallpaperEngine : WallpaperService.Engine() {
        private val handler = Handler()
        private val drawRunner = Runnable { draw() }
        private val circles: MutableList<MyPoint>
        private val paint = Paint()
        private var width: Int = 0
        internal var height: Int = 0
        private var visible = true
        private val maxNumber: Int
        private val touchEnabled: Boolean

        init {
            val prefs = PreferenceManager
                .getDefaultSharedPreferences(this@MyWallpaperService)
            maxNumber = Integer
                .valueOf(prefs.getString(resources.getString(R.string.lable_number_of_circles), "4")!!)
            touchEnabled = prefs.getBoolean("touch", false)
            circles = ArrayList()
            paint.isAntiAlias = true
            paint.color = Color.WHITE
            paint.style = Paint.Style.STROKE
            paint.strokeJoin = Paint.Join.ROUND
            paint.strokeWidth = 10f
            handler.post(drawRunner)
        }

        override fun onVisibilityChanged(visible: Boolean) {
            this.visible = visible
            if (visible) {
                handler.post(drawRunner)
            } else {
                handler.removeCallbacks(drawRunner)
            }
        }

        override fun onSurfaceDestroyed(holder: SurfaceHolder) {
            super.onSurfaceDestroyed(holder)
            this.visible = false
            handler.removeCallbacks(drawRunner)
        }

        override fun onSurfaceChanged(
            holder: SurfaceHolder, format: Int,
            width: Int, height: Int
        ) {
            this.width = width
            this.height = height
            super.onSurfaceChanged(holder, format, width, height)
        }

        override fun onTouchEvent(event: MotionEvent) {
            if (touchEnabled) {

                val x = event.x
                val y = event.y
                val holder = surfaceHolder
                var canvas: Canvas? = null

                canvas = holder.lockCanvas()
                if (canvas != null) {
                    canvas.drawColor(Color.BLACK)
                    circles.clear()
                    circles.add(MyPoint((circles.size + 1).toString(), x, y))
                    drawCircles(canvas, circles)

                }

                if (canvas != null)
                    holder.unlockCanvasAndPost(canvas)

                super.onTouchEvent(event)
            }
        }

        private fun draw() {
            val holder = surfaceHolder
            var canvas: Canvas? = null

            canvas = holder.lockCanvas()
            if (canvas != null) {
                if (circles.size >= maxNumber) {
                    circles.clear()
                }
                val x = (width * Math.random()).toInt()
                val y = (height * Math.random()).toInt()
                circles.add(
                    MyPoint(
                        (circles.size + 1).toString(),
                        x.toFloat(), y.toFloat()
                    )
                )
                drawCircles(canvas, circles)
            }

            if (canvas != null)
                holder.unlockCanvasAndPost(canvas)

            handler.removeCallbacks(drawRunner)
            if (visible) {
                handler.postDelayed(drawRunner, 5000)
            }
        }

        // Surface view requires that all elements are drawn completely
        private fun drawCircles(canvas: Canvas, circles: List<MyPoint>) {
            canvas.drawColor(Color.BLACK)
            for (point in circles) {
                canvas.drawCircle(point.x, point.y, 20.0f, paint)
            }
        }
    }


}