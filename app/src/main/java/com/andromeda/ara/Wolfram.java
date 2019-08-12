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
