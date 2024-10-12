package com.dicoding.dicodingeventapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

class DetailEventActivity : AppCompatActivity() {
    private val viewModel: EventViewModel by viewModels()

//    private lateinit var binding: ActivityMainBinding
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_event)

//        binding = ActivityMainBinding.inflate(layoutInflater)
//        setContentView(binding.root)

        progressBar = findViewById(R.id.progressBar2)
        progressBar.visibility = View.VISIBLE

        val id = intent.getIntExtra("id", 1)

        if (id != -1) {
            Log.d(TAG, "EventId: $id")
            viewModel.fetchEventDetail(id.toString())
        }


        // Observe the event detail LiveData
        viewModel.eventDetailLiveData.observe(this) { detailResponse ->

            progressBar.visibility = View.INVISIBLE
            detailResponse?.let {

                val eventName = findViewById<TextView>(R.id.eventName)
                val eventMediaCover = findViewById<ImageView>(R.id.mediaCover)
                val eventTime = findViewById<TextView>(R.id.eventTime)
                val eventQuota = findViewById<TextView>(R.id.eventQuota)
                val eventDescription = findViewById<TextView>(R.id.eventDescription)
                val registerButton = findViewById<Button>(R.id.registerButton)
//                ketika tombol register diclik
                registerButton.setOnClickListener {
                    val url = detailResponse.event?.link ?: "Event link not available"

                    // Membuat intent untuk membuka URL di browser
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse(url)

                    // Memulai intent
                    startActivity(intent)
                }

                eventName.text = detailResponse.event?.name ?: "Event name not available"
                eventTime.text = detailResponse.event?.beginTime ?: ""
                eventQuota.text = detailResponse.event?.quota.toString()

                val htmlText = detailResponse.event?.description
                if (htmlText != null) {
                    eventDescription.text = Html.fromHtml(htmlText, Html.FROM_HTML_MODE_LEGACY)
                } else {
                    eventDescription.text = getString(R.string.description_not_available)
                }

                Glide.with(this)
                    .load(detailResponse.event?.mediaCover)
                    .into(eventMediaCover)

            }
        }
    }
}
