
package com.chewuwuyou.app.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.ref.SoftReference;
import java.net.URL;
import java.util.HashMap;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;
import android.view.View;

public class AsyncBitmapLoader
{
    /**
     * 内存图片软引用缓冲
     */
    private HashMap<String, SoftReference<Bitmap>> imageCache = null;

    public AsyncBitmapLoader()
    {
        imageCache = new HashMap<String, SoftReference<Bitmap>>();
    }

    public Bitmap loadBitmap(final View imageView, final String imageURL,
            final ImageCallBack imageCallBack)
    {
        // 在内存缓存中，则返回Bitmap对象
        if (imageCache.containsKey(imageURL))
        {
            SoftReference<Bitmap> reference = imageCache.get(imageURL);
            Bitmap bitmap = reference.get();
            if (bitmap != null)
            {
                return bitmap;
            }
        }
        else
        {
            /**
             * 加上一个对本地缓存的查找
             */

            String bitmapName = imageURL.substring(imageURL.lastIndexOf("/") + 1);
            File cacheDir = new File("/mnt/sdcard/cwwy/");
            File[] cacheFiles = cacheDir.listFiles();
            int i = 0;
            if (null != cacheFiles) {
                for (; i < cacheFiles.length; i++)
                {
                    if (bitmapName.equals(cacheFiles[i].getName()))
                    {
                        break;
                    }
                }

                if (i < cacheFiles.length)
                {
                    return BitmapFactory.decodeFile("/mnt/sdcard/cwwy/" + bitmapName);
                }
            }
        }

        final Handler handler = new Handler()
        {
            /*
             * (non-Javadoc)
             * @see android.os.Handler#handleMessage(android.os.Message)
             */
            @Override
            public void handleMessage(Message msg)
            {
                // TODO Auto-generated method stub
                imageCallBack.imageLoad(imageView, (Bitmap) msg.obj);
            }
        };

        // 如果不在内存缓存中，也不在本地（被jvm回收掉），则开启线程下载图片
        new Thread()
        {
            /*
             * (non-Javadoc)
             * @see java.lang.Thread#run()
             */
            @Override
            public void run()
            {
                // TODO Auto-generated method stub
                InputStream bitmapIs = HttpUtils.getStreamFromURL(imageURL);
                Bitmap bitmap = BitmapFactory.decodeStream(bitmapIs);
                imageCache.put(imageURL, new SoftReference<Bitmap>(bitmap));
                Message msg = handler.obtainMessage(0, bitmap);
                handler.sendMessage(msg);

                File dir = new File("/mnt/sdcard/cwwy/");
                if (!dir.exists())
                {
                    dir.mkdirs();
                }

                File bitmapFile = new File("/mnt/sdcard/cwwy/" +
                        imageURL.substring(imageURL.lastIndexOf("/") + 1));
                if (!bitmapFile.exists())
                {
                    try
                    {
                        bitmapFile.createNewFile();
                    } catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
                FileOutputStream fos;
                try
                {
                    if (bitmap != null) {
                        fos = new FileOutputStream(bitmapFile);
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, fos);
                        fos.close();
                    }
                } catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                } catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }.start();

        return null;
    }

    public interface ImageCallBack
    {
        public void imageLoad(View imageView, Bitmap bitmap);
    }

    public static Bitmap GetLocalOrNetBitmap(String url)
    {
        Bitmap bitmap = null;
        InputStream in = null;
        BufferedOutputStream out = null;
        try
        {
            in = new BufferedInputStream(new URL(url).openStream(), Constant.IO_BUFFER_SIZE);
            final ByteArrayOutputStream dataStream = new ByteArrayOutputStream();
            out = new BufferedOutputStream(dataStream, Constant.IO_BUFFER_SIZE);
            copy(in, out);
            out.flush();
            byte[] data = dataStream.toByteArray();
            bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            data = null;
            return bitmap;
        } catch (IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    private static void copy(InputStream in, OutputStream out)
            throws IOException {
        byte[] b = new byte[Constant.IO_BUFFER_SIZE];
        int read;
        while ((read = in.read(b)) != -1) {
            out.write(b, 0, read);
        }
    }
}
