package bmta.sem.view

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import bmta.sem.NotificationBroadcastReceiver
import bmta.sem.R
import bmta.sem.databinding.ActivityNewEventBinding
import bmta.sem.model.Events
import bmta.sem.viewmodel.EventViewModel
import java.util.Calendar

class NewEvent : AppCompatActivity() {
    private lateinit var binding: ActivityNewEventBinding
    val viewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = ActivityNewEventBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setSupportActionBar(binding.toolbar.toolbar)
        binding.tpTime.setIs24HourView(true)

        var event = viewModel.getById(intent.getLongExtra("eventId", -1))
        binding.bsave.setOnClickListener {
            createEvent(event)
        }

        if (event == null) {
            binding.toolbar.tvTitle.text = getString(R.string.nova_udalost)
        } else {
            binding.toolbar.tvTitle.text = getString(R.string.detail_udalosti)

            binding.etName.setText(event.name)
            binding.etDescription.setText(event.description)
            binding.cbNotification.isChecked = event.notification

            var calendar = Calendar.getInstance()
            calendar.timeInMillis = event.date

            binding.dpDate.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH))

            binding.tpTime.hour = calendar.get(Calendar.HOUR)
            binding.tpTime.minute = calendar.get(Calendar.MINUTE)
        }
    }

    private fun createEvent(event: Events?) {
        val name = binding.etName.text.toString()
        val description = binding.etDescription.text.toString()
        val notification = binding.cbNotification.isChecked

        val data = Events(
            event?.id,
            name = name,
            description = description,
            date = getTime(),
            notification = notification
        )

        if (data.name.isNotEmpty() && !(data.notification && data.date == 0L)) {
            data.id = viewModel.add(data)
            scheduleActivity(data)
            Toast.makeText(this, "Událost uložena", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, MainActivity::class.java))
        } else {
            Toast.makeText(this, "Událost není správně vyplněna", Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun getTime(): Long {
        return Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()

            set(Calendar.YEAR, binding.dpDate.year)
            set(Calendar.MONTH, binding.dpDate.month)
            set(Calendar.DAY_OF_MONTH, binding.dpDate.dayOfMonth)

            set(Calendar.HOUR, binding.tpTime.hour)
            set(Calendar.MINUTE, binding.tpTime.minute)
        }.timeInMillis
    }

    private fun scheduleActivity(event: Events) {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(this, NotificationBroadcastReceiver::class.java)
        intent.putExtra("notification_id", event.id?.toInt())
        intent.putExtra("notification_title", event.name)
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                event.id!!.toInt(),
                intent,
                PendingIntent.FLAG_MUTABLE
            )

        if (!alarmManager.canScheduleExactAlarms()) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermissions(arrayOf(android.Manifest.permission.USE_EXACT_ALARM), 123)
                requestPermissions(arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 123)
            }
            requestPermissions(arrayOf(android.Manifest.permission.SCHEDULE_EXACT_ALARM), 123)
        }

        alarmManager.cancel(pendingIntent)
        if (event.notification && event.date != null && event.date != 0L) {
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                event.date!!,
                pendingIntent
            )
        }
    }
}