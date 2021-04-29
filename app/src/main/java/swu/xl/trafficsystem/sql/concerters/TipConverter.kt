package swu.xl.trafficsystem.sql.concerters

import androidx.room.TypeConverter
import com.amap.api.services.help.Tip
import com.google.gson.reflect.TypeToken
import swu.xl.trafficsystem.util.GsonUtil

class TipConverter {
    @TypeConverter
    fun stringToTip(value: String): Tip {
        val type = object : TypeToken<Tip>(){}.type
        return GsonUtil.getInstance().fromJson(value,type)
    }

    @TypeConverter
    fun tipToString(tip: Tip): String {
        return GsonUtil.getInstance().toJson(tip)
    }
}