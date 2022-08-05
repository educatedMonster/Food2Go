package com.example.kafiesta.screens.main.fragment.myshop

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.databinding.MyShopFragmentBinding
import com.trackerteer.taskmanagement.utilities.extensions.showToast

class MyShopFragment : Fragment() {

    companion object {
        fun newInstance() = MyShopFragment()
    }

    private lateinit var binding: MyShopFragmentBinding

    // TODO: Use the ViewModel
    private val myShopViewModel: MyShopViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(MyShopViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.my_shop_fragment,
            container,
            false)
        binding.lifecycleOwner = this
        binding.myShopViewModel = myShopViewModel
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