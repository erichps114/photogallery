package me.erichps.photogallery.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import dagger.hilt.android.AndroidEntryPoint
import me.erichps.photogallery.R
import me.erichps.photogallery.databinding.FragmentSearchBinding
import me.erichps.photogallery.view.viewmodels.SearchViewModel
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private var binding: FragmentSearchBinding? = null
    private val viewModel: SearchViewModel by viewModels()

    @Inject
    lateinit var adapter: InfiniteAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupUI()
        setupObserver()
    }

    private fun setupUI() {
        binding?.apply {
            searchResult.layoutManager = LinearLayoutManager(requireContext())
            searchResult.adapter = adapter.apply { onLoadMore = { viewModel.loadMoreData() } }
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                override fun onQueryTextSubmit(query: String?): Boolean {
                    viewModel.searchPhoto(query, 1)
                    searchView.clearFocus()
                    adapter.clearAll()
                    progressBar.show()
                    return true
                }

                override fun onQueryTextChange(newText: String?) = false
            })
        }
    }

    private fun setupObserver() {
        viewModel.apply {
            photos.observe(viewLifecycleOwner) {
                binding?.progressBar?.hide()
                adapter.addPhotos(it)
                binding?.emptySign?.visibility = if (adapter.itemCount > 0) View.GONE else View.VISIBLE
            }
            errorMessage.observe(viewLifecycleOwner) {
                binding?.progressBar?.hide()
                Toast.makeText(this@SearchFragment.requireContext(),getString(R.string.search_error), Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroyView() {
        binding = null
        super.onDestroyView()
    }


}