package com.intrivix.android.busman.network;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
/* 
 * Parameters received should be URL, REQUEST TYPE.
 */
import android.os.Handler;
import android.os.Message;


public class APITask extends AsyncTask<Object,Object,Object> {

    public static final int REQUEST_POST = 1;
    public static final int REQUEST_GET = 2;
    public static final int RESULT_OK = 200;
    
    public static String GET_ROUTE_URL = "http://dev.georgevanburgh.co.uk/busman/api/directions";

    private int mHTTPResponseCode;
    private Handler mTaskHandler = null;
    private String responseContents;

    @Override
    protected Object doInBackground(Object... args) {
        // TODO Auto-generated method stub
        mTaskHandler = (Handler)args[0];
        String url = (String) args[1];
        String params = (String) args[2];
        int reqType = (Integer) args[3];

        HttpClient httpClient = new DefaultHttpClient();
        HttpUriRequest httpReq = null;

        if(reqType == REQUEST_POST) {
            HttpPost post = new HttpPost(url);
            try {
                //post.setEntity(new UrlEncodedFormEntity(params, "utf-8"));
            	post.setEntity(new StringEntity(params));
            	System.out.println("API SENT THIS DATA: " + params);
            	System.out.println("API SENT TO URL: " + url);
                post.setHeader("Content-Type", "application/json");
            }
            catch (UnsupportedEncodingException e1) {
                // Consume exceptions
            }
            httpReq = post;
        } else if(reqType == REQUEST_GET) {
            httpReq = new HttpGet(url);
        }

        HttpResponse httpResponse;
        try {
            httpResponse = httpClient.execute(httpReq);
            //httpResponse.s

            // Get the status code
            mHTTPResponseCode = httpResponse.getStatusLine().getStatusCode();

            if(mHTTPResponseCode == RESULT_OK){
                HttpEntity httpEntity = httpResponse.getEntity();
                if (httpEntity != null) {
                    httpEntity = new BufferedHttpEntity(httpEntity);
                    InputStream httpContent = null;
                    try {
                        httpContent = httpEntity.getContent();
                        responseContents = inStreamToString(httpContent);

                    } finally {
                        if (httpContent != null) {
                            httpContent.close();
                        }
                    }
                }
            }
        } catch (IOException e) {
            // Consume exceptions
            // [NOTES][TODO] Error handling
            return null;
        }

        return responseContents;
        // return mUR
    }// do in background



    @Override
    protected void onPostExecute(Object result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (mTaskHandler != null) {
            Message msg = new Message();
            msg.obj = result;
            mTaskHandler.sendMessage(msg);
        }
    }
    
    public  static String getEncodedParams(ArrayList<NameValuePair> params) {
        if (params == null) {
          return "";
        }

        String paramString = URLEncodedUtils.format(params, "utf-8");
        paramString = paramString.replace("+", "%20");
        return "?" + paramString;
    }

    public static String inStreamToString(InputStream in) {
            if (in == null) {
              return null;
            }

            String strLine = "";
            StringBuilder stringBuilder = new StringBuilder();

            // Convert to String
            InputStreamReader inStreamReader = null;
            BufferedReader bufferedReader = null;
            try {
              try {
                inStreamReader = new InputStreamReader(in, "utf-8");
                bufferedReader = new BufferedReader(inStreamReader);

                strLine = bufferedReader.readLine();
                while (strLine != null) {
                  stringBuilder.append(strLine);
                  stringBuilder.append("\n");
                  strLine = bufferedReader.readLine();
                }
              }
              finally {
                if (bufferedReader!= null) {
                  bufferedReader.close();
                }

                if (inStreamReader !=null) {
                  inStreamReader.close();
                }
              }
            }
            catch (UnsupportedEncodingException e) {
            }
            catch (IOException e) {
            }

            return stringBuilder.toString();
    }

    public static String buildGetParameters(ArrayList<NameValuePair> params) {
        if (params == null) {
            return "";
        }

        String paramString = URLEncodedUtils.format(params, "utf-8");
        paramString = paramString.replace("+", "%20");

        return "?" + paramString;
    }

    public static void callApi(String url, String params, int requestType, Handler handler) {

        Object[] args = new Object[4];
        args[0] = handler;
        args[1] = url;
        args[2] = params;
        args[3] = requestType;
        APITask task = new APITask();
        task.execute(args);
    }

}

