package swu.xl.trafficsystem.sql.entity

import androidx.room.*
import com.amap.api.services.help.Tip
import swu.xl.trafficsystem.sql.concerters.TipConverter

@Entity(
    tableName = "history",
    indices = [Index(value = ["tip"], unique = true)]
)
@TypeConverters(TipConverter::class)
data class HistoryEntity (
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var tip: Tip? = null,
    var uid: Int
)