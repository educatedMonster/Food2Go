package com.example.kafiesta.screens.main.fragment.order.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.databinding.FragmentCompletedBinding
import com.example.kafiesta.databinding.FragmentDashboard2Binding

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentCompleted : Fragment() {

    private lateinit var binding: FragmentCompletedBinding
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(OrderViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return initBinding(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        initConfig()
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_completed,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.orderViewModel = orderViewModel
        return binding.root
    }

    private fun initConfig() {
        initAdapter()
        initViews()
        initLiveData()
    }

    private fun initAdapter() {
//        TODO("Not yet implemented")
    }

    private fun initViews() {
        binding.sectionLabel.text = "Fragment Completed"
    }

    private fun initLiveData() {
//        TODO("Not yet implemented")
//        dashboardViewModel.sampleHere.observe(viewLifecycleOwner, Observer {
//
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }


}
