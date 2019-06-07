package client;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Processor {

    private Cache cache;
    private String URL;
    private String last_modified = "";
    private String redirectURL="";
    private String []lines;
    private String []heads;
    private String pattern="(\\S+):(.+)";
    private Pattern r = Pattern.compile(pattern);
    private Matcher m;

    public Processor(String URL,Cache cache){
        this.cache=cache;
        this.URL=URL;
    }

    public int deal(String line,String head,String body){
        heads = head.split("\n");;
        lines = line.split(" ");
        int statusCode = Integer.parseInt(lines[1]);
        if(statusCode==200){
            this.captureDate();
            cache.addContent(URL,body);
        }
        else if(statusCode==301||statusCode==302){
            this.captureURL();
            return 1;
        }

        else if(statusCode==304){
            return 2;
        }
        return 0;
    }

    private void captureURL(){
        for(String s:heads){
            m = r.matcher(s);
            if(m.find()){
                if(m.group(1).equals("Location")) {
                    this.redirectURL = m.group(2);
                    break;
                }
            }
        }
    }

    private void captureDate(){
        for(String s:heads){
            m = r.matcher(s);
            if(m.find()){
                if(m.group(1).equals("Date")) {
                    last_modified = m.group(2);
                    cache.addDate(URL,last_modified);
                    break;
                }
            }
        }

    }


    public String getRedirectURL(){
        return this.redirectURL;
    }

    public void setURL(String url){this.URL = url;}

}
