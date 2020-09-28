package com.example.contactlist

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.contactlist.Adapter.UserAdapter
import com.example.contactlist.Data.User
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    lateinit var usersList: List<User>
    val myRequestId = 22

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissions()
    }

    private fun permissions() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS),
                myRequestId
            )
        } else {
            createRecyclerView()
        }
    }

    private fun createRecyclerView() {
        val viewManager = StaggeredGridLayoutManager(3, 1)
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = UserAdapter(fetchAllContacts()) {
                Toast.makeText(
                    this@MainActivity,
                    "Clicked on user $it!", Toast.LENGTH_SHORT
                ).show()
                val sendIntent: Intent = Intent(Intent.ACTION_CALL).apply {
                    data = Uri.parse("tel:${it.number}")
                }

                startActivity(sendIntent)
            }
            setHasFixedSize(true)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            myRequestId -> {
                if (grantResults.isNotEmpty() && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED
                ) {
                    createRecyclerView()
                } else {
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    fun Context.fetchAllContacts(): List<User> {
        contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)
            .use { cursor ->
                if (cursor == null) return emptyList()
                val builder = ArrayList<User>()
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)) ?: "N/A"
                    val surname =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI)) ?: "N/A"
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)) ?: "N/A"

                    builder.add(User(name, surname, phoneNumber))
                }
                return builder
            }
    }
    fun getRealPathFromURI(contentURI: Uri): String? {
        val cursor: Cursor? = getContentResolver().query(contentURI, null, null, null, null)
        return run {
            cursor!!.moveToFirst()
            val idx: Int = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA)
            cursor.getString(idx)
        }
    }
}



