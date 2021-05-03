package swu.xl.trafficsystem.sql.concerters

import androidx.room.TypeConverter
import com.amap.api.services.route.BusPath
import com.google.gson.reflect.TypeToken
import swu.xl.trafficsystem.util.GsonUtil

class PathConverter {
    @TypeConverter
    fun stringToPath(value: String): BusPath {
        val type = object : TypeToken<BusPath>(){}.type
        return GsonUtil.getInstance().fromJson(value,type)
    }

    @TypeConverter
    fun pathToString(path: BusPath): String {
        return GsonUtil.getInstance().toJson(path)
    }
}