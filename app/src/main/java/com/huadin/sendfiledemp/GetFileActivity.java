package com.huadin.sendfiledemp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by 潇湘 on 2016/7/30.
 *
 * 启动文件管理器，获取文件
 */
public class GetFileActivity extends AppCompatActivity
{
  private static final String TAG = "GetFileActivity";

  @BindView(R.id.get_file_button)
  Button mButton;
  @BindView(R.id.show_file_path)
  TextView showPath;

  private String filePath ;

  private static final String APPLICATION_KEY = "ded11eb9e41079e4e4c7529c374eb40e";

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.get_file_layout);
    ButterKnife.bind(this);
    Bmob.initialize(this,APPLICATION_KEY);
  }

  @OnClick(R.id.get_file_button)
  public void onClick()
  {
    Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
    intent.addCategory(Intent.CATEGORY_OPENABLE);
    intent.setType("*/*");
    startActivityForResult(Intent.createChooser(intent,"请选择要上传的文件"),100);
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data)
  {
    if (resultCode == RESULT_OK)
    {
      Uri uri = data.getData();
      filePath = uri.getPath();
      sendFileToHttp(filePath);
      showPath.setText(filePath);
      Log.i(TAG, "onActivityResult: getPath = " + uri.getPath());
      Log.i(TAG, "onActivityResult: getEncodedPath = " + uri.getEncodedPath());
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void sendFileToHttp(String path)
  {
    BmobFile bmobFile = new BmobFile(new File(path));
    bmobFile.uploadblock(new UploadFileListener() {
      @Override
      public void done(BmobException e)
      {
        if (e == null)
        {
          Snackbar.make(mButton,"文件上传成功",Snackbar.LENGTH_SHORT).show();
        }else
        {
          Log.i(TAG, "done: " + e.getMessage());
        }
      }

      @Override
      public void onProgress(Integer value)
      {
        Log.i(TAG, "value: " + value);
      }
    });
  }
}
