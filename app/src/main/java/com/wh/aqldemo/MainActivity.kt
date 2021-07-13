package com.wh.aqldemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.work.*
import com.google.android.material.snackbar.Snackbar
import com.wh.App
import com.wh.viewmodel.MyObseravble
import com.wh.workmanager.SocketReconnectionScheduler
import kotlinx.android.synthetic.main.activity_main.*
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI


class MainActivity : AppCompatActivity() {
    private lateinit var webSocketClient: WebSocketClient
    private lateinit var mViewModel: MyObseravble

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initModel()
        init()

    }

    private fun init() {
        replaceFragment(FirstFragment.newInstance())
        initWebSocket()
    }

    private fun initModel() {
        mViewModel = ViewModelProvider(this)[MyObseravble::class.java]

    }


    override fun onResume() {
        super.onResume()
        App.setInForeground(true) // App in background

    }

    override fun onPause() {
        super.onPause()
        App.setInForeground(false) // App in foreground

    }

    override fun onDestroy() {
        super.onDestroy()
        webSocketClient.close()
    }

    private fun initWebSocket() {
        val coinbaseUri: URI = URI(WEB_SOCKET_URL)
        createWebSocketClient(coinbaseUri)
        webSocketClient.connect()

    }


    // Socket listener
    private fun createWebSocketClient(coinbaseUri: URI?) {
        webSocketClient = object : WebSocketClient(coinbaseUri) {

            override fun onOpen(handshakedata: ServerHandshake?) {
                Log.d(TAG, "onOpen")
                //subscribe()
            }

            override fun onMessage(message: String?) {
                Log.d(TAG, "onMessage: $message")
                mViewModel.data.postValue(message)// Send data to observer

            }

            override fun onClose(code: Int, reason: String?, remote: Boolean) {
                Log.d(TAG, "onClose")
                showSnackBar(getString(R.string.alert_socket_closed))
                schedule()
            }

            override fun onError(ex: Exception?) {
                Log.d("createWebSocketClient", "onError: ${ex?.message}")
                schedule()
                showSnackBar("onError: ${ex?.message}")
            }

        }
    }


    companion object {
        const val WEB_SOCKET_URL = "ws://city-ws.herokuapp.com/"
        const val TAG = "MainActivity"
    }

    fun addFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().setCustomAnimations(
            R.anim.slide_from_right, R.anim.slide_to_left,
            R.anim.slide_from_left, R.anim.slide_to_right
        ).addToBackStack("").add(R.id.container, fragment)
            .commit()
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment)
            .commit()

    }

    // To handle socket in case of disconnect
    private fun schedule() {

        val oneTimeWorkRequest = SocketReconnectionScheduler.schedule()
        // To subscribe observer in main thread
        runOnUiThread {
            oneTimeWorkRequest?.let {
                WorkManager.getInstance(this).enqueue(it)

                // In your UI (activity, fragment, etc)
                WorkManager.getInstance(this).getWorkInfoByIdLiveData(it.id)
                    .observe(this, Observer { workInfo ->
                        // Check if the current work's state is "successfully finished"
                        if (workInfo != null && workInfo.state == WorkInfo.State.SUCCEEDED) {
                            if (webSocketClient.isClosed)
                                initWebSocket()
                        }
                    })
            }
        }
        WorkManager.getInstance(this).enqueue(oneTimeWorkRequest)

    }

    private fun showSnackBar(message: String) {
        val snackbar = Snackbar
            .make(cl, message, Snackbar.LENGTH_LONG)
        snackbar.show()
    }

}