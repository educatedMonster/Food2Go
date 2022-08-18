package com.example.kafiesta.screens.dashboard.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.kafiesta.R
import com.example.kafiesta.databinding.FragmentDashboardBinding
import com.example.kafiesta.screens.main.fragment.order.OrderFragment

/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private val pageViewModel: PageViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(PageViewModel::class.java)
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
            R.layout.fragment_dashboard,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.pageViewModel = pageViewModel
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
        binding.sectionLabel.text = "PlaceholderFragment"
    }

    private fun initLiveData() {
//        TODO("Not yet implemented")
//        pageViewModel.sampleHere.observe(viewLifecycleOwner, Observer {
//
//        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }

    companion object {
        fun newInstance() = OrderFragment()
    }
}
