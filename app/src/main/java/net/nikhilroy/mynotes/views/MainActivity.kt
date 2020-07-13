package net.nikhilroy.mynotes.views

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import kotlinx.android.synthetic.main.activity_main.*
import net.nikhilroy.mynotes.R
import net.nikhilroy.mynotes.viewModels.MainActivityViewModel
import java.io.*


class MainActivity : AppCompatActivity() {

    val TAG = MainActivity::class.java.name
    val READ_PERMISSION_CODE = 1
    val WRITE_PERMISSION_CODE = 2
    lateinit var viewModel : MainActivityViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel =  ViewModelProvider(this).get(MainActivityViewModel::class.java)

        viewModel.getNotes().observe(this, Observer<String>{ notes ->
            updateNotes(notes)
        })

        viewModel.setNotesFile(getExternalFilesDir(null))

        save_button.setOnClickListener{
            viewModel.saveNotes(note_text_box.text.toString())
        }

        read_button.setOnClickListener {
            viewModel.readSavedNotes()
        }
    }

    fun updateNotes(notes: String) {
        note_text_box.setText(notes)
    }

    fun checkPermissions(){
        isWriteStoragePermissionGranted()
        isReadStoragePermissionGranted()
    }

    fun isReadStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted: READ_EXTERNAL_STORAGE")
                true
            } else {
                Log.v(TAG, "Permission is revoked: READ_EXTERNAL_STORAGE")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_PERMISSION_CODE
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted: READ_EXTERNAL_STORAGE")
            true
        }
    }

    fun isWriteStoragePermissionGranted(): Boolean {
        return if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED
            ) {
                Log.v(TAG, "Permission is granted: WRITE_EXTERNAL_STORAGE")
                true
            } else {
                Log.v(TAG, "Permission is revoked: WRITE_EXTERNAL_STORAGE")
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    WRITE_PERMISSION_CODE
                )
                false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(TAG, "Permission is granted: WRITE_EXTERNAL_STORAGE")
            true
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            WRITE_PERMISSION_CODE -> {
                Log.d(TAG, "External storage")
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(
                        TAG,
                        "Permission: " + permissions[0] + "was " + grantResults[0]
                    )
                } else {
                   //permission not granted
                }
            }
            READ_PERMISSION_CODE -> {
                Log.d(TAG, "External storage")
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Log.v(
                        TAG,
                        "Permission: " + permissions[0] + "was " + grantResults[0]
                    )
                } else {
                    //permission not granted
                }
            }
        }
    }

}