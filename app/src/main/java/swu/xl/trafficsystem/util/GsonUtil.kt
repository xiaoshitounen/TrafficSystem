package swu.xl.trafficsystem.util

import com.google.gson.Gson

object GsonUtil {
    private val gson = Gson()

    fun getInstance() = gson
}