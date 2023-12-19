package bmta.sem.viewmodel

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import bmta.sem.NotificationBroadcastReceiver
import bmta.sem.databinding.EventLayoutBinding
import bmta.sem.model.AppDatabase
import bmta.sem.model.Events
import bmta.sem.view.NewEvent
import java.text.SimpleDateFormat
import java.util.Locale

class EventAdapter(val context: Context, val list: List<Events>) :
    RecyclerView.Adapter<EventAdapter.ViewHolder>() {
    // Inner ViewHolder class
    class ViewHolder(val binding: EventLayoutBinding) : RecyclerView.ViewHolder(binding.root) {}

    // DAO instance to interact with the database
    private val dao = AppDatabase.getDatabaseInstance(context).eventDao()


    // function to inflate the layout for each contact and create a new ViewHolder instance
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            EventLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    // function to bind the data to the view elements of the ViewHolder
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, NewEvent::class.java)
            intent.putExtra("eventId", list[position].id)
            context.startActivity(intent)
        }

        holder.binding.tvName.text = list[position].name
        holder.binding.tvDescription.text = list[position].description
//        holder.binding.cbNotification.isChecked = list[position].notification
        holder.binding.ivNotification.isVisible = list[position].notification

        if (list[position].date != 0L)
            holder.binding.tvDate.text = SimpleDateFormat(
                "dd. MM. yyyy, HH:mm",
                Locale.getDefault()
            ).format(list[position].date)
        else
            holder.binding.tvDate.text = ""

        holder.binding.deleteButton.setOnClickListener {
            dao.delete(list[position])
            notifyItemRemoved(position)

            val alarmManager =
                holder.itemView.context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val intent = Intent(holder.itemView.context, NotificationBroadcastReceiver::class.java)
            val pendingIntent = PendingIntent.getBroadcast(
                holder.itemView.context,
                list[position].id!!.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE
            )
            alarmManager.cancel(pendingIntent)
        }
    }

    // function returns the number of items in the list
    override fun getItemCount(): Int {
        return list.size
    }
}
