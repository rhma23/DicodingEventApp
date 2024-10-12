package com.dicoding.dicodingeventapp

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.GridLayoutManager
import com.dicoding.dicodingeventapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {
    private val viewModel: EventViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var SearchView: SearchView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)
        //
        recyclerView.layoutManager = GridLayoutManager(this, 2)

        binding.progressBar.visibility = View.VISIBLE

        viewModel.eventsLiveData.observe(this) { events ->
            events?.let {
                binding.progressBar.visibility = View.INVISIBLE
                recyclerView.adapter = EventAdapter(events) { event ->
                    val intent = Intent(this, DetailEventActivity::class.java)
                    intent.putExtra("id", event.id)
                    startActivity(intent)
                    Log.d(TAG, "Selected Event ID: ${event.id}")
                }
            } ?: run {
                Log.e(TAG, "Event data is null")
            }
        }

        //fetch events (upcoming events initially)
        viewModel.fetchEvents(1)

        //searchView setup
        val searchView = findViewById<SearchView>(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let {
                    viewModel.searchEvents(it)
                    Log.d("SearchView", "Query submitted: $it")
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    // Log the changed query text as the user types
                    Log.d("SearchView", "Query text changed: $it")
                }
                return false
            }
        })

        // Bottom Navigation for filtering events
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottomNavigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.upcoming -> {
                    searchView.visibility = View.VISIBLE
                    recyclerView.layoutManager = LinearLayoutManager(this)
                    viewModel.fetchEvents(1)
                    true
                }
                R.id.finished -> {
                    searchView.visibility = View.INVISIBLE
                    recyclerView.layoutManager = GridLayoutManager(this, 2)
                    viewModel.fetchEvents(0)
                    true
                }
                else -> false
            }
        }
    }
}
