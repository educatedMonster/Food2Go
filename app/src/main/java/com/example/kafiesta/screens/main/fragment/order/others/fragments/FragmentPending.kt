package com.example.kafiesta.screens.main.fragment.order.others.fragments

import android.content.Intent
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
import com.example.kafiesta.constants.IntentConst.ORDER_ID
import com.example.kafiesta.constants.OrderConst
import com.example.kafiesta.constants.UserConst
import com.example.kafiesta.databinding.FragmentPendingBinding
import com.example.kafiesta.domain.OrderListBaseDomain
import com.example.kafiesta.screens.image_viewer.ImageViewerActivity
import com.example.kafiesta.screens.main.fragment.order.OrderStatusEnum
import com.example.kafiesta.screens.main.fragment.order.OrderViewModel
import com.example.kafiesta.screens.main.fragment.order.others.adapter.OrderAdapter
import com.example.kafiesta.screens.main.fragment.order.others.dialogs.DialogOrderDetails
import com.example.kafiesta.screens.main.fragment.order.others.dialogs.DialogWebView
import com.example.kafiesta.utilities.decorator.DividerItemDecoration
import com.example.kafiesta.utilities.extensions.showToast
import com.example.kafiesta.utilities.getDialog
import com.example.kafiesta.utilities.helpers.OrderRecyclerClick
import com.example.kafiesta.utilities.helpers.RecyclerClick
import com.example.kafiesta.utilities.helpers.SharedPrefs
import com.example.kafiesta.utilities.helpers.getSecurePrefs
import com.trackerteer.taskmanagement.utilities.extensions.visible
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
        userId =
            SharedPrefs(getSecurePrefs(requireContext())).getString(UserConst.USER_ID)!!.toLong()
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
        mAdapter = OrderAdapter(
            context = requireContext(),
            onClickCallBack = RecyclerClick(
                click = {
                    val a = it as OrderListBaseDomain
                    val dialog = DialogOrderDetails(
                        status = a.order.status,
                        model = a,
                        onClickCallBack = OrderRecyclerClick(
                            proceed = { model ->
                                val order = model as OrderListBaseDomain
                                orderViewModel.orderMoveStatus(
                                    order.order.id,
                                    OrderConst.ORDER_PREPARING,
                                    ""
                                )
                                showToast(getString(R.string.dialog_message_order_preparing,
                                    order.order.orderId))
                            },
                            reject = { model ->
                                val order = model as OrderListBaseDomain
                                orderViewModel.orderMoveStatus(
                                    order.order.id,
                                    OrderConst.ORDER_REJECTED,
                                    "Static Test Rejected"
                                )
                                showToast(getString(R.string.dialog_message_order_rejected,
                                    order.order.orderId))
                            },
                            proofURL = { model ->
                                val order = model as OrderListBaseDomain
//                            val webDialog = DialogWebView(
//                                model = order,
//                                onClickCallBack = OrderRecyclerClick(
//                                    proceed = { model2 ->
//                                        val order2 = model2 as OrderListBaseDomain
//                                        orderViewModel.orderMoveStatus(
//                                            order2.order.id,
//                                            OrderConst.ORDER_PREPARING,
//                                            ""
//                                        )
//                                        showToast(getString(R.string.dialog_message_order_preparing, order.order.orderId))
//                                    },
//                                    reject = { model2 ->
//                                        val order2 = model2 as OrderListBaseDomain
//                                        orderViewModel.orderMoveStatus(
//                                            order2.order.id,
//                                            OrderConst.ORDER_REJECTED,
//                                            "Static Test Rejected"
//                                        )
//                                        showToast(getString(R.string.dialog_message_order_rejected, order.order.orderId))
//                                    },
//                                    proofURL = {}
//                                ),
//                                listener = object : DialogWebView.Listener {
//                                    override fun onCloseClicked() {
//                                        (getDialog(
//                                            requireActivity(),
//                                            DialogTag.DIALOG_WEB_VIEW_TAG
//                                        ) as DialogWebView?)?.dismiss()
//                                    }
//                                }
//                            )
//                            webDialog.show(requireActivity().supportFragmentManager,
//                                DialogTag.DIALOG_WEB_VIEW_TAG)


                                val intent =
                                    Intent(requireContext(), ImageViewerActivity::class.java)
                                intent.putExtra(ORDER_ID, order.order.id)
                                startActivity(intent)
                                requireActivity().overridePendingTransition(R.anim.enter_from_bottom,
                                    R.anim.stay)

                                (getDialog(requireActivity(),
                                    DialogTag.DIALOG_ORDER_DETAILS) as DialogOrderDetails?)?.dismiss()
                            }
                        ),
                        activity = requireActivity()
                    )
                    dialog.show(requireActivity().supportFragmentManager,
                        DialogTag.DIALOG_ORDER_DETAILS)
                }
            ))
    }

    private fun initViews() {
        binding.recyclerView.apply {
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
        orderViewModel.apply {
            orderPendingList.observe(viewLifecycleOwner) { model ->
//            mCurrentPage = it.currentPage
//            mLastPage = it.lastPage

                if (model.isNotEmpty()) {
                    for (data in model) {
                        mAdapter.addData(data)
                    }
                } else {
                    binding.layoutEmptyTask.root.visible()
                }
            }

            isLoading.observe(viewLifecycleOwner) {
                binding.swipeRefreshLayout.isRefreshing = it

            }

            orderStatus.observe(viewLifecycleOwner) {
                (getDialog(requireActivity(),
                    DialogTag.DIALOG_ORDER_DETAILS) as DialogOrderDetails?)?.dismiss()
                (getDialog(requireActivity(),
                    DialogTag.DIALOG_WEB_VIEW_TAG) as DialogWebView?)?.dismiss()
                showToast(it)
                initRequest()
            }
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
            date_from = getDateNow(),
            date_to = getDateNow())
    }


    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }

    private fun getDateNow(): String {
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
