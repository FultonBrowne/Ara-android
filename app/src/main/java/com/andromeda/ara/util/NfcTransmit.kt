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

import android.app.Activity
import android.app.PendingIntent
import android.content.Intent
import android.nfc.NfcAdapter
import org.ndeftools.Message
import org.ndeftools.MimeRecord
import org.ndeftools.externaltype.AndroidApplicationRecord


class NfcTransmit {
    fun main(act:Activity) {
        val aar = AndroidApplicationRecord();
        aar.packageName = "com.andromeda.ara";
        val mimeRecord = MimeRecord()
        mimeRecord.mimeType = "text/plain"
        mimeRecord.data = "This is my data".toByteArray(charset("UTF-8"))
        val message = Message() //  org.ndeftools.Message
        message.add(aar)
        message.add(mimeRecord)
        val nfcAdapter = NfcAdapter.getDefaultAdapter(act)
        val nfcPendingIntent = PendingIntent.getActivity(act, 0, Intent(act, this.javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0)


    }


}
