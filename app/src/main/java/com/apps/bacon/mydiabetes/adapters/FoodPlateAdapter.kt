package com.apps.bacon.mydiabetes.adapters

import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.apps.bacon.mydiabetes.R
import com.apps.bacon.mydiabetes.data.Product
import com.apps.bacon.mydiabetes.utilities.Calculations
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.product_item_food_plate.view.*

class FoodPlateAdapter constructor(
    private val listener: OnProductClickListener
) : RecyclerView.Adapter<FoodPlateAdapter.ViewHolder>() {
    private var data: List<Product> = ArrayList()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view), View.OnClickListener {
        val productName: TextView = view.productName
        val measure: TextInputEditText = view.measureTextInput
        val measureLayout: TextInputLayout = view.measureTextInputLayout
        val carbohydrateExchangers: TextView = view.carbohydrateExchangers
        val proteinFatExchangers: TextView = view.proteinFatExchangers
        val calories: TextView = view.calories

        init {
            view.setOnClickListener(this)

        }

        override fun onClick(p0: View?) {
            listener.onProductClick(data[adapterPosition].id)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.product_item_food_plate, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.productName.text = data[position].name
        if(data[position].weight == null){
            holder.measure.setText(data[position].pieces.toString())
            holder.measureLayout.suffixText = "szt."
            holder.measure.inputType = InputType.TYPE_NUMBER_FLAG_SIGNED or InputType.TYPE_CLASS_NUMBER
        }else{
            holder.measure.setText(data[position].weight.toString())
            holder.measureLayout.suffixText = "g/ml"
            holder.measure.inputType = InputType.TYPE_NUMBER_FLAG_DECIMAL or InputType.TYPE_CLASS_NUMBER
        }

        holder.carbohydrateExchangers.text = data[position].carbohydrateExchangers.toString()
        holder.proteinFatExchangers.text = data[position].proteinFatExchangers.toString()
        holder.calories.text = data[position].calories.toString()

        holder.measure.onTextChanged {
            val value: String = if(it.isNullOrEmpty()){
                "0"
            }else
                it.toString()

            if(data[position].weight == null){
                holder.carbohydrateExchangers.text = Calculations()
                    .carbohydrateExchangesByPieces(
                        data[position].carbohydrateExchangers, data[position].pieces!!, value.toInt())
                    .toString()
                holder.proteinFatExchangers.text = Calculations()
                    .proteinFatExchangersByPieces(
                    data[position].proteinFatExchangers, data[position].pieces!!, value.toInt())
                    .toString()
                holder.calories.text = Calculations()
                    .caloriesByPieces(
                        data[position].calories!!, data[position].pieces!!, value.toInt())
                    .toString()
            }else{
                holder.carbohydrateExchangers.text = Calculations()
                    .carbohydrateExchangesByWeight(
                        data[position].carbohydrateExchangers, data[position].weight!!, value.toDouble())
                    .toString()
                holder.proteinFatExchangers.text = Calculations()
                    .proteinFatExchangersByWeight(
                        data[position].proteinFatExchangers, data[position].weight!!, value.toDouble())
                    .toString()
                holder.calories.text = Calculations()
                    .caloriesByWeight(
                        data[position].calories!!, data[position].weight!!, value.toDouble())
                    .toString()

            }

        }
    }

    override fun getItemCount(): Int = data.size

    fun updateData(dataList: List<Product>){
        data = dataList
        notifyDataSetChanged()
    }

    fun getProduct(position: Int) : Product{
        return data[position]
    }

    interface OnProductClickListener {
        fun onProductClick(productID: Int)

    }

    private fun TextInputEditText.onTextChanged(onTextChanged: (CharSequence?) -> Unit){
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                onTextChanged.invoke(p0)
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })
    }
}