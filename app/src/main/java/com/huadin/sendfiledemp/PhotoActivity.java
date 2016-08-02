package com.huadin.sendfiledemp;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import com.huadin.Util.HttpUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.URL;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 潇湘 on 2016/7/30.
 */
public class PhotoActivity extends AppCompatActivity
{
  @BindView(R.id.get_bitmap)
  Button getBitmap;
  @BindView(R.id.show_bitmap)
  ImageView showBitmap;

  private static final String TAG = "PhotoActivity";

  private static final int OK = 0x11;

  private Bitmap bitmap;

  private ShowHandler handler;

  @Override
  protected void onCreate(@Nullable Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.show_image_photo);
    ButterKnife.bind(this);
    if (handler == null)
    {
      handler = new ShowHandler(this);
    }
  }

  @OnClick(R.id.get_bitmap)
  public void onClick()
  {

    new Thread()
    {
      @Override
      public void run()
      {
        super.run();
        bitmapFromHttp();
//        String resultGet = HttpUtil.sendGet("http://baike.baidu.com", "雷锋");
//        String resultPost = HttpUtil.sendPost("http://baike.baidu.com", "雷锋");

//        Log.i(TAG, "resultGet = " + resultGet);
//        Log.i(TAG, "resultPost = " + resultPost);
      }
    }.start();

  }

  private void bitmapFromHttp()
  {
    InputStream is = null;
    FileOutputStream fos = null;
    try
    {
      URL showUrl = new URL("http://img2.imgtn.bdimg.com/it/u=1003704465,1400426357&fm=21&gp=0.jpg");

      is = showUrl.openStream();

      //从 InputStream 中解析图片

      bitmap = BitmapFactory.decodeStream(is);
      if (bitmap != null)
      {
        handler.sendEmptyMessage(OK);

        is.close();
        //保存到本地

        is = showUrl.openStream();

        File file = getFilesDir();
        File oldFile = new File(file.getPath() + "/bitmap_1.jpg");
        if (!oldFile.exists())//不存在
        {
          Log.i(TAG, "文件不存在");
          fos = openFileOutput("bitmap_1.jpg", MODE_PRIVATE);

          byte[] buff = new byte[1024];
          int count = 0;
          while ((count = is.read(buff)) > 0)
          {
            fos.write(buff, 0, count);
          }

          is.close();
          fos.close();
        }
      }

    } catch (Exception e)
    {
      e.printStackTrace();
    }
  }

  /**
   * 弱引用 handler
   */
  static class ShowHandler extends Handler
  {
    private final WeakReference<PhotoActivity> mActivity;

    public ShowHandler(PhotoActivity activity)
    {
      mActivity = new WeakReference<>(activity);
    }

    @Override
    public void handleMessage(Message msg)
    {
      PhotoActivity activity = mActivity.get();
      if (msg.what == OK)
      {
        if (activity != null)
        {
          activity.showBitmap.setImageBitmap(activity.bitmap);
        }
      }
    }
  }

}
