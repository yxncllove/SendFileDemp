package com.huadin.Util;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.List;
import java.util.Map;

/**
 * Created by 潇湘 on 2016/7/30.
 */
public class HttpUtil
{
  private static final String TAG = "HttpUtil";
  public static String sendGet (String url , String params)
  {
    String result = "";
    BufferedReader in = null;//缓冲流

    try
    {
      URL getUrl = new URL(url + "?" + params);
      URLConnection conn = getUrl.openConnection();
      //设置通用的请求属性
      conn.setRequestProperty("accept","*/*");
      conn.setRequestProperty("connection","Keep-Alive");
      conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Window NT 5.1 ;SV1)");

      //建立连接
      conn.connect();

      //获取所有的响应头字段
      Map<String, List<String>> map = conn.getHeaderFields();
      for (String key:map.keySet())
      {
        Log.i(TAG, "sendGet: key = " + map.get(key));
      }

      //读取响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      String line = null;
      while ((line = in.readLine()) != null)
      {
        result = "\n" + line;
      }
      return result;

    } catch (Exception e)
    {
      e.printStackTrace();
    }finally
    {
      try
      {
        if (in != null)
        {
          in.close();
        }
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return result;
  }

  public static String sendPost (String url , String params)
  {
    String result = "";
    BufferedReader in = null;//缓冲流
    PrintWriter pw = null;//打印流

    try
    {
      URL postUrl = new URL(url);

      URLConnection conn = postUrl.openConnection();
      //设置通用属性
      conn.setRequestProperty("accept","*/*");
      conn.setRequestProperty("connection","Keep-Alive");
      conn.setRequestProperty("user-agent","Mozilla/4.0 (compatible; MSIE 6.0; Window NT 5.1 ;SV1)");

      //发送post 请求必须设置该两行
      conn.setDoInput(true);
      conn.setDoOutput(true);

      //获取输出流
      pw = new PrintWriter(conn.getOutputStream());
      //发送请求参数
      pw.print(params);
      //flush输出流的缓冲
      pw.flush();

      //读取响应
      in = new BufferedReader(new InputStreamReader(conn.getInputStream()));

      String line = "";
      while ((line = in.readLine()) != null)
      {
        result = "\n" + line;
      }

      return result;

    } catch (Exception e)
    {
      e.printStackTrace();
    }finally
    {
      try
      {
        if (in != null)
        {
          in.close();
        }
        if (pw != null)
        {
          pw.close();
        }
      } catch (IOException e)
      {
        e.printStackTrace();
      }
    }
    return result;
  }
}
