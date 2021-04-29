package swu.xl.trafficsystem.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import swu.xl.trafficsystem.sql.entity.HistoryEntity

@Dao
interface HistoryDao {
    @Insert
    fun insert(history: HistoryEntity)

    @Query("SELECT * FROM history WHERE uid = :user")
    fun queryAll(user: Int): List<HistoryEntity>

    @Query("DELETE FROM history WHERE uid = :user")
    fun deleteAll(user: Int)
}