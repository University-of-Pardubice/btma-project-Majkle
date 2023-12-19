package bmta.sem.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Events(
    @PrimaryKey(autoGenerate = true) var id: Long? = null,
    var name: String,
    var description: String?,
    var date: Long = 0,
    var notification: Boolean = false
)
