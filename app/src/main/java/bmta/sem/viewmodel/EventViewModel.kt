package bmta.sem.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import bmta.sem.model.AppDatabase
import bmta.sem.model.Events

class EventViewModel(application: Application) : AndroidViewModel(application) {
    val repository: EventRepository

    init {
        val dao = AppDatabase.getDatabaseInstance(application).eventDao()
        repository = EventRepository(dao)
    }

    fun getAll(): LiveData<List<Events>> = repository.getAll()

    fun getById(id: Long): Events = repository.getById(id)

    fun add(contact: Events): Long {
        return repository.insertContact(contact)
    }

}