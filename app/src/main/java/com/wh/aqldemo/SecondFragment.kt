package com.wh.aqldemo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.robinhood.spark.animation.LineSparkAnimator
import com.wh.adapter.RandomizedAdapter
import com.wh.model.DataResponse
import com.wh.viewmodel.MyObseravble
import kotlinx.android.synthetic.main.fragment_second.*

class SecondFragment : Fragment() {
    private lateinit var mAdapter: RandomizedAdapter
    private lateinit var mViewModel: MyObseravble
    private var mCityName: String? = null

    companion object {
        val ARG_PARAM1 = "cityName"
        fun newInstance(cityName: String) =
            SecondFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, cityName)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModel()
        initSubscribe()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_second, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        init()
        getBundle()
    }

    private fun initModel() {
        mViewModel = ViewModelProvider(requireActivity())[MyObseravble::class.java]

    }

    private fun initSubscribe() {
        mViewModel.data.observe(this, Observer {
            val response: DataResponse = Gson().fromJson(it, DataResponse::class.java)
            mCityName?.let {
                for (data in response) {
                    if (it == data.city) {
                        mAdapter.loadData(data.aqi.toFloat())
                        progressBar?.visibility = View.GONE
                        break
                    }
                }
            }

        })


    }

    private fun init() {
        mAdapter = RandomizedAdapter()
        sparkview.adapter = mAdapter
        val lineSparkAnimator = LineSparkAnimator()
        //lineSparkAnimator.setDuration(2500L)
        // lineSparkAnimator.setInterpolator(AccelerateDecelerateInterpolator())
        sparkview.sparkAnimator = lineSparkAnimator
    }

    private fun getBundle() {
        arguments?.let {
            mCityName = it.getString(ARG_PARAM1, "")
        }
    }

}