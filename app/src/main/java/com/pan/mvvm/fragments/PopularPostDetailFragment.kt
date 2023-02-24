package com.pan.mvvm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pan.mvvm.adapter.PopularPostImageAdapter
import com.pan.mvvm.databinding.FragmentPopularPostDetailBinding


class PopularPostDetailFragment : Fragment() {
    private var _binding: FragmentPopularPostDetailBinding? = null
    private val binding get() = _binding

    private lateinit var popularPostImageAdapter: PopularPostImageAdapter
    private val args by navArgs<PopularPostDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPopularPostDetailBinding.inflate(layoutInflater, container, false)
        _binding!!.postTitle.text = args.popularArgs.title
        _binding!!.postDate.text = args.popularArgs.updatedAt.substring(0, 10)
        _binding!!.userName.text = args.popularArgs.username
        _binding!!.description.text = args.popularArgs.description
        context?.let {
            Glide.with(it).load(args.popularArgs.userprofile).into(_binding!!.trendingImage)
        }
        val postList = args.popularArgs.files
        popularPostImageAdapter = PopularPostImageAdapter(requireContext(), postList)
        binding?.imageDetailListAdapter?.adapter = popularPostImageAdapter
        binding?.imageDetailListAdapter?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        _binding!!.favoriteText.text = args.popularArgs.v.toString()
        _binding!!.viewCount.text = args.popularArgs.viewcount.toString()
        return binding?.root

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}