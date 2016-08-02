package com.huadin.sendfiledemp;

import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 潇湘 on 2016/7/29.
 */
public class ListViewFile extends AppCompatActivity
{
  @BindView(R.id.textView)
  TextView textView;
  @BindView(R.id.back)
  Button back;
  @BindView(R.id.item_listView)
  ListView itemListView;

  File currentParent;
  File[] currentFiles;
  private static final String TAG = "ListViewFile";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.file_listview);
    ButterKnife.bind(this);
    showListFiles();
  }

  private void showListFiles()
  {
    //SD卡是否存在
    File filePath = new File("/mnt/sdcard");
    if (filePath.exists())//存在
    {
      currentParent = Environment.getDataDirectory();
      File[] files = Environment.getExternalStorageDirectory().listFiles();
      currentFiles = files;
      inflaterListView(files);//显示所有文件
      //ListView Item 点击事件
      itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
      {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id)
        {
          itemClick(parent, view, position);
        }
      });
    }
  }

  private void itemClick(AdapterView<?> parent, View view, int position)
  {
    if (currentFiles[position].isFile())
    {
      return;
    }

    File[] itemFiles = currentFiles[position].listFiles();
    if (itemFiles == null || itemFiles.length == 0)
    {
      Snackbar.make(itemListView, "该目录下没有文件", Snackbar.LENGTH_SHORT).show();
    } else
    {
      //将当前文件夹设置为父目录
      currentParent = currentFiles[position];
      currentFiles = itemFiles;
      File[] newFiles = currentParent.listFiles();
      //更新ListView
      inflaterListView(newFiles);
    }
  }

  @OnClick(R.id.back)
  public void onClick() //返回上一级目录
  {
    try
    {
      Log.i(TAG, "onClick: "+ currentParent.getCanonicalPath());

      if (currentParent.getCanonicalPath().equals("/data")) return;

      //不是跟目录
      if (!currentParent.getCanonicalPath().equals("/mnt/sdcard"))
      {
        currentParent = currentParent.getParentFile();
        //列出该目录下的所有文件，并更新ListView
        currentFiles = currentParent.listFiles();
        inflaterListView(currentFiles);
      }else if (currentParent.getCanonicalPath().equals("/mnt/sdcard"))
      {
        return;
      }

    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private void inflaterListView(File[] files)
  {
    List<Map<String, Object>> itemList = new ArrayList<>();
    for (int i = 0; i < files.length; i++)
    {
      Map<String, Object> map = new HashMap<>();
      if (files[i].isDirectory())//文件夹
      {
        map.put("icon", R.drawable.files);
      } else
      {
        map.put("icon", R.drawable.file);
      }

      map.put("fileName", files[i].getName());
      itemList.add(map);
    }

    SimpleAdapter adapter = new SimpleAdapter(this, itemList, R.layout.item_line,
            new String[]{"icon", "fileName"}, new int[]{R.id.icon, R.id.fileName});

    itemListView.setAdapter(adapter);

    try
    {
      textView.setText("当前路径为：" + currentParent.getCanonicalPath());
    } catch (IOException e)
    {
      e.printStackTrace();
    }
  }
}
