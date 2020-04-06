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

package com.andromeda.ara.client.search

import com.andromeda.ara.client.models.TabModel
import kotlin.reflect.KClass


interface Actions {
    fun runActions(action:String, arg1:String, arg2:String)
    fun <T> parseYaml(yaml:String): T
    companion object{
        const val CALL = "CALL"
        const val APP = "OPEN_APP"
        const val TEXT = "TEXT"
        const val MEDIA = "TOG_MEDIA"
        const val STOP_TIMERS = "STOPTIMERS"
        const val OUTPUT = "OUTPUT"
        const val MAPS = "MAPS"
        const val FLASH = "FLASH"
        const val TIMER = "TIMER"
        const val RESPOND = "RESPOND"
    }
}