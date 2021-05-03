package swu.xl.trafficsystem.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.amap.api.services.route.BusPath
import com.amap.api.services.route.BusRouteResult
import swu.xl.trafficsystem.sql.concerters.PathConverter
import swu.xl.trafficsystem.sql.concerters.ResultConverter

@Entity(tableName = "love")
@TypeConverters(PathConverter::class, ResultConverter::class)
class LoveEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var start: String,
    var end: String,
    var time: String,
    var path: BusPath,
    var result: BusRouteResult,
    var uid: Int
)