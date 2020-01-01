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

package com.andromeda.ara.Ui

import androidx.compose.Composable
import androidx.compose.unaryPlus
import androidx.ui.core.Dp
import androidx.ui.foundation.DrawImage
import androidx.ui.layout.ConstrainedBox
import androidx.ui.layout.DpConstraints
import androidx.ui.res.imageResource
import com.andromeda.ara.R

class Views {
    @Composable
    fun constLayout() {
        ConstrainedBox(constraints = DpConstraints.tightConstraints(Dp.Infinity, Dp.Infinity)) {
            val image = +imageResource(R.drawable.aboutpic)

            DrawImage(image)

        }
    }

    @Composable
    fun constLayout(height: Float, width: Float) {
        ConstrainedBox(constraints = DpConstraints.tightConstraints(Dp(width), Dp(height))) {

        }
    }
}