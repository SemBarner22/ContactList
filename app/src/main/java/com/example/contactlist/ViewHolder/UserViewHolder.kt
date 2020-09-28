package com.example.contactlist.ViewHolder

import android.database.Cursor
import android.graphics.drawable.Drawable
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.example.contactlist.Data.User
import com.example.contactlist.MainActivity
import kotlinx.android.synthetic.main.list_h_item.view.*


class UserViewHolder(var root: View) : RecyclerView.ViewHolder(root) {
    fun bind(user: User) {
        with(root) {
            first_name.text = user.firstName
            last_name.setImageURI(Uri.parse(user.lastName))
            number.text = user.number
        }
    }
}

//Uri my_contact_Uri = Uri.withAppendedPath(ContactsContract.Contacts.CONTENT_URI, String.valueOf(Contact_Id));
//InputStream photo_stream = ContactsContract.Contacts.openContactPhotoInputStream(getContentResolver(),my_contact_Uri);
//BufferedInputStream buf =new BufferedInputStream(photo_stream);
//Bitmap my_btmp = BitmapFactory.decodeStream(buf);
//profile.setImageBitmap(my_btmp);

