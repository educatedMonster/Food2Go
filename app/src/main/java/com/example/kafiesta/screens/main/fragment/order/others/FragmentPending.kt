package com.example.kafiesta.screens.main.fragment.order.others

import android.os.Build
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
import com.example.kafiesta.constants.DialogTag
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.FragmentPendingBinding
import com.example.kafiesta.domain.OrderBaseDomain
import com.example.kafiesta.screens.main.fragment.order.OrderStatusEnum
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.helpers.OrderRecyclerClick
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.trackerteer.taskmanagement.utilities.extensions.showToast
import kotlinx.android.synthetic.main.fragment_pending.view.*
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentPending : Fragment() {

    private lateinit var binding: FragmentPendingBinding
    private val orderViewModel: OrderViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory(requireActivity().application)
            .create(OrderViewModel::class.java)
    }

    private lateinit var mAdapter: OrderAdapter
    private var userId = 0L

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return initBinding(inflater, container)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userId = SharedPrefs(getSecurePrefs(requireContext())).getString(UserConst.USER_ID)!!.toLong()
        initConfig()
    }

    override fun onResume() {
        super.onResume()
        initRequest()
    }

    private fun initBinding(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_pending,
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
        mAdapter = OrderAdapter(RecyclerClick(
            click = {
                val dialog = DialogOrderDetails(
                    userId = userId,
                    model = it as OrderBaseDomain,
                    listener = object : DialogOrderDetails.Listener {
                        override fun onAcceptOrder(model: OrderBaseDomain) {
                            showToast("Not yet implemented")
                        }

                        override fun onRejectOrder(model: OrderBaseDomain) {
                            showToast("Not yet implemented")
                        }
                    },
                    activity = requireActivity()
                )
                dialog.show(requireActivity().supportFragmentManager, DialogTag.DIALOG_ORDER_DETAILS)
            }
        ))
    }

    private fun initViews() {
        binding.root.recycler_view.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
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

                }
                super.onScrolled(recyclerView, dx, dy)
            }
        })
    }

    private fun initLiveData() {
        orderViewModel.orderPendingList.observe(viewLifecycleOwner) { it ->
//            mCurrentPage = it.currentPage
//            mLastPage = it.lastPage
            for (data in it.result) {
                mAdapter.addData(data)
            }
        }

        orderViewModel.isLoading.observe(viewLifecycleOwner) {
            binding.swipeRefreshLayout.isRefreshing = it

        }
    }

    fun initRequest() {
        mAdapter.clearAdapter()
        orderViewModel.getAllOrderList(
            orderStatusEnum = OrderStatusEnum.PENDING,
//            length = 10,
//            start = 0,
            search = "",
            merchant_user_id = 5,
//            date_from = getDateNow(),
//            date_to = getDateNow())
            date_from = "2022-08-22",
            date_to = "2022-08-22") // TODO - for testing
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }

    private fun getDateNow() :  String{
        val current = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LocalDateTime.now()
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val formatter = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            DateTimeFormatter.ofPattern("yyyy-MM-dd")
        } else {
            TODO("VERSION.SDK_INT < O")
        }

        val formatted = current.format(formatter)
        println("Current Date and Time is: $formatted")
        return formatted
    }


}
