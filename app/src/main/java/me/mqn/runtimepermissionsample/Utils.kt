package me.mqn.runtimepermissionsample

import android.view.View
import android.view.ViewGroup
import android.widget.Button

object Utils {

    fun ViewGroup.addButton(label: String, onclick: View.OnClickListener) {
        val button = Button(context)
        button.layoutParams =
            ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        button.text = label
        button.setOnClickListener(onclick)
        this.addView(button)
    }
}