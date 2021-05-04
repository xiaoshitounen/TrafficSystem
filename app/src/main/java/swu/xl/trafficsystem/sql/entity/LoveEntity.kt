package swu.xl.trafficsystem.sql.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import swu.xl.trafficsystem.adapter.CustomBusStep
import swu.xl.trafficsystem.sql.concerters.StepConverter

@Entity(tableName = "love")
@TypeConverters(StepConverter::class)
class LoveEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var start: String,
    var target: String,
    var time: String,
    var walkDistance: String,
    var steps: List<CustomBusStep>,
    var uid: Int
)