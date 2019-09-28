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

import com.wolfram.alpha.*;



public class Wolfram {
    private String title;
    private String info;
    private String link;
    private String image = null;



    public RssFeedModel Wolfram1(String input){
        WAEngine engine = new WAEngine();
        engine.setAppID("HEPHXJ-8EHWK2GA2X");
        //engine.addFormat("plaintext");

        // Create the query.
        WAQuery query = engine.createQuery();

        // Set properties of the query.
        query.setInput(input);
        query.setMagnification(120.0);
        try {
            WAQueryResult queryResult = engine.performQuery(query);
            if (queryResult.isError()) {
                title = "err";
                info = "err";
            } else if (!queryResult.isSuccess()) {
                title = "err";
                info = "err";
            } else {

                for (WAPod pod : queryResult.getPods()) {
                    if (!pod.isError()) {
                        title = pod.getTitle();

                        for (WASubpod subpod : pod.getSubpods()) {
                            for (Object element : subpod.getContents()) {
                                if (element instanceof WAPlainText) {
                                    info = ((WAPlainText) element).getText();
                                    link = queryResult.getQuery().toWebsiteURL();

                                }
                                if (element instanceof WAImage){
                                    image = ((WAImage) element).getURL();
                                }


                            }
                        }

                    }
                }
                // We ignored many other types of Wolfram|Alpha output, such as warnings, assumptions, etc.
                // These can be obtained by methods of WAQueryResult or objects deeper in the hierarchy.
            }
        } catch (WAException e) {
            e.printStackTrace();
        }
        return new RssFeedModel(info, link, title, image);

    }

}
