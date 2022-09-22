package com.example.food2go.autocomplete

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import android.widget.TextView
import com.example.food2go.R
import java.util.*

class ArrayAdapterInstantAuto(
    context: Context,
    private val resourceId: Int,
    items: ArrayList<InstantAutoItem>,
) :
    ArrayAdapter<InstantAutoItem>(context, resourceId, items) {
    private var items: MutableList<InstantAutoItem> = ArrayList()
    private var tempItems: MutableList<InstantAutoItem>
    private var suggestions: MutableList<InstantAutoItem>

    private var mThreshold = 0
    private var mSelected: InstantAutoItem? = null

    init {
        this.items = items
        tempItems = items
        suggestions = ArrayList()
    }

    fun updateListItems(items: ArrayList<InstantAutoItem>) {
        this.items.clear()
        this.items.addAll(items)
        tempItems.clear()
        tempItems.addAll(items)
        notifyDataSetChanged()
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        try {
            if (convertView == null) {
                val inflater = LayoutInflater.from(context)
                view = inflater.inflate(resourceId, parent, false)
            }
            val i = getItem(position)
            val name = view!!.findViewById<TextView>(R.id.text1)
//            val imageView: ImageView = view.findViewById(R.id.imageView)
//            imageView.setImageResource(t.getImage())
            name.text = i!!.text
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return view!!
    }

    override fun getItem(position: Int): InstantAutoItem? {
        return items[position]
    }

    override fun getCount(): Int {
        return items.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getFilter(): Filter {
        return autoItemFilter
    }

    private val autoItemFilter: Filter = object : Filter() {
        override fun convertResultToString(resultValue: Any): CharSequence {
            val i = resultValue as InstantAutoItem
            return i.text
        }

        override fun performFiltering(charSequence: CharSequence): FilterResults {
            return if (charSequence.length >= mThreshold) {
                suggestions.clear()
                for (i in tempItems) {

                    if (i.text.lowercase(Locale.getDefault()).contains(charSequence.toString()
                            .lowercase(Locale.getDefault()))
                    ) {
                        suggestions.add(i)
                    }

                }
                val filterResults = FilterResults()
                filterResults.values = suggestions
                filterResults.count = suggestions.size
                filterResults
            } else {
                FilterResults()
            }
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
            val tempValues = results!!.values as ArrayList<*>
            var currentSuggestion = 0
            if (results.count > 0) {
                clear()
                for (i in tempValues) {
//                    if (results.count > MAX_SUGGESTIONS && currentSuggestion >= MAX_SUGGESTIONS) {
//                        remove(i as InstantAutoItem?)
//                        notifyDataSetChanged()
//                        break
//                    }
                    add(i as InstantAutoItem?)
                    currentSuggestion++
                    notifyDataSetChanged()
                }
            } else {
                clear()
                notifyDataSetChanged()
            }
        }
    }

    fun setThreshold(threshold: Int) {
        mThreshold = threshold
    }

    fun setSelected(instantAutoItem: InstantAutoItem?) {
        this.mSelected = instantAutoItem
    }

    fun getSelected(): InstantAutoItem? {
        return mSelected
    }

    val getTempList: List<InstantAutoItem?>
        get() = tempItems

    companion object {
        private const val MAX_SUGGESTIONS = 10
    }

}
