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

class SkillsMap {
    fun map(mainVal: Int): String? {
        val mainMap = mapOf(0 to "CALL", 1 to "TEXT", 2 to "TOG_MEDIA", 3 to "OUTPUT", 4 to "OPEN_APP", 5 to  "FLASH")
        return mainMap[mainVal]

    }

    fun mapFlip(mainVal: String): Int? {
        val mainMap = mapOf("CALL" to 0, "TEXT" to 1, "TOG_MEDIA" to 2, "OUTPUT" to 3, "OPEN_APP" to 4, "FLASH" to 5)
        return mainMap[mainVal]

    }

}