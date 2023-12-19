package com.example.a2048

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class Dialog(var message: String, var mainActivity: MainActivity) : DialogFragment() {
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.setMessage(message)
                .setPositiveButton("Начать заново") { dialog, id ->
                    mainActivity.game.restart()
                    mainActivity.updateView()
                }
            builder.setCancelable(false);
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }
}