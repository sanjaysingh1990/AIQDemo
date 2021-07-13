package com.wh.adapter

import com.robinhood.spark.SparkAdapter
import java.util.*

class RandomizedAdapter : SparkAdapter() {
    private val yData: ArrayList<Float>


    override fun getCount(): Int {
        return yData.size
    }

    override fun getItem(index: Int): Any {
        return yData[index]
    }

    override fun getY(index: Int): Float {
        return yData[index]
    }

    init {
        yData = ArrayList()
    }

    fun loadData(value: Float) {
        yData.add(value)
        notifyDataSetChanged()
    }
}