package bmta.sem.model

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Events::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun eventDao(): EventDao

    companion object {
        @Volatile
        var INSTANCE: AppDatabase? = null

        // Singleton instance of the database
        fun getDatabaseInstance(context: Context): AppDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            // Synchronized block to make sure that
            // only one instance of the database is created
            synchronized(this) {
                val roomDatabaseInstance =
                    Room.databaseBuilder(context, AppDatabase::class.java, "Events")
                        .allowMainThreadQueries().build()
                INSTANCE = roomDatabaseInstance
                return roomDatabaseInstance
            }
        }

    }
}