package swu.xl.trafficsystem.log

import android.util.Log

object TrafficSystemLogger {
    private const val TAG = "TrafficSystemLogger"

    fun d(msg: String) {
        Log.d(TAG, msg)
    }

    fun i(msg: String) {
        Log.i(TAG, msg)
    }

    fun w(msg: String) {
        Log.w(TAG, msg)
    }

    fun e(msg: String) {
        Log.e(TAG, msg)
    }

    fun e(msg: String, t: Throwable) {
        Log.e(TAG, msg, t)
    }
}