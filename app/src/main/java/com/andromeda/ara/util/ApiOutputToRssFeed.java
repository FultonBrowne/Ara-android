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

import com.andromeda.ara.R;
import com.andromeda.ara.client.models.FeedModel;
import com.andromeda.ara.models.OutputModel;

import java.util.ArrayList;

public class ApiOutputToRssFeed {
    public synchronized ArrayList<FeedModel> main(ArrayList<OutputModel> tofeed) {
        ArrayList<FeedModel> feedModels = new ArrayList<>();
        System.out.println(R.string.starting_step_2);
        System.out.println(tofeed.size());
        // System.out.println(tofeed.get(1).link.toString());

        try {


            for (int i = 0; i <= feedModels.size(); i++) {
                System.out.println(i);
                String mInfo = tofeed.get(i).getDescription();
                String mTitle = tofeed.get(i).getTitle();
                System.out.println(mTitle);
                String mlink = tofeed.get(i).getLink();
                String mOut = tofeed.get(i).getOutputTxt();
                String mPic = tofeed.get(i).getImage();
                FeedModel mainModel = new FeedModel(mInfo, mlink, mTitle, mPic, mOut, true);
                System.out.println(mOut);
                feedModels.add(mainModel);


            }
        } catch (Exception ignored) {
        }

        return feedModels;

    }

}

