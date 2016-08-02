package com.huadin.sendfiledemp;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.RandomAccessFile;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity
{

  private static final String TAG = "MainActivity";

  @BindView(R.id.inPut)
  EditText inPut;
  @BindView(R.id.outPut)
  EditText outPut;
  @BindView(R.id.readDate)
  Button readDate;
  @BindView(R.id.writerDate)
  Button writerDate;

  @Override
  protected void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    ButterKnife.bind(this);
  }

  @OnClick({R.id.readDate, R.id.writerDate})
  public void onClick(View view)
  {
    switch (view.getId())
    {
      case R.id.readDate:
        readDate();
        break;
      case R.id.writerDate:
        writerDate();
        break;
    }

  }

  private void writerDate()
  {
    String content = outPut.getText().toString();
    try
    {
      //获取SD卡状态
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
      {
        //获取sd卡目录
        File fileDir = Environment.getExternalStorageDirectory();
        File file = new File(fileDir.getCanonicalPath() , "huaDin.txt");
        RandomAccessFile raf = new RandomAccessFile(file,"rw");
        raf.seek(file.length());
        raf.write(content.getBytes());
        raf.close();
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  private void readDate()
  {
    try
    {
      if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
      {
        //获取Sd卡的目录
        File fileDir = Environment.getExternalStorageDirectory();
        //获取指定文件的输入流
        Log.i(TAG, "readDate: AbsolutePath" + fileDir.getAbsolutePath());
        Log.i(TAG, "readDate: CanonicalPath" + fileDir.getCanonicalPath());
        Log.i(TAG, "readDate: Path" + fileDir.getPath());

        FileInputStream fis = new FileInputStream(fileDir.getCanonicalPath() + "/huaDin.txt");
        //将其包装成 BuffterReader
        BufferedReader br = new BufferedReader(new InputStreamReader(fis));

        StringBuffer sb = new StringBuffer("");
        int count = 0;
        char[] buff = new char[1024];
        while ((count = br.read(buff)) > 0)
        {
          sb.append(new String(buff,0 ,count));
        }
        br.close();

        inPut.setText(sb.toString());
      }
    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }
}
