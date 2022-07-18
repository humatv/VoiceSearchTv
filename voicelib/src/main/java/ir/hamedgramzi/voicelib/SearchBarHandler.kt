package ir.hamedgramzi.searchwithvoice

import android.Manifest
import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Typeface
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.app.ActivityCompat.requestPermissions
import ir.hamedgramzi.voicelib.SearchBar
import ir.hamedgramzi.voicelib.SearchResultProvider


class SearchBarHandler(
    private val activity: Activity,
    private val searchBar: SearchBar,
    private val mProvider: SearchResultProvider?,
    var language: String = LANGUAGE_FA,
    var country: String = COUNTRY_IR
) {

    companion object {
        private const val TAG = "SearchBarHandler"
        const val LANGUAGE_EN = "en"
        const val COUNTRY_US = "US"
        const val LANGUAGE_FA = "fa"
        const val COUNTRY_IR = "IR"
        const val AUDIO_PERMISSION_REQUEST_CODE = 0
    }

    private var mSpeechRecognizer: SpeechRecognizer? = null

    private val mPermissionListener =
        SearchBar.SearchBarPermissionListener {
            requestPermissions(
                activity,
                arrayOf(Manifest.permission.RECORD_AUDIO),
                AUDIO_PERMISSION_REQUEST_CODE
            )
        }

    init {
        handle()
    }

    private fun handle() {
        searchBar.setSearchBarListener(object : SearchBar.SearchBarListener {
            override fun onSearchQueryChange(query: String) {
                mProvider?.onQueryTextChange(query)
            }

            override fun onSearchQuerySubmit(query: String) {
                mProvider?.onQueryTextSubmit(query)
            }

            override fun onKeyboardDismiss(query: String) {
                mProvider?.onQueryComplete()
            }
        })
        searchBar.setPermissionListener(mPermissionListener)
    }


    fun setTypeface(typeface: Typeface){
        searchBar.setTypeface(typeface)
    }

    fun requestFocus() {
        searchBar.requestFocus()
    }

    fun resume() {
        if (mSpeechRecognizer == null) {
            mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(activity)
            searchBar.setLanguage(language, country)
            searchBar.setSpeechRecognizer(mSpeechRecognizer)
            activity.registerReceiver(receiver, IntentFilter("ir.huma.action.newVoiceSearch"))
        }
    }

    fun startRecognition() {
        resume()
        searchBar.startRecognition()
    }

    fun stop() {
        if (null != mSpeechRecognizer) {
            searchBar.setSpeechRecognizer(null)
            mSpeechRecognizer!!.destroy()
            mSpeechRecognizer = null
        }
    }


    fun destroy() {
        stop()
        try {
            activity.unregisterReceiver(receiver)
        } catch (e: Exception) {

        }
    }

    private var receiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.d(TAG, "onReceive: startRecognition")
            mProvider?.onVoiceButtonPress()
            startRecognition()
        }
    }

}