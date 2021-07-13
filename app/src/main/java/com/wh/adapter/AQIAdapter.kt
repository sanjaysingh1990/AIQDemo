package com.wh.adapter

import android.content.res.ColorStateList
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wh.aqldemo.R
import com.wh.model.DataResponseItem
import com.wh.utils.Utils
import kotlinx.android.synthetic.main.list_item.view.*
import java.util.*

class AQIAdapter(private val onItemClick: (String) -> Unit) :
    RecyclerView.Adapter<AQIAdapter.ViewHolder>() {

    val mDataList: ArrayList<DataResponseItem> = ArrayList()

    // holder class to hold reference
    inner class ViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
        init {
            view.setOnClickListener {
                onItemClick(mDataList[adapterPosition].city)
            }
        }

        fun initValues(data: DataResponseItem?) {
            view.text_aqi_city.text = data?.city
            val aqiStatus = Utils.getStatus(data?.aqi?.toInt() ?: 0, view.context)
            view.text_aqi_value.text = "${data?.aqi?.toInt()}"
            view.text_aqi_value.backgroundTintList = ColorStateList.valueOf(aqiStatus.color)
            view.text_index.text = "${adapterPosition + 1}"
            view.text_aqi_status.text = aqiStatus.status
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // create view holder to hold reference
        return ViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.initValues(mDataList[position])
    }

    fun loadData(dataList: ArrayList<DataResponseItem>) {
        mDataList?.clear()
        mDataList?.addAll(dataList)
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return mDataList?.size ?: 0
    }

}