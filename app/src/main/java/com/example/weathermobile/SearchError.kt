package com.example.weathermobile

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class SearchError: DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog
            .Builder(requireContext())
            .setTitle("Something Went Wrong")
            .setMessage("ZIP code error, please try re-entering ZIP code again")
            .setPositiveButton("Retry", null)
            .create()
    }

    companion object{
        const val TAG = "SearchError"
    }
}