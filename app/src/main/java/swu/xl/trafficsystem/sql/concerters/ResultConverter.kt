package swu.xl.trafficsystem.sql.concerters

import androidx.room.TypeConverter
import com.amap.api.services.route.BusRouteResult
import com.google.gson.reflect.TypeToken
import swu.xl.trafficsystem.util.GsonUtil

class ResultConverter {
    @TypeConverter
    fun stringToResult(value: String): BusRouteResult {
        val type = object : TypeToken<BusRouteResult>(){}.type
        return GsonUtil.getInstance().fromJson(value,type)
    }

    @TypeConverter
    fun resultToString(result: BusRouteResult): String {
        return GsonUtil.getInstance().toJson(result)
    }
}