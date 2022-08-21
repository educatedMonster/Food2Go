package com.example.kafiesta.screens.dashboard.others

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kafiesta.R
import com.example.kafiesta.databinding.FragmentDashboard2Binding
import com.example.kafiesta.databinding.FragmentDashboardBinding
import com.example.kafiesta.screens.main.fragment.order.OrderFragment
import com.example.kafiesta.utilities.decorator.DividerItemDecoration

/**
 * A placeholder fragment containing a simple view.
 */
class Fragment2 : Fragment() {

    private lateinit var binding: FragmentDashboard2Binding
    private val dashboardViewModel: DashboardViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(DashboardViewModel::class.java)
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
            R.layout.fragment_dashboard_2,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.pageViewModel = dashboardViewModel
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
        binding.root.findViewById<RecyclerView>(R.id.recycler_view).apply {
            layoutManager = LinearLayoutManager(context)
//            adapter = mAdapter
            addItemDecoration(
                DividerItemDecoration(
                    context,
                    R.drawable.list_divider_decoration
                )
            )
        }.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                if (!recyclerView.canScrollVertically(1)) {
//                    initRequestOffset()
                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })

        binding.swipeRefreshLayout.setOnRefreshListener {
//            initRequest()
        }
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

    companion object {
        fun newInstance() = OrderFragment()
    }
}
