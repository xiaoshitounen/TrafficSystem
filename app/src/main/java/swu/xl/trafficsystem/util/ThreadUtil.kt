package swu.xl.trafficsystem.util

import android.os.Handler
import android.os.Looper

object ThreadUtil {
    val handler = Handler(Looper.getMainLooper())

    inline fun runOnUiThread(crossinline block: () -> Unit) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            block()
        } else {
            handler.post {
                block()
            }
        }
    }

    fun postDelay(runnable: Runnable, delayMillis: Long) {
        handler.postDelayed(runnable, delayMillis)
    }
}