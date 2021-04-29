package swu.xl.trafficsystem.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import swu.xl.trafficsystem.sql.dao.HistoryDao
import swu.xl.trafficsystem.sql.entity.HistoryEntity

@Database(entities = [HistoryEntity::class], version = 1)
abstract class TrafficSystemRoomBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao

    companion object {
        @Volatile
        private var INSTANCE: TrafficSystemRoomBase? = null

        fun getRoomBase(context: Context): TrafficSystemRoomBase {
            return INSTANCE ?: Room.databaseBuilder(
                context,
                TrafficSystemRoomBase::class.java,
                "traffic.system.db"
            ).fallbackToDestructiveMigration().build().also {
                INSTANCE = it
            }
        }
    }
}