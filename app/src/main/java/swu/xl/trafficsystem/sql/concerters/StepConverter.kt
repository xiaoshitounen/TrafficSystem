package swu.xl.trafficsystem.sql.concerters

import androidx.room.TypeConverter
import com.google.gson.reflect.TypeToken
import swu.xl.trafficsystem.adapter.CustomBusStep
import swu.xl.trafficsystem.util.GsonUtil

class StepConverter {
    @TypeConverter
    fun stringToStep(value: String): List<CustomBusStep> {
        val type = object : TypeToken<List<CustomBusStep>>(){}.type
        return GsonUtil.getInstance().fromJson(value,type)
    }

    @TypeConverter
    fun stepToString(point: List<CustomBusStep>): String {
        return GsonUtil.getInstance().toJson(point)
    }
}