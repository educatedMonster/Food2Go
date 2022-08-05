package com.example.kafiesta.screens.main.fragment.order

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.databinding.OrderFragmentBinding

class OrderFragment : Fragment() {

    companion object {
        fun newInstance() = OrderFragment()
    }

    private lateinit var binding: OrderFragmentBinding

    // TODO: Use the ViewModel
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(OrderViewModel::class.java)
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.order_fragment,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.orderViewModel = orderViewModel
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
        binding.buttonLogout.setOnClickListener {

        }
    }

    private fun initLiveData() {
        //
    }

    fun initRequest() {
        binding.tvText.text = "Init Request"
    }

}