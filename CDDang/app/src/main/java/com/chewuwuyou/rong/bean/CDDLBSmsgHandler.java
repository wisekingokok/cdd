package com.chewuwuyou.rong.bean;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import io.rong.common.FileUtils;
import io.rong.common.RLog;
import io.rong.imlib.NativeClient;
import io.rong.imlib.model.Message;
import io.rong.message.MessageHandler;
import io.rong.message.utils.BitmapUtil;

/**
 * Created by Administrator on 2016/9/12 0012.
 */
public class CDDLBSmsgHandler extends MessageHandler<CDDLBSMsg> {
    private static final String TAG = "CDDLBSmsgHandler";
    private static final int THUMB_WIDTH = 408;
    private static final int THUMB_HEIGHT = 240;
    private static final int THUMB_COMPRESSED_QUALITY = 30;
    private static final String LOCATION_PATH = "/location/";

    public CDDLBSmsgHandler(Context context) {
        super(context);
    }

    public void decodeMessage(Message message, CDDLBSMsg content) {
        String name = message.getMessageId() + "";
        if (message.getMessageId() == 0) {
            name = message.getSentTime() + "";
        }

        Uri uri = obtainLocationUri(this.getContext());
        File file = new File(uri.toString() + name);
        if (file.exists()) {
            content.setImgUri(Uri.fromFile(file));
        } else {
            if (content != null) {
                String base64 = content.getBase64();
                if (!TextUtils.isEmpty(base64)) {
                    if (base64.startsWith("http")) {
                        content.setImgUri(Uri.parse(base64));
                        content.setBase64((String) null);
                    } else {
                        try {
                            byte[] e = Base64.decode(content.getBase64(), 2);
                            file = FileUtils.byte2File(e, uri.toString(), name + "");
                            if (content.getImgUri() == null) {
                                if (file != null && file.exists()) {
                                    content.setImgUri(Uri.fromFile(file));
                                } else {
                                    RLog.e("CDDLBSmsgHandler", "getImgUri is null");
                                }
                            }
                        } catch (IllegalArgumentException var8) {
                            RLog.e("CDDLBSmsgHandler", "Not Base64 Content!");
                            var8.printStackTrace();
                        }

                        message.setContent(content);
                        content.setBase64((String) null);
                    }
                }
            }

        }
    }

    public void encodeMessage(Message message) {
        CDDLBSMsg content = (CDDLBSMsg) message.getContent();
        if (content.getImgUri() == null) {
            RLog.w("CDDLBSmsgHandler", "No thumbnail uri.");
            if (this.mHandleMessageListener != null) {
                this.mHandleMessageListener.onHandleResult(message, 0);
            }

        } else {
            Uri uri = obtainLocationUri(this.getContext());
            File file;
            String thumbnailPath;
            if (content.getImgUri().getScheme().toLowerCase().equals("file")) {
                thumbnailPath = content.getImgUri().getPath();
            } else {
                file = this.loadLocationThumbnail(content, message.getSentTime() + "");
                thumbnailPath = file != null ? file.getPath() : null;
            }

            if (thumbnailPath == null) {
                RLog.e("CDDLBSmsgHandler", "load thumbnailPath null!");
                if (this.mHandleMessageListener != null) {
                    this.mHandleMessageListener.onHandleResult(message, -1);
                }

            } else {
                try {
                    Bitmap e = BitmapUtil.interceptBitmap(thumbnailPath, 408, 240);
                    if (e != null) {
                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                        e.compress(Bitmap.CompressFormat.JPEG, 30, outputStream);
                        byte[] data = outputStream.toByteArray();
                        outputStream.close();
                        String base64 = Base64.encodeToString(data, 2);
                        content.setBase64(base64);
                        file = FileUtils.byte2File(data, uri.toString(), message.getMessageId() + "");
                        if (file != null && file.exists()) {
                            content.setImgUri(Uri.fromFile(file));
                        }

                        if (!e.isRecycled()) {
                            e.recycle();
                        }

                        if (this.mHandleMessageListener != null) {
                            this.mHandleMessageListener.onHandleResult(message, 0);
                        }
                    } else {
                        RLog.e("CDDLBSmsgHandler", "get null bitmap!");
                        if (this.mHandleMessageListener != null) {
                            this.mHandleMessageListener.onHandleResult(message, -1);
                        }
                    }
                } catch (Exception var10) {
                    RLog.e("CDDLBSmsgHandler", "Not Base64 Content!");
                    var10.printStackTrace();
                    if (this.mHandleMessageListener != null) {
                        this.mHandleMessageListener.onHandleResult(message, -1);
                    }
                }

            }
        }
    }

    private static Uri obtainLocationUri(Context context) {
        File file = context.getFilesDir();
        String userId = NativeClient.getInstance().getCurrentUserId();
        String path = file.getPath() + File.separator + userId + "/location/";
        file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }

        Uri uri = Uri.parse(path);
        return uri;
    }

    private File loadLocationThumbnail(CDDLBSMsg content, String name) {
        File file = null;
        HttpURLConnection conn = null;
        int responseCode = 0;

        try {
            Uri e = content.getImgUri();
            URL url = new URL(e.toString());
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);
            conn.connect();
            responseCode = conn.getResponseCode();
            if (responseCode >= 200 && responseCode < 300) {
                String path = FileUtils.getCachePath(this.getContext(), "location");
                file = new File(path);
                if (!file.exists()) {
                    file.mkdirs();
                }

                file = new File(path, name);
                InputStream is = conn.getInputStream();
                FileOutputStream os = new FileOutputStream(file);
                byte[] buffer = new byte[1024];

                int len;
                while ((len = is.read(buffer)) != -1) {
                    os.write(buffer, 0, len);
                }

                is.close();
                os.close();
            }
        } catch (Exception var16) {
            var16.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }

            RLog.d("CDDLBSmsgHandler", "loadLocationThumbnail result : " + responseCode);
        }

        return file;
    }
}
