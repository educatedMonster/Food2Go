package com.example.food2go.screens.product_and_inventory.inventory

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.example.food2go.R
import com.example.food2go.constants.DialogTag
import com.example.food2go.constants.UserConst
import com.example.food2go.databinding.FragmentInventoryBinding
import com.example.food2go.domain.InventoryDomain
import com.example.food2go.screens.product_and_inventory.product.dialogs.DialogModifyQuantity
import com.example.food2go.screens.product_and_inventory.inventory.adapter.InventoryAdapter
import com.example.food2go.utilities.decorator.DividerItemDecoration
import com.example.food2go.utilities.getDialog
import com.example.food2go.utilities.helpers.RecyclerClick2View
import com.example.food2go.utilities.helpers.SharedPrefs
import com.example.food2go.utilities.helpers.getSecurePrefs
import com.example.food2go.utilities.hideKeyboard
import com.example.food2go.utilities.extensions.showToast
import com.trackerteer.taskmanagement.utilities.extensions.gone
import com.trackerteer.taskmanagement.utilities.extensions.visible
import timber.log.Timber

/**
 * A placeholder fragment containing a simple view.
 */
class FragmentInventory : Fragment() {

    private lateinit var binding: FragmentInventoryBinding
    private val inventoryViewModel: InventoryViewModel by lazy {
        ViewModelProvider.AndroidViewModelFactory.getInstance(requireActivity().application)
            .create(InventoryViewModel::class.java)
    }

    private lateinit var mAdapter: InventoryAdapter
    private var userId = 0L
    private var mLength = 10L
    private var mStart = 0L
    private var mSearch = ""
    private var mCurrentPage = 0L
    private var mLastPage = 0L

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
            R.layout.fragment_inventory,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.model = inventoryViewModel
        return binding.root
    }

    private fun initConfig() {
        initAdapter()
        initEventListener()
        initLiveData()
    }

    private fun initAdapter() {
        mAdapter = InventoryAdapter(
            context = requireContext(),
            RecyclerClick2View(
                click1 = {},
                click2 = { it, view ->
                    val model = it as InventoryDomain
                    showInventoryMenu(model, view)
                }
            )
        )
    }

    private fun initEventListener() {
        binding.apply {
            swipeRefreshLayout.setOnRefreshListener {
                initRequest()
            }

            recyclerViewProducts.apply {
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
                        initRequestOffset()
                    }
                    super.onScrolled(recyclerView, dx, dy)
                }
            })
        }
    }

    private fun initLiveData() {
        inventoryViewModel.apply {
            inventoryList.observe(viewLifecycleOwner) {
                mLength += it.data.size
                mStart += it.data.size
                mCurrentPage = it.currentPage
                mLastPage = it.lastPage

                for (data in it.data) {
                    mAdapter.addData(data)
                }
            }

            isModifyQuantity.observe(viewLifecycleOwner) {
                hideKeyboard(requireActivity())
                (getDialog(requireActivity(),
                    DialogTag.DIALOG_BOTTOM_QUANTITY) as DialogModifyQuantity?)?.dismiss()
                initRequest()
                showToast("Quantity modified")
            }

            isLoading.observe(viewLifecycleOwner) {
                setLoading(it)
            }
        }
    }

    private fun showInventoryMenu(model: InventoryDomain, view: View) {
        val popup = PopupMenu(requireContext(), view)
        val inflater = popup.menuInflater
        inflater.inflate(R.menu.menu_inventory_nav, popup.menu)
        popup.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.action_nav_inventory_edit -> {
                    val dialog = DialogModifyQuantity(
                        userId = userId,
                        model = model,
                        listener = object : DialogModifyQuantity.Listener {
                            override fun onAddQuantityListener(quantity: String, productId: Long) {
                                inventoryViewModel.modifyQuantity(quantity, productId)
                            }
                        }
                    )
                    dialog.setParentActivity(requireActivity(), requireActivity())
                    dialog.show(requireActivity().supportFragmentManager,
                        DialogTag.DIALOG_BOTTOM_QUANTITY)
                    return@setOnMenuItemClickListener true
                }
                R.id.action_nav_inventory_remove -> {
                    val alertDeleteDialog = AlertDialog.Builder(requireContext())
                    alertDeleteDialog.apply {
                        setTitle("Delete")
                        setMessage("Are you sure you want to remove this product in your inventory?")
                        setPositiveButton("Remove") { dialogInterface, _ ->
                            inventoryViewModel.removeInventory(productId = model.productID)
                            mAdapter.remove(model)
                            dialogInterface.dismiss()
                        }
                        setNegativeButton(getString(R.string.dialog_cancel_button)) { dialogInterface, _ ->
                            dialogInterface.dismiss()
                        }
                        alertDeleteDialog.show()
                    }
                    return@setOnMenuItemClickListener true
                }
                else -> {
                    return@setOnMenuItemClickListener false
                }
            }
        }
        popup.show()
    }

    private fun initRequest() {
        mAdapter.clearAdapter()
        mCurrentPage = 1L
        mLength = 10L
        mStart = 0L

        inventoryViewModel.getAllInventory(
            length = mLength,
            start = mStart,
            search = mSearch)
    }

    private fun initRequestOffset() {
        if (mCurrentPage < mLastPage) {
            mCurrentPage++
            inventoryViewModel.getAllInventory(
                length = mLength,
                start = mStart,
                search = mSearch)
        }
    }

    private fun setLoading(set: Boolean) {
        try {
            binding.apply {
                swipeRefreshLayout.isRefreshing = set
                if (set) {
                    shimmerViewContainer.startShimmer()
                    shimmerViewContainer.visible()
                    recyclerViewProducts.gone()
                } else {
                    shimmerViewContainer.stopShimmer()
                    shimmerViewContainer.gone()
                    recyclerViewProducts.visible()
                }
            }
        } catch (e: Exception) {
            Timber.e(e)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding
    }
}
