package swu.xl.trafficsystem.sql.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import swu.xl.trafficsystem.sql.entity.LoveEntity

@Dao
interface LoveDao {
    @Insert
    fun insert(love: LoveEntity)

    @Query("DELETE FROM love")
    fun deleteAll()

    @Delete
    fun delete(love: LoveEntity)

    @Query("SELECT * FROM love WHERE uid = :user")
    fun queryAll(user: Int): List<LoveEntity>
}