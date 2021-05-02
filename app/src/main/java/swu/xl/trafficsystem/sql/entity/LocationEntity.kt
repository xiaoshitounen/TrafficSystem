package swu.xl.trafficsystem.sql.entity

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "location",
    indices = [Index(value = ["type", "uid"], unique = true)]
)
class LocationEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var latitude: Double,
    var longitude: Double,
    var name: String,
    //ROUTE_POINT_HOME: 家
    //ROUTE_POINT_COMPANY: 公司
    var type: Int,
    var uid: Int
)