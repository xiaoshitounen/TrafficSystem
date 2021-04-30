package swu.xl.trafficsystem.sql.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import swu.xl.trafficsystem.sql.entity.UserEntity

@Dao
interface UserDao {
    @Insert
    fun insert(user: UserEntity)

    @Update
    fun update(user: UserEntity)

    @Query("SELECT * FROM user")
    fun queryAll(): List<UserEntity>
}