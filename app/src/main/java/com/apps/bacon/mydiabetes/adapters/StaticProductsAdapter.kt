package com.apps.bacon.mydiabetes.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.entities.StaticProduct
import com.apps.bacon.mydiabetes.databinding.ProductItemBinding
import com.bumptech.glide.Glide
import java.util.*

class StaticProductsAdapter constructor(
    private val listener: OnProductClickListener
) : RecyclerView.Adapter<StaticProductsAdapter.ViewHolder>() {
    private var data: List<StaticProduct> = ArrayList()

    inner class ViewHolder(view: ProductItemBinding) : RecyclerView.ViewHolder(view.root),
        View.OnClickListener {
        val productName: TextView = view.productName
        val measure: TextView = view.measure
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories
        val icon: ImageView = view.productIcon

        init {
            itemView.setOnClickListener(this)
        }

        override fun onClick(p0: View?) {
            listener.onStaticProductClick(data[bindingAdapterPosition].id)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = ProductItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (data[position].icon == null)
            holder.icon.setImageDrawable(
                ContextCompat.getDrawable(
                    holder.itemView.context,
                    R.drawable.ic_round_dinner_dining
                )
            )
        else
            Glide.with(holder.itemView).load(data[position].icon).into(holder.icon)

        holder.productName.text = data[position].name
        if (data[position].weight == null)
            holder.measure.text = data[position].pieces.toString()
        else
            holder.measure.text = data[position].weight.toString()

        holder.carbohydrateExchangers.text = data[position].carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = data[position].proteinFatExchangers.toString()
        holder.calories.text = data[position].calories.toString()
    }

    override fun getItemCount(): Int = data.size

    fun updateData(dataList: List<StaticProduct>) {
        data = dataList
        notifyDataSetChanged()
    }

    interface OnProductClickListener {
        fun onStaticProductClick(productId: Int)
    }
}