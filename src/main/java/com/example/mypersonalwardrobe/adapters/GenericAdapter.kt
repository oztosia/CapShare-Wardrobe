import android.content.ContentValues
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.example.mypersonalwardrobe.adapters.viewholders.GenericViewHolder
import com.google.gson.Gson


class GenericAdapter<T>(private val viewHolderFactory: (view: View) -> GenericViewHolder<T>,
                        @LayoutRes val layout: Int)
    : RecyclerView.Adapter<GenericViewHolder<T>>(), Filterable {

    var dataSet: ArrayList<T> = arrayListOf()
    var sortedArray: ArrayList<T> = arrayListOf()

    init { sortedArray = dataSet }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenericViewHolder<T> {
        val v = LayoutInflater.from(parent.context).inflate(layout, parent, false)
        //val lifecycleOwner = parent.context as LifecycleOwner
        return viewHolderFactory(v)
    }


    override fun onBindViewHolder(holder: GenericViewHolder<T>, position: Int) {
        holder.bind(sortedArray[position])
    }


    override fun getItemCount(): Int = sortedArray.size




    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) sortedArray = dataSet else {
                    val filteredList = ArrayList<T>()
                    dataSet
                        .filter {
                            val gson = Gson()
                            (gson.toJson(it).toString().contains(constraint!!))
                        }
                        .forEach { filteredList.add(it) }
                    sortedArray = filteredList

                    Log.d(ContentValues.TAG, "filtered list size: " + sortedArray.size)

                }
                return FilterResults().apply { values = sortedArray }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                sortedArray = if (results?.values == null)
                    dataSet
                else
                    results.values as ArrayList<T>
                notifyDataSetChanged()
            }
        }
    }


}