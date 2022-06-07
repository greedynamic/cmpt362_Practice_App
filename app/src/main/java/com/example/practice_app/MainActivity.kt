package com.example.practice_app

import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import java.io.File
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var nameText: EditText

    private lateinit var userDataPrefs: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var savedImgName = "lab1_saved_img.jpg"
    private var tempImgName = "lab1_temp_img.jpg"
    private val PREFS_FILE_NAME = "Lab1PrefsFile_UserData"

    companion object{
        val EXTRA_KEY = "extra key"
        lateinit var profileImg: ImageView
        lateinit var tempImgLocation:Uri
        lateinit var savedImgLocation:Uri
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        profileImg = findViewById(R.id.photoProfile)
        nameText = findViewById(R.id.nameInput)

        userDataPrefs = getSharedPreferences(PREFS_FILE_NAME, MODE_PRIVATE)
        editor = userDataPrefs.edit()

        nameText.setText(userDataPrefs.getString("name", "Your Name"))

        loadSavedImage()
    }

    fun loadSavedImage() {
        val savedImg = File(getExternalFilesDir(null), savedImgName)
        savedImgLocation = FileProvider.getUriForFile(this, "com.example.practice_app", savedImg)

        val tempImage = File(getExternalFilesDir(null), tempImgName)
        tempImgLocation = FileProvider.getUriForFile(this, "com.example.practice_app", tempImage)

        if(savedImg.exists()){
            val bitmap = Util.getBitmap(this, savedImgLocation)
            println("The imgSaveLocation inside the oldImg check: " + savedImgLocation)
            profileImg.setImageBitmap(bitmap)
        }
    }

    fun onButtonClick(view: View) {
        val edittext:EditText = findViewById(R.id.editText)
        val message:String = edittext.text.toString()
//        println("debug: $message")
        val intent: Intent = Intent(this, DisplayActivity::class.java)

        val bundle:Bundle = Bundle()
        bundle.putString(EXTRA_KEY, message)
        intent.putExtras(bundle)
        // intent.putExtra(EXTRA_KEY, message)
        startActivity(intent)
    }

    fun onChangeBtnToGetPhoto(view: View) {
        val myDialog = ChangePhotoDialog()
        val bundle = Bundle()
        bundle.putInt(ChangePhotoDialog.DIALOG_KEY, ChangePhotoDialog.TEST_DIALOG)
        myDialog.arguments = bundle
        myDialog.show(supportFragmentManager, "Change profile photo dialog")
    }

    fun onClickBtnToSave(view: View) {
        val oldImg = File(getExternalFilesDir(null), savedImgName)
        val newImg = File(getExternalFilesDir(null), tempImgName)
        newImg.copyTo(oldImg, true)

        val name:String = nameText.text.toString()
        editor.putString("name", name)
        editor.apply()

        Toast.makeText(this, "Saved!", Toast.LENGTH_SHORT).show()
        this.finishAffinity();
    }

    fun onClickBtnToCancel(view: View) {
        val newImg = File(getExternalFilesDir(null), tempImgName)
        newImg.delete()
        this.finishAffinity();
    }

/*
    fun onCameraBtnToTakePhoto(view: View) {
        Util.checkPermissions(this)
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imgSaveLocation)
        cameraResult.launch(intent)
    }

    fun onGalleryBtnToLoadPhoto(view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)
    }
*/
}