package com.andromeda.ara;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;
import com.wolfram.alpha.*;

public class SearchSkill {
    public void SearchSkill(String search){
        String search1=search.replace(' ','+');
        String url = "http://api.wolframalpha.com/v2/query?appid=DEMO&input=";
        String done = url+search1+"&includepodid=Result";
        String title;
        String info;
        String link;

        try {
            URL url1 = new URL(done);
            HttpURLConnection conn = (HttpURLConnection)url1.openConnection();
            conn.setRequestMethod("GET");
            int responsecode = conn.getResponseCode();
            if(responsecode != 200) {
                throw new RuntimeException("HttpResponseCode: " + responsecode);
            }
            else{
                Scanner sc = new Scanner(url1.openStream());
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            link = "err";
            info = "err";
            title = "err";
        } catch (IOException e) {
            e.printStackTrace();
            link = "err";
            info = "err";
            title = "err";
        }



    }
}
