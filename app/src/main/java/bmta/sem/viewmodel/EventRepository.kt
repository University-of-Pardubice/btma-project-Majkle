package bmta.sem.viewmodel

import androidx.lifecycle.LiveData
import bmta.sem.model.EventDao
import bmta.sem.model.Events


class EventRepository(val dao: EventDao) {
    fun getAll(): LiveData<List<Events>> {
        return dao.getAll()
    }

    fun getById(id: Long): Events {
        return dao.getById(id)
    }

    fun insertContact(events: Events): Long {
        return dao.insert(events)
    }

    fun deleteContact(contact: Events) {
        dao.delete(contact)
    }
}
