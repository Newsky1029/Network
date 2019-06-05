package client;

import java.util.HashMap;
import java.util.Map;

public class Cache {
    Map dateMap;
    Map contentMap;

    public Cache(){
        dateMap = new HashMap();
        contentMap = new HashMap();
    }

    public void addDate(String url,String last_modified){
        dateMap.put(url,last_modified);
    }

    public void addContent(String url,String content){
        contentMap.put(url,content);
    }

    public String getLastModified(String url){
        if(dateMap.containsKey(url))return (String)dateMap.get(url);
        else return null;
    }

    public String getContent(String url){
        if(contentMap.containsKey(url))return (String)contentMap.get(url);
        else return null;
    }

}
