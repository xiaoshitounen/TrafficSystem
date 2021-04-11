package swu.xl.trafficsystem.util

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import swu.xl.trafficsystem.base.BaseActivity

object PermissionUtil {
    const val location_code = 100
    /**
     * 定位权限
     * ACCESS_COARSE_LOCATION：GPS定位
     * ACCESS_FINE_LOCATION：Wifi或者移动基站定位
     */
    private val location = arrayOf<String>(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    fun checkLocation(context: Context): Boolean {
        location.forEach {
            if (ActivityCompat.checkSelfPermission(context, it) != PackageManager.PERMISSION_GRANTED) {
                return false
            }
        }
        return true
    }

    fun requestLocation(activity: Activity) {
        ActivityCompat.requestPermissions(activity, location, location_code)
    }
}