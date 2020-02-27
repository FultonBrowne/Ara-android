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

package com.andromeda.ara.util;

import com.andromeda.ara.models.DeviceModelIndexed;
import com.andromeda.ara.models.OutputModel;
import com.andromeda.ara.models.SkillsFromDB;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class JsonParse {
    public ArrayList<OutputModel> search(String jsontxt) {
        Gson gson = new Gson();
        return gson.fromJson(jsontxt, new TypeToken<ArrayList<OutputModel>>() {
        }.getType());

    }
    public ArrayList<NewsData> news(String jsontxt) {
        Gson gson = new Gson();
        return gson.fromJson(jsontxt, new TypeToken<ArrayList<NewsData>>() {
        }.getType());

    }

    public ArrayList<SkillsFromDB> skills(String jsontxt) {
        Gson gson = new Gson();
        return gson.fromJson(jsontxt, new TypeToken<ArrayList<SkillsFromDB>>() {
        }.getType());

    }
    public ArrayList<DeviceModelIndexed> iotIndexed(String jsontxt) {
        Gson gson = new Gson();
        return gson.fromJson(jsontxt, new TypeToken<ArrayList<DeviceModelIndexed>>() {
        }.getType());

    }
    public ArrayList<SkillsDBModel> skillsServer(String jsontxt) {
        Gson gson = new Gson();
        return gson.fromJson(jsontxt, new TypeToken<ArrayList<SkillsDBModel>>() {
        }.getType());

    }


}
