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

package com.andromeda.ara;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
class OutputModel {
    private String title;
    private String link;
    private String description;
    private String OutputTxt;
    private String exes;

    OutputModel(String title, String description, String link, String image, String OutputTxt, String exes) {
        this.title = title;
        this.link = link;
        this.description = description;
        this.OutputTxt = OutputTxt;
        this.exes = exes;
    }
}
