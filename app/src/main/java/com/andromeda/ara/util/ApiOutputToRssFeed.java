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

package com.andromeda.ara.util;

import com.andromeda.ara.OutputModel;
import com.andromeda.ara.RssFeedModel;

import java.util.ArrayList;

public class ApiOutputToRssFeed {
    public ArrayList<RssFeedModel> main(ArrayList<OutputModel> tofeed) {
        ArrayList<RssFeedModel> feedModels = new ArrayList<>();
        System.out.println("Starting step 2");
        System.out.println(tofeed.size());
        // System.out.println(tofeed.get(1).link.toString());


        for (int i = 1; i <= feedModels.size(); i++) {
            String mInfo = tofeed.get(i).description;
            String mTitle = tofeed.get(i).title;
            System.out.println(mTitle);
            String mlink = tofeed.get(i).link;
            String mPic = "";
            RssFeedModel mainModel = new RssFeedModel(mInfo, mlink, mTitle, mPic);
            feedModels.add(mainModel);


        }
        return feedModels;

    }

}

