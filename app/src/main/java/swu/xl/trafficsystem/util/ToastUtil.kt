package swu.xl.trafficsystem.util

import android.widget.Toast
import swu.xl.trafficsystem.application.MyApplication

object ToastUtil {
    fun toast(msg: String) {
        ThreadUtil.runOnUiThread {
            Toast.makeText(MyApplication.getApplication(), msg, Toast.LENGTH_SHORT).show()
        }
    }
}