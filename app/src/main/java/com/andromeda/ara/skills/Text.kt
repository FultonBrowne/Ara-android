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
package com.andromeda.ara.skills

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.net.Uri
import com.andromeda.ara.phoneData.GetContacts

class Text {
    fun sendText(search: String, ctx: Context) {
        val num = GetContacts().search(search, ctx)
        val uri = Uri.parse("smsto:$num")
        val intent = Intent(Intent.ACTION_SENDTO, uri)
        intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
        intent.putExtra("sms_body", "The SMS text")
        ctx.startActivity(intent)
    }

}