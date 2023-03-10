package me.erichps.photogallery.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.erichps.photogallery.R
import me.erichps.photogallery.databinding.FragmentFeedBinding
import me.erichps.photogallery.view.viewmodels.FeedViewModel
import javax.inject.Inject

@AndroidEntryPoint
class FeedFragment : Fragment() {

    @Inject
    lateinit var adapter: InfiniteAdapter

    private var binding: FragmentFeedBinding? = null
    private val viewModel: FeedViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchPhotos()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFeedBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding?.apply {
            photoList.layoutManager = LinearLayoutManager(requireContext())
            photoList.adapter = adapter.apply { onLoadMore = { fetchPhotos() } }
            refresh.setOnRefreshListener {
                adapter.clearAll()
                viewModel.refresh()
                fetchPhotos()
            }
            fab.setOnClickListener {
                if (photoList.layoutManager is GridLayoutManager) {
                    photoList.layoutManager = LinearLayoutManager(requireContext())
                } else {
                    photoList.layoutManager = GridLayoutManager(requireContext(),2)
                }
            }
        }
    }

    private fun setupObserver() {
        viewModel.apply {
            photos.observe(viewLifecycleOwner) {
                adapter.addPhotos(it)
                binding?.refresh?.isRefreshing = false
            }
            errorMessage.observe(viewLifecycleOwner) {
                binding?.refresh?.isRefreshing = false
                Toast.makeText(this@FeedFragment.requireContext(),getString(R.string.general_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun fetchPhotos() {
        binding?.refresh?.isRefreshing = true
        viewModel.getPhoto()
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }

}