package com.sparcedge.team01;

import android.app.Activity;
import android.os.Bundle;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * Created with IntelliJ IDEA.
 * User: swoodruff
 * Date: 8/25/12
 * Time: 11:14 AM
 * To change this template use File | Settings | File Templates.
 */
public class Weather extends Activity {

    protected static HttpContext localContext = new BasicHttpContext();
    protected static CookieStore cookieStore = new BasicCookieStore();

    Weather() {
        Tripppy.LOG("Creating cookie store...");
        localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    public String getForecast(String woeid) {
        String URL = "http://weather.yahooapis.com/forecastrss?w=" + woeid;
        Tripppy.LOG("forecast URL: " + URL);
        String code = "1";

        try {
            BufferedReader in = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(URL));
            HttpResponse response = client.execute(request, localContext);
            in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String page = sb.toString();
            Tripppy.LOG(page);
            code = findPattern(page, "text=\"(.+)\" code=", 1);
        } catch (Exception e) { Tripppy.LOG("Exception occurred while fetching forecast: " + e.getMessage());}
        return code;
    }

    public String getWOEID(String place) {
        String woeid = "";

        String URL = "http://query.yahooapis.com/v1/public/yql?q=select*from%20geo.places%20where%20text=%22" +
                place + "%22&format=xml";

        try {
            BufferedReader in = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(URL));
            HttpResponse response = client.execute(request, localContext);
            in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();

            String page = sb.toString();
            //<woeid>12770203</woeid>
            woeid = findPattern(page, "<woeid>(\\d+)</", 1);
        } catch (Exception e) { Tripppy.LOG("Exception occurred while fetching WOEID: " + e.getMessage());}
        return woeid;
    }

    public String getViewState() {
        String vs = "";

        try {
            BufferedReader in = null;
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI("http://www.google.com"));
            HttpResponse response = client.execute(request, localContext);
            in = new BufferedReader
                    (new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String page = sb.toString();
            vs = findPattern(page, "__VIEWSTATE\" value=\"(.*)\"", 1);
            vs = vs.split(" ")[0].replaceAll("\"", "");
        } catch (Exception e) { Tripppy.LOG("Exception occurred while fetching viewState: " + e.getMessage());}
        return vs;
    }

    public static String findPattern(String src, String pattern, int groupNum){
        Pattern pp = Pattern.compile(pattern);
        Matcher m = pp.matcher(src);
        String result = "";
        try {
            while (m.find()) {
                if(groupNum <= m.groupCount()) {
                    result = m.group(groupNum);
                } else {
                    Tripppy.LOG("findPattern::Group not found!");
                }
            }
        }
        catch (Exception e) {
            Tripppy.LOG(e.getMessage());
        }
        return result;
    }
}