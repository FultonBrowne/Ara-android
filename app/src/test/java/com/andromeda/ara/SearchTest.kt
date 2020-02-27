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

package com.andromeda.ara

import android.app.Activity
import com.andromeda.ara.activitys.MainActivity
import com.andromeda.ara.search.Search
import com.andromeda.ara.skills.SearchFunctions
import com.andromeda.ara.voice.TTS
import org.junit.Test


class SearchTest :SearchFunctions{
    @Test
    fun test(){
        var act:Activity = MainActivity()
        Search().main("test", act, this, null, arrayListOf())
    }

    override fun callBack(m: String, link: String) {

    }

    override fun callForString(m: String): String {
        return ""
    }

}