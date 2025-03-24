package com.example.androidtvexample.search

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.media.AudioAttributes
import android.media.AudioManager
import android.media.SoundPool
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.SparseIntArray
import androidx.leanback.R

interface SearchListener{
    fun onQueryChange(query: String)

    fun onQuerySubmit(query: String)
}

class VoiceSearchManager(
    private val context: Context,
//    private val onQueryChange: (String) -> Unit,
//    private val onQuerySubmit: (String) -> Unit,
) {
    private var searchListener: SearchListener? = null
    private var speechRecognizer: SpeechRecognizer? = null
    private val handler = Handler(Looper.myLooper()!!)
    private var soundMap = SparseIntArray()
    var isRecognizing = false

    private var soundPool: SoundPool = SoundPool.Builder().setMaxStreams(2)
        .setAudioAttributes(
            AudioAttributes.Builder().setLegacyStreamType(AudioManager.STREAM_SYSTEM).build()
        ).build()

    init {
        loadSounds(context)
        speechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    }

    fun setSearchListener(listener: SearchListener) {
        searchListener = listener
    }

    fun stopRecognition() {
        if (!isRecognizing) return
        cancelRecognition()
    }

    fun startRecognition() {
        if (isRecognizing) return
        val res = context.checkCallingOrSelfPermission(Manifest.permission.RECORD_AUDIO)
        if (PackageManager.PERMISSION_GRANTED != res) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                return
            }
        }
        isRecognizing = true
        setRecognitionListener()
        val recognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
        }
        speechRecognizer?.startListening(recognizerIntent)
    }

    fun submitRecognition() {
        if (!isRecognizing) return
        cancelRecognition()
    }

    fun finish() {
        cancelRecognition()
    }

    private fun cancelRecognition() {
        isRecognizing = false
        speechRecognizer?.cancel()
        speechRecognizer?.setRecognitionListener(null)
    }

    private fun setRecognitionListener() {
        speechRecognizer?.setRecognitionListener(object : RecognitionListener {
            override fun onReadyForSpeech(bundle: Bundle) {
                playSearchOpen()
            }

            override fun onBeginningOfSpeech() {}

            override fun onRmsChanged(rmsdB: Float) {}

            override fun onBufferReceived(bytes: ByteArray) {}

            override fun onEndOfSpeech() {}

            override fun onError(error: Int) {
                stopRecognition()
                playSearchFailure()
            }

            override fun onResults(bundle: Bundle) {
                val matches = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (matches != null && matches.isNotEmpty()) {
                    submitQuery(matches.first())
                }
                submitRecognition()
                playSearchSuccess()
            }

            override fun onPartialResults(bundle: Bundle) {
                val results = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
                if (results == null || results.size == 0) {
                    return
                }
                searchListener?.onQueryChange(results.first())
            }

            override fun onEvent(i: Int, bundle: Bundle) {

            }
        })
    }

    private fun submitQuery(query: String) {
        if (query.isNotEmpty()) {
            searchListener?.onQuerySubmit(query)
        }
    }

    private fun loadSounds(context: Context) {
        val sounds = intArrayOf(
            R.raw.lb_voice_failure,
            R.raw.lb_voice_open,
            R.raw.lb_voice_no_input,
            R.raw.lb_voice_success
        )
        for (sound in sounds) {
            soundMap.put(sound, soundPool.load(context, sound, 1))
        }
    }

    private fun play(resId: Int) {
        handler.post {
            val sound = soundMap.get(resId)
            soundPool.play(sound, FULL_LEFT_VOLUME, FULL_RIGHT_VOLUME, DEFAULT_PRIORITY,
                DO_NOT_LOOP, DEFAULT_RATE
            )
        }
    }
    private fun playSearchOpen() {
        play(R.raw.lb_voice_open)
    }

    private fun playSearchFailure() {
        play(R.raw.lb_voice_failure)
    }

    private fun playSearchSuccess() {
        play(R.raw.lb_voice_success)
    }

    companion object {
        const val FULL_LEFT_VOLUME = 1.0f
        const val FULL_RIGHT_VOLUME = 1.0f
        const val DEFAULT_PRIORITY = 1
        const val DO_NOT_LOOP = 0
        const val DEFAULT_RATE = 1.0f
    }


}