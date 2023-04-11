package com.pan.mvvm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.pan.mvvm.adapter.CategoryRowAdapter
import com.pan.mvvm.adapter.LatestPostAdapter
import com.pan.mvvm.adapter.PopularPostAdapter
import com.pan.mvvm.databinding.FragmentMainBinding
import com.pan.mvvm.utils.NetworkResult
import com.pan.mvvm.utils.TokenManager
import com.pan.mvvm.viewModel.MyanfobaseViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class MainFragment : Fragment() {

    private var _binding: FragmentMainBinding? = null
    private val binding get() = _binding!!
    private lateinit var adapter: CategoryRowAdapter
    private lateinit var popularPostAdapter: PopularPostAdapter
    private lateinit var latestPostAdapter: LatestPostAdapter


    @Inject
    lateinit var tokenManager: TokenManager

    private val myanfobaseViewModel by viewModels<MyanfobaseViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMainBinding.inflate(layoutInflater, container, false)
        //adapter
        adapter = CategoryRowAdapter()
        popularPostAdapter = PopularPostAdapter()
        latestPostAdapter = LatestPostAdapter()

        bindObserversForCategoryItem()
        bindObserversForPopularItem()
        bindObserversForLatestPostItem()

        //getAllCategoryName
        myanfobaseViewModel.getAllCategoryItem()
        //getAllPopularItem
        myanfobaseViewModel.getAllPopularPostItem()
        //getLatestPostItem
        myanfobaseViewModel.getLatestPostItem()
        return binding.root
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
                        adapter.notifyDataSetChanged()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true

                }
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
                        popularPostAdapter.setPopularItem(it)
                        binding.popularRecycler.adapter = popularPostAdapter
                        adapter.notifyDataSetChanged()
                    }
                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()

                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }
            }
        }
    }

    private fun bindObserversForCategoryItem() {
        myanfobaseViewModel.getAllCategoryLiveData.observe(viewLifecycleOwner) { response ->

            binding.progressBar.isVisible = false
            when (response) {
                is NetworkResult.Success -> {
                    val cateItemList = response.data
                    cateItemList?.let {
                        binding.mainRecycler.layoutManager =
                            LinearLayoutManager(
                                requireContext(),
                                LinearLayoutManager.HORIZONTAL,
                                false
                            )
                        binding.mainRecycler.setHasFixedSize(true)
                        adapter.setCateNamList(it)
                        binding.mainRecycler.adapter = adapter
                        adapter.notifyDataSetChanged()
                    }

                }
                is NetworkResult.Error -> {
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_SHORT).show()
                }
                is NetworkResult.Loading -> {
                    binding.progressBar.isVisible = true
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}