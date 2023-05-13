package com.pan.mvvm.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.pan.mvvm.adapter.LatestPostImageAdapter
import com.pan.mvvm.databinding.FragmentLatestPostDetailBinding

class LatestPostDetailFragment : Fragment() {

    private lateinit var latestPostImageAdapter: LatestPostImageAdapter

    private var _binding: FragmentLatestPostDetailBinding? = null
    private val binding get() = _binding

    private val args by navArgs<LatestPostDetailFragmentArgs>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLatestPostDetailBinding.inflate(layoutInflater, container, false)

        _binding!!.postTitle.text = args.latestArgs.title
        _binding!!.postDate.text = args.latestArgs.updatedAt.substring(0, 10)
        _binding!!.userName.text = args.latestArgs.username
        _binding!!.description.text = args.latestArgs.description

        context?.let {
            Glide.with(it).load(args.latestArgs.userprofile).into(_binding!!.latestImageProfile)
        }

        val latestList = args.latestArgs.files


        latestPostImageAdapter = LatestPostImageAdapter(requireContext(), latestList)
        binding?.imageDetailListAdapter?.adapter = latestPostImageAdapter
        binding?.imageDetailListAdapter?.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        _binding!!.favoriteText.text = args.latestArgs.viewcount.toString()
        _binding!!.viewCount.text = args.latestArgs.viewcount.toString()


        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}