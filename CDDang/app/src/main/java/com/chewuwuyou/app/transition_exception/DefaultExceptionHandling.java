package com.chewuwuyou.app.transition_exception;

import android.content.Context;

import com.chewuwuyou.app.R;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;

import java.net.ConnectException;
import java.net.SocketTimeoutException;

/**
 * 异常处理器
 * Created by xxy on 2016/8/15 0015.
 */
public class DefaultExceptionHandling {

    public static CustomException handling(Context context, Throwable e) {
        if (e == null)
            throw new RuntimeException("the exception is null !");
        e.printStackTrace();
        if (CustomException.class.isInstance(e)) {//自定义异常
            switch (((CustomException) e).getErrorCode()) {
                case CustomError.CODE_SERVER_RESPONSE:
                    return new CustomException(CustomError.CODE_SERVER_RESPONSE, context.getResources().getString(R.string.server_response_error));//服务器返回数据不能解析
                default:
                    return (CustomException) e;
            }
        } else if (SocketTimeoutException.class.isInstance(e)) {// 超时
            return new CustomException(CustomError.CODE_CONNECTION_ERROR, context.getResources().getString(R.string.time_out_error));
        } else if (ConnectException.class.isInstance(e)) {// 连接不到服务器
            return new CustomException(CustomError.CODE_CONNECTION_ERROR, context.getResources().getString(R.string.connect_error));
        } else if (JsonParseException.class.isInstance(e)) {// Gson类型转换错误
            return new CustomException(CustomError.CODE_SERVER_JSON_ERROR, context.getResources().getString(R.string.json_error));
        } else if (JsonSyntaxException.class.isInstance(e)) {
            return new CustomException(CustomError.CODE_SERVER_JSON_ERROR, context.getResources().getString(R.string.json_syntax_error));//服务器返回的数据转Json失败
        }else if (NullPointerException.class.isInstance(e)){
            return new CustomException(CustomError.NULLPOINTER_EXCEPTION,context.getResources().getString(R.string.null_point_exception));//空指针异常
        }else {
            e.printStackTrace();
            return new CustomException(CustomError.CODE_UNKOWN_ERROR, e);
        }
    }
}
