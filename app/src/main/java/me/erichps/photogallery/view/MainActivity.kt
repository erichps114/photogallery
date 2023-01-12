package me.erichps.photogallery.view

import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.erichps.photogallery.R
import me.erichps.photogallery.databinding.ActivityMainBinding
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var adapter: InfiniteAdapter

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainViewModel by viewModels()
    private var page = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding.apply {
            photoList.layoutManager = LinearLayoutManager(this@MainActivity)
            photoList.adapter = adapter
            refresh.setOnRefreshListener {
                adapter.clearAll()
                page=1
                fetchPhotos()
            }
        }
        adapter.onLoadMore = {
            fetchPhotos()
        }
    }

    private fun setupObserver() {
        viewModel.apply {
            photos.observe(this@MainActivity) {
                adapter.addPhotos(it)
                page++
                binding.refresh.isRefreshing = false
            }
            errorMessage.observe(this@MainActivity) {
                binding.refresh.isRefreshing = false
                Toast.makeText(this@MainActivity,getString(R.string.general_error),Toast.LENGTH_SHORT).show()
            }
            fetchPhotos()
        }
    }

    private fun fetchPhotos() {
        binding.refresh.isRefreshing = true
        viewModel.getPhoto(page)
    }

}