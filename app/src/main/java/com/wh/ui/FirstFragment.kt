package com.wh.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.wh.adapter.AQIAdapter
import com.wh.model.DataResponse
import com.wh.viewmodel.MyObseravble
import kotlinx.android.synthetic.main.fragment_first.*

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class FirstFragment : Fragment() {
    private lateinit var mViewModel: MyObseravble
    private lateinit var mAdapter: AQIAdapter
    private var mIsRefreshClicked = true

    companion object {
        fun newInstance() =
            FirstFragment()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initModel()
        initSubscribe()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setListener()
        setupRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        fab?.visibility = View.VISIBLE
    }

    override fun onPause() {
        super.onPause()
        fab?.visibility = View.GONE
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    private fun initModel() {
        mViewModel = ViewModelProvider(requireActivity())[MyObseravble::class.java]

    }

    private fun initSubscribe() {
        mViewModel.data.observe(this, Observer {
            progressBar?.visibility = View.GONE
            // To load data only when refresh is called
            if (mIsRefreshClicked) {
                mIsRefreshClicked = false
                val response = Gson().fromJson(it, DataResponse::class.java)
                mAdapter.loadData(response)
            }
        })


    }


    private fun setListener() {
        fab?.setOnClickListener {
            mIsRefreshClicked = true
        }
    }

    private fun setupRecyclerView() {
        mAdapter = AQIAdapter(onItemClick)
        val layoutManager = LinearLayoutManager(activity)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = mAdapter

    }

    private val onItemClick = { cityName: String ->
        (activity as MainActivity).addFragment(SecondFragment.newInstance(cityName))
    }


}