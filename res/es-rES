/*
 * Copyright (c) 2020. Fulton Browne
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

package com.andromeda.ara.util

import android.R
import android.app.Activity
import android.app.AlertDialog
import android.widget.Toast
import com.microsoft.appcenter.push.PushListener
import com.microsoft.appcenter.push.PushNotification


class PushUtil : PushListener {
    override fun onPushNotificationReceived(activity: Activity, pushNotification: PushNotification) { /* The following notification properties are available. */
        val title = pushNotification.title
        val message = pushNotification.message
        val customData = pushNotification.customData
        /*
         * Message and title cannot be read from a background notification object.
         * Message being a mandatory field, you can use that to check foreground vs background.
         */if (message != null) { /* Display an alert for foreground push. */
            val dialog = AlertDialog.Builder(activity)
            if (title != null) {
                dialog.setTitle(title)
            }
            dialog.setMessage(message)
            if (customData.isNotEmpty()) {
                dialog.setMessage(message + "\n" + customData)
            }
            dialog.setPositiveButton(R.string.ok, null)
            dialog.show()
        } else { /* Display a toast when a background push is clicked. */
            val pre = pushNotification.customData["db"]?.get(0)
            val end = pushNotification.customData["db"]?.get(1)
            val act = pushNotification.customData["db"]?.get(2)
            OnDeviceSkills(activity.applicationContext).insert(pre.toString(), end.toString(), act.toString())

            //push new skills


            Toast.makeText(activity, "test", Toast.LENGTH_LONG).show() // For example R.string.push_toast would be "Push clicked with data=%1s"
        }
    }
}
