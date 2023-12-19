package bmta.sem.model

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface EventDao {
    @Query("SELECT * FROM Events")
    fun getAll(): LiveData<List<Events>>

    @Query("SELECT * FROM Events WHERE id = :id")
    fun getById(id: Long): Events

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(events: Events): Long

    @Delete
    fun delete(events: Events)
}