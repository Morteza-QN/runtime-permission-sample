package me.mqn.runtimepermissionsample

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat

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

    fun AppCompatActivity.checkPermission(permission: String) =
        ActivityCompat.checkSelfPermission(this, permission)

    fun AppCompatActivity.shouldRequestPermissionRationale(permission: String) =
        ActivityCompat.shouldShowRequestPermissionRationale(this, permission)

    fun AppCompatActivity.requestAllPermissions(permissionsArray: Array<String>, requestCode: Int) {
        ActivityCompat.requestPermissions(this, permissionsArray, requestCode)
    }

    fun isLowerThanAndroid10() = Build.VERSION.SDK_INT < Build.VERSION_CODES.R

    fun isAndroid13OrGreater() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU

    fun Context.openSettingPage() {
        val intent = Intent()
        intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri: Uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        startActivity(intent)
    }

    fun Context.showSettingsDialog() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.permission_alert))
        builder.setMessage(getString(R.string.goto_setting_and_permission))
        builder.setPositiveButton(getString(R.string.str_settings)) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
            openSettingPage()
        }
        builder.setNegativeButton(getString(R.string.str_cancel)) { dialog: DialogInterface, _: Int ->
            dialog.dismiss()
        }
        val dialog = builder.create()
        dialog.setCancelable(false)
        if (!dialog.isShowing) {
            dialog.show()
        }
    }
}