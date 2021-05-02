package swu.xl.trafficsystem.sql.dao

import androidx.room.*
import swu.xl.trafficsystem.sql.entity.LocationEntity

@Dao
interface LocationDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(location: LocationEntity)

    @Query("SELECT * FROM location WHERE uid = :user")
    fun queryAll(user: Int): List<LocationEntity>
}