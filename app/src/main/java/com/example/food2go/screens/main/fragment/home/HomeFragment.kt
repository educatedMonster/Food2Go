package com.example.food2go.screens.main.fragment.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.food2go.R
import com.example.food2go.databinding.HomeFragmentBinding

class HomeFragment : Fragment() {

    companion object {
        fun newInstance() = HomeFragment()
    }

    private lateinit var binding: HomeFragmentBinding

    // TODO: Use the ViewModel
    private val homeViewModel: HomeViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.home_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.homeViewModel = homeViewModel
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initConfig()
    }

    private fun initConfig() {
        initBinding()
        initListener()
        initLiveData()
    }

    private fun initBinding() {
        //
    }

    private fun initListener() {

    }

    private fun initLiveData() {
        //
    }

    fun initRequest() {
        binding.tvText.text = "Init Request"
    }
}