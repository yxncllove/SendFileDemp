package com.huadin.sendfiledemp;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.TextView;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;



import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 潇湘 on 2016/8/1.
 */
public class FileFromLoasctActivity extends AppCompatActivity
{
  @BindView(R.id.file_name)
  Button getFile;
  @BindView(R.id.show_file_content)
  TextView showContent;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.file_from);
    ButterKnife.bind(this);
  }

  @OnClick(R.id.file_name)
  public void onClick()
  {
   new Thread()
   {
     @Override
     public void run()
     {
       super.run();
       httpFile ();
     }
   }.start();
  }

  private void httpFile ()
  {
    try
    {
      String url = "http://192.168.10.62:8516/Service1.asmx";//webService url
      String nameSpace = "http://tempuri.org/";//命名空间
      String name = "GetUserInfo";//方法名
      String soapAction = nameSpace + name;

      SoapObject request  = new SoapObject(nameSpace,name);

      SoapSerializationEnvelope envelope=new SoapSerializationEnvelope(SoapEnvelope.VER12);
      envelope.dotNet = true;
      envelope.setOutputSoapObject(request);

      HttpTransportSE ht = new HttpTransportSE(url);

      ht.call(soapAction,envelope);

      final SoapPrimitive result = (SoapPrimitive) envelope.getResponse();
      if (result != null)
      {
        runOnUiThread(new Runnable() {
          @Override
          public void run()
          {
            showContent.setText(result.toString());
          }
        });
      }

    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
