/*
 * Copyright (c) 2019. Fulton Browne
 *  This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.andromeda.ara.phoneData

import android.Manifest
import android.R
import android.app.Activity
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Context
import android.content.DialogInterface.OnDismissListener
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Build
import android.provider.ContactsContract
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat


class GetContacts {

    val PERMISSIONS_REQUEST_READ_CONTACTS = 1
    fun search(search:String, ctx:Context, act: Activity): String {
        requestContactPermission(ctx, act)
        val cr: ContentResolver = ctx.contentResolver
        val cur: Cursor? = cr.query(ContactsContract.Contacts.CONTENT_URI,
                null, null, null, null)
        var no = ""

        if ((cur?.count ?: 0) > 0) {
            while (cur != null && cur.moveToNext()) {
                val id: String = cur.getString(
                        cur.getColumnIndex(ContactsContract.Contacts._ID))
                val name: String = cur.getString(cur.getColumnIndex(
                        ContactsContract.Contacts.DISPLAY_NAME))
                if (cur.getInt(cur.getColumnIndex(
                                ContactsContract.Contacts.HAS_PHONE_NUMBER)) > 0) {
                    val pCur: Cursor? = cr.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?", arrayOf(id), null)
                    while (pCur!!.moveToNext()) {
                        val phoneNo: String = pCur.getString(pCur.getColumnIndex(
                                ContactsContract.CommonDataKinds.Phone.NUMBER))
                        if(name == search){
                            no = phoneNo
                        }

                    }
                    pCur.close()
                }
            }
        }
        cur?.close()
        return no

    }
     fun requestContactPermission(ctx:Context, act:Activity)  {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(ctx, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(act,
                                Manifest.permission.READ_CONTACTS)) {
                    val builder = AlertDialog.Builder(ctx)
                    builder.setTitle("Read Contacts permission")
                    builder.setPositiveButton(R.string.ok, null)
                    builder.setMessage("Please enable access to contacts.")
                    builder.setOnDismissListener(OnDismissListener { requestPermissions(act ,arrayOf(Manifest.permission.READ_CONTACTS), PERMISSIONS_REQUEST_READ_CONTACTS) })
                    builder.show()
                }
            }
        }
    }
}