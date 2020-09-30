package com.example.contactlist

import android.Manifest.permission
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.GridLayoutManager.SpanSizeLookup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.contactlist.Adapter.UserAdapter
import com.example.contactlist.Data.User
import com.example.contactlist.R.plurals.user_amount_strings
import kotlinx.android.synthetic.main.activity_main.*
import java.text.MessageFormat


class MainActivity : AppCompatActivity() {

    val myRequestId = 22
    lateinit var userList: List<User>
//    lateinit var viewManager: GridLayoutManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        permissions()
    }

    private fun permissions() {
        if (ContextCompat.checkSelfPermission(this, permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED ||
            ContextCompat.checkSelfPermission(this, permission.SEND_SMS)
            != PackageManager.PERMISSION_GRANTED

        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(permission.READ_CONTACTS, permission.SEND_SMS),
                myRequestId
            )
        } else {
            createRecyclerView()
        }
    }


    private fun createRecyclerView() {
//        viewManager = GridLayoutManager(this, 2)
        val viewManager = LinearLayoutManager(this)
        userList = fetchAllContacts()
        val amount: String = this.resources.getQuantityString(user_amount_strings,
            userList.size, userList.size)
        Toast.makeText(
            this@MainActivity,
            MessageFormat.format(amount), Toast.LENGTH_SHORT
        ).show()
//        val viewManager = LinearLayoutManager(this)
        myRecyclerView.apply {
            layoutManager = viewManager
            adapter = UserAdapter(userList, {
                Toast.makeText(
                    this@MainActivity,
                    MessageFormat.format(getString(R.string.touch)) + "$it!", Toast.LENGTH_SHORT
                ).show()
                val sendIntent: Intent = Intent(Intent.ACTION_DIAL).apply {
                    data = Uri.parse("tel:${it.number}")
                }
                startActivity(sendIntent)
            }) {
                val uri = Uri.parse("smsto:${it.number}")
                val intent = Intent(Intent.ACTION_SENDTO, uri)
                intent.putExtra("sms_body", "SMS")
                startActivity(intent)
            }
//            viewManager.setSpanSizeLookup(object : SpanSizeLookup() {
//                override fun getSpanSize(position: Int): Int {
//                    return when ((adapter as UserAdapter).getItemViewType(position)) {
//                        UserAdapter.TYPE_HORIZONTAL -> 1
//                        else -> 2
//                    }
//                }
//            })
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
                if (grantResults.isNotEmpty()) {
                    var s = 0
                    for (i in grantResults.indices) {
                        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                            ++s
                        }
                    }
                    if (s == grantResults.size) {
                        createRecyclerView()
                    } else {
                        Toast.makeText(this,
                            MessageFormat.format(getString(R.string.deny)), Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun Context.fetchAllContacts(): List<User> {
        contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
            .use { cursor ->
                if (cursor == null) return emptyList()
                val builder = ArrayList<User>()
                while (cursor.moveToNext()) {
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))
                            ?: "N/A"
                    val surname =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.PHOTO_URI))
                            ?: "N/A"
                    val phoneNumber =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                            ?: "N/A"

                    builder.add(User(name, surname, phoneNumber))
                }
                return builder
            }
    }
}



