package net.nikhilroy.mynotes.viewModels

import android.os.Environment
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.io.*

class MainActivityViewModel : ViewModel() {
    private val notes = MutableLiveData<String>()
    private val TAG = MainActivityViewModel::class.java.name
    val FILE_NAME = "userNotes.txt"
    var externalFile : File? = null

    fun getNotes() = notes

    fun setNotesFile(dir: File?) {
        externalFile = File(dir, FILE_NAME)
    }

    fun readSavedNotes() {
        var data = ""
        if (isExternalStorageReadable() || true) {
            var fileInputStream = FileInputStream(externalFile)
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            val stringBuilder: StringBuilder = StringBuilder()
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                stringBuilder.append(text)
            }
            data = stringBuilder.toString()
            fileInputStream.close()
        }
        notes.value = data
    }

    fun writeToExternalStorage(notes: String) {
        if (isExternalStorageWritable()) {
            val fos = FileOutputStream(externalFile)
            fos.write(notes.toByteArray())
            fos.close()
            Log.i(TAG, "Write success!")
        }
    }

    fun isExternalStorageWritable(): Boolean {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED
    }

    fun isExternalStorageReadable(): Boolean {
        return Environment.getExternalStorageState() in
                setOf(Environment.MEDIA_MOUNTED, Environment.MEDIA_MOUNTED_READ_ONLY)
    }

    fun saveNotes(notes: String) {
        writeToExternalStorage(notes)
    }


}