package swu.xl.trafficsystem.store

import android.app.Application
import android.content.Context
import android.content.SharedPreferences

object TrafficSystemStore {
    const val KEY_MAP_TYPE = "traffic_map_type"
    const val KEY_MAP_INDOOR = "traffic_map_indoor"
    const val KEY_MAP_TRAFFIC = "traffic_map_traffic"
    const val KEY_MAP_ZOOM = "traffic_map_zoom"
    const val KEY_MAP_COMPASS = "traffic_map_compass"
    const val KEY_MAP_LOCATION = "traffic_map_location"
    const val KEY_MAP_SCALE = "traffic_map_scale"
    const val KEY_MAP_LOGO_POSITION = "traffic_map_logo_position"

    private lateinit var sp: SharedPreferences

    fun init(application: Application) {
        sp = application.getSharedPreferences("traffic", Context.MODE_PRIVATE)
    }

    fun save(key: String, value: Boolean) {
        sp.edit().putBoolean(key, value).apply()
    }

    fun save(key: String, value: String) {
        sp.edit().putString(key, value).apply()
    }

    fun save(key: String, value: Int) {
        sp.edit().putInt(key, value).apply()
    }

    fun save(key: String, value: Long) {
        sp.edit().putLong(key, value).apply()
    }

    fun getBoolean(key: String, defaultValue: Boolean = false) = sp.getBoolean(key, defaultValue) fun getString(key: String, defaultValue: String = "") = sp.getString(key, defaultValue)!!

    fun getLong(key: String, default: Long) = sp.getLong(key, default)

    fun getInt(key: String, default: Int = 0) = sp.getInt(key, default)

    fun hasKey(key: String) = sp.contains(key)
}