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

package com.andromeda.ara.skills

import android.accounts.Account
import android.content.Context
import com.andromeda.ara.util.YamlModel
import com.fasterxml.jackson.core.type.TypeReference
import java.util.*


class RunActions {
    fun doIt(yaml: ArrayList<YamlModel>?, searchTerm: String, ctx:Context) {

        val arg1: String
        if (yaml != null) {
            //for (i in yaml) {
                if (yaml[0].action == "OPEN_APP") {
                    arg1 = if (yaml[0].arg1 == "TERM") searchTerm
                    else yaml[0].arg1
                    OpenApp().openApp(arg1, ctx)
                }
            else if(yaml[0].action == "CALL"){
                    arg1 = if (yaml[0].arg1 == "TERM") searchTerm
                    else yaml[0].arg1
            Phone().call(arg1, ctx)

                }


            //}
        }


    }
}