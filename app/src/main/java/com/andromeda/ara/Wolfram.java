package com.andromeda.ara;

import com.wolfram.alpha.WAEngine;
import com.wolfram.alpha.WAException;
import com.wolfram.alpha.WAPlainText;
import com.wolfram.alpha.WAPod;
import com.wolfram.alpha.WAQuery;
import com.wolfram.alpha.WAQueryResult;
import com.wolfram.alpha.WASubpod;

import java.util.List;

public class Wolfram {
    String title;
    String info;
    List<RssFeedModel> Out;



    public RssFeedModel Wolfram1(String input){
        WAEngine engine = new WAEngine();
        engine.setAppID("HEPHXJ-8EHWK2GA2X");
        engine.addFormat("plaintext");

        // Create the query.
        WAQuery query = engine.createQuery();

        // Set properties of the query.
        query.setInput(input);
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
        RssFeedModel rssFeedModel = new RssFeedModel(info, "wolfram.com", title);
        return rssFeedModel;

    }

}
