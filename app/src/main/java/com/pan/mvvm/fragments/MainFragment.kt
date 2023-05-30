package com.pan.mvvm.fragments

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.pan.mvvm.R
import com.pan.mvvm.adapter.LatestPostAdapter
import com.pan.mvvm.adapter.PopularPostAdapter
import com.pan.mvvm.databinding.FragmentMainBinding
import com.pan.mvvm.ui.CategoryActivity
import com.pan.mvvm.ui.CreatePostActivity
import com.pan.mvvm.ui.CurrencyExchangeActivity
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var popularPostAdapter: PopularPostAdapter
    private lateinit var latestPostAdapter: LatestPostAdapter
    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        popularPostAdapter = PopularPostAdapter()
        latestPostAdapter = LatestPostAdapter()

        bindObserversForPopularItem()
        bindObserversForLatestPostItem()

        //getAllPopularItem
        myanfobaseViewModel.getAllPopularPostItem()
        //getLatestPostItem
        myanfobaseViewModel.getLatestPostItem()

        //Go to Categories Activity
        binding.category.setOnClickListener {
            val intent = Intent(requireContext(), CategoryActivity::class.java)
            startActivity(intent)
        }

        binding.btnCreate.setOnClickListener {
            val intent = Intent(requireContext(), CreatePostActivity::class.java)
            startActivity(intent)
        }

        binding.currency.setOnClickListener {
            val intent = Intent(requireContext(), CurrencyExchangeActivity::class.java)
            startActivity(intent)
        }

        binding.search.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                popularPostAdapter.filter(newText)
                latestPostAdapter.filter(newText)
                return true
            }

        })
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbar = requireView().findViewById<MaterialToolbar>(R.id.toolbar)
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.setLogo(R.drawable.myanlogo)
    }


    private fun bindObserversForLatestPostItem() {
        myanfobaseViewModel.getAllLatestPostLiveData.observe(viewLifecycleOwner) { response ->
            binding.progressBar.isVisible = false
            when (response) {
                is NetworkResult.Success -> {
                    val latestPostItemList = response.data
                    latestPostItemList?.let {
                        binding.latestRecycler.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        latestPostAdapter.setLatestPostItem(it)
                        binding.latestRecycler.adapter = latestPostAdapter
                    }
                }

                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                        .show()

                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true

                }

                else -> {}
            }
        }
    }

    private fun bindObserversForPopularItem() {
        myanfobaseViewModel.getAllPopuplarPostItemLiveData.observe(viewLifecycleOwner) { response ->

            binding.progressBar.isVisible = false
            when (response) {
                is NetworkResult.Success -> {
                    val popularList = response.data

                    popularList?.let {
                        binding.popularRecycler.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        binding.popularRecycler.setHasFixedSize(true)
                        // Set the list of items to the adapter
                        popularPostAdapter.setPopularItem(it)
                        binding.popularRecycler.adapter = popularPostAdapter

                    }
                }

                is NetworkResult.Error -> {
                    MaterialAlertDialogBuilder(requireContext())
                        .setMessage(response.message)
                        .setPositiveButton(android.R.string.ok, null)
                        .show()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT)
                        .show()

                }

                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

                else -> {}
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
