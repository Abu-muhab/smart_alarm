package com.abumuhab.smartalarm.util

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.textfield.TextInputLayout

fun removeEditTextError(input: TextInputLayout) {
    input.isErrorEnabled = false
    input.error = null;
}

fun hideSoftKeyboard(context: Context, view: View) {
    val imm: InputMethodManager =
        context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
    view.clearFocus()
}


fun showBasicMessageDialog(message: String, activity: Activity) {
    val builder: AlertDialog.Builder = AlertDialog.Builder(activity)
    builder.setMessage(message)
//    builder.apply {
//        this.setPositiveButton("OKAY", DialogInterface.OnClickListener { _, _ ->
//
//        })
//    }
    val dialog: AlertDialog? = builder.create()
    dialog?.show()
}