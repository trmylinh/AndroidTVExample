package com.example.androidtvexample.search

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.FragmentActivity
import com.example.androidtvexample.R
import com.example.androidtvexample.databinding.ActivitySearchBinding

class SearchActivity : FragmentActivity() {
    private lateinit var binding: ActivitySearchBinding
    private lateinit var mVoiceSearchFragment: VoiceSearchFragment
    private val TAG = "SearchActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mVoiceSearchFragment = (supportFragmentManager
            .findFragmentById(R.id.search_fragment) as VoiceSearchFragment)
        checkRunTimePermission()
    }

    private fun checkRunTimePermission() {
        Log.e(TAG, "==== checkRuntime Permission")

        val permissionArrays = arrayOf(Manifest.permission.RECORD_AUDIO)
        requestPermissions(permissionArrays, 11111)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
        deviceId: Int
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults, deviceId)
        Log.e(TAG, "==== OnPermission Result")

        if (11111 == requestCode && grantResults.isNotEmpty()) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show()
            }
            mVoiceSearchFragment.setRecognitionListener()
        }
    }
}