package swu.xl.trafficsystem.sql

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import swu.xl.trafficsystem.sql.dao.HistoryDao
import swu.xl.trafficsystem.sql.dao.LocationDao
import swu.xl.trafficsystem.sql.dao.UserDao
import swu.xl.trafficsystem.sql.entity.HistoryEntity
import swu.xl.trafficsystem.sql.entity.LocationEntity
import swu.xl.trafficsystem.sql.entity.UserEntity

@Database(entities = [HistoryEntity::class, UserEntity::class, LocationEntity::class], version = 5)
abstract class TrafficSystemRoomBase : RoomDatabase() {
    abstract fun historyDao(): HistoryDao
    abstract fun userDao(): UserDao
    abstract fun locationDao(): LocationDao

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