package bmta.sem.view

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import bmta.sem.R
import bmta.sem.databinding.ActivityMainBinding
import bmta.sem.viewmodel.EventAdapter
import bmta.sem.viewmodel.EventViewModel

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    val viewModel: EventViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.toolbar.tvTitle.text = getString(R.string.udalosti)
        setSupportActionBar(binding.toolbar.toolbar)

        setContentView(binding.root)

        binding.floatingActionButton.setOnClickListener {
            val intent = Intent(this, NewEvent::class.java)
            startActivity(intent)
        }

        viewModel.getAll().observe(this) { list ->
            binding.recyclerView.layoutManager = LinearLayoutManager(applicationContext)
            binding.recyclerView.adapter = EventAdapter(this, list)
        }

        //Notifications init
        val name = "My Notification Channel"
        val descriptionText = "Channel description"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("channel_id", name, importance).apply {
            description = descriptionText
        }
        val notificationManager: NotificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}