package com.example.practice_app

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.FileProvider
import androidx.fragment.app.DialogFragment
import java.io.File

/**
 * A simple [Fragment] subclass.
 * Use the [ChangePhotoDialog.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChangePhotoDialog : DialogFragment(), DialogInterface.OnClickListener{
    private val SOURCE = arrayOf(
        "Open Camera",
        "Select from Gallery"
    )
    private lateinit var sourcePickList: ListView
    private lateinit var cameraResult: ActivityResultLauncher<Intent>
    private lateinit var galleryResult: ActivityResultLauncher<Intent>

    companion object{
        const val DIALOG_KEY = "dialog"
        const val TEST_DIALOG = 1
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        lateinit var ret: Dialog
        val bundle = arguments
        val dialogId = bundle?.getInt(DIALOG_KEY)
        if (dialogId == TEST_DIALOG) {
            val builder = AlertDialog.Builder(requireActivity())
            val view: View = requireActivity().layoutInflater.inflate(R.layout.dialog_change_photo, null)
            sourcePickList = view.findViewById(R.id.sourcePickListView)
            setListView()
            builder.setView(view)
            builder.setTitle("Pick Profile picture")
            ret = builder.create()
        }

        galleryResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),{
            if(it.resultCode == Activity.RESULT_OK && it.data != null) {
                val selectedImg: Uri = it.data?.data!!
                MainActivity.profileImg.setImageURI(selectedImg)
                dismiss()
            }
        })

        cameraResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult(),{
            if(it.resultCode == Activity.RESULT_OK) {
                println("The imgSaveLocation: " + MainActivity.tempImgLocation)
                val bitmap = Util.getBitmap(requireContext(), MainActivity.tempImgLocation)
                MainActivity.profileImg.setImageBitmap(bitmap)
                dismiss()
            }
        })
        return ret
    }

    fun setListView() {
        val arrayAdapter: ArrayAdapter<String> = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_list_item_1,
            SOURCE)
        sourcePickList.adapter = arrayAdapter
        sourcePickList.setOnItemClickListener(){ parent: AdapterView<*>, view: View, position: Int, id: Long ->
            println("debug: parent: $parent | view: $view | position: $position | id: $id")
            onListsClicked(position, view)
        }
    }

    fun onListsClicked(listPosition: Int, view:View) {
        when (listPosition) {
            0 -> useCameraToTakePhoto(view)
            1 -> useGalleryToLoadPhoto(view)
        }
    }

    fun useCameraToTakePhoto(view: View) {
        Util.checkPermissions(requireActivity())
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.putExtra(MediaStore.EXTRA_OUTPUT, MainActivity.tempImgLocation)
        cameraResult.launch(intent)
    }

    fun useGalleryToLoadPhoto(view: View) {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        galleryResult.launch(intent)
    }

    override fun onClick(dialog: DialogInterface, item: Int) {
        println("onClick inside ChangePhotoDialog has been clicked!")
    }
}