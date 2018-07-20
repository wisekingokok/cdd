
package com.chewuwuyou.app.utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 短信验证码工具类
 * 
 * @author xiajy
 * @version 1.0
 * @created 2012-4-8
 */
public class SmsUtil {
    private static int connectTimeOut = 5000;
    private static int readTimeOut = 10000;
    private static String requestEncoding = "UTF-8";
    private static final String SMS_ACCOUNT = "JSMB260304";
    private static final String SMS_PASSWORD = "479321";

    public static int getConnectTimeOut() {
        return connectTimeOut;
    }

    public static void setConnectTimeOut(int connectTimeOut) {
        SmsUtil.connectTimeOut = connectTimeOut;
    }

    public static int getReadTimeOut() {
        return readTimeOut;
    }

    public static void setReadTimeOut(int readTimeOut) {
        SmsUtil.readTimeOut = readTimeOut;
    }

    public static String getRequestEncoding() {
        return requestEncoding;
    }

    public static void setRequestEncoding(String requestEncoding) {
        SmsUtil.requestEncoding = requestEncoding;
    }

    public static String doGet(String requrl, Map<?, ?> parameters, String recvEndcoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        String vchartset = recvEndcoding == "" ? SmsUtil.requestEncoding : recvEndcoding;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Entry<?, ?> element = (Entry<?, ?>) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), vchartset));
                params.append("&");
            }
            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(requrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("连接超时：", String.valueOf(SmsUtil.connectTimeOut));
            System.setProperty("访问超时：", String.valueOf(SmsUtil.readTimeOut));
            url_con.setDoOutput(true);//
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            byte[] echo = new byte[10 * 1024];
            int len = in.read(echo);
            responseContent = (new String(echo, 0, len).trim());
            int code = url_con.getResponseCode();
            if (code != 200) {
                responseContent = "ERROR" + code;
            }
        } catch (Exception e) {
            System.out.println("网络故障:" + e.toString());
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;

    }

    public static String doGet(String reqUrl, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        String vchartset = recvEncoding == "" ? SmsUtil.requestEncoding : recvEncoding;
        try {
            StringBuffer params = new StringBuffer();
            String queryUrl = reqUrl;
            int paramIndex = reqUrl.indexOf("?");

            if (paramIndex > 0) {
                queryUrl = reqUrl.substring(0, paramIndex);
                String parameters = reqUrl.substring(paramIndex + 1, reqUrl.length());
                String[] paramArray = parameters.split("&");
                for (int i = 0, len = paramArray.length; i < len; i++) {
                    String string = paramArray[i];
                    int index = string.indexOf("=");
                    if (index > 0) {
                        String parameter = string.substring(0, index);
                        String value = string.substring(index + 1, string.length());
                        params.append(parameter);
                        params.append("=");
                        params.append(URLEncoder.encode(value, vchartset));
                        params.append("&");
                    }
                }

                params = params.deleteCharAt(params.length() - 1);
            }
            URL url = new URL(queryUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("GET");
            System.setProperty("sun.net.client.defaultConnectTimeout",
                    String.valueOf(SmsUtil.connectTimeOut));
            System.setProperty("sun.net.client.defaultReadTimeout",
                    String.valueOf(SmsUtil.readTimeOut));
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();
            InputStream in = url_con.getInputStream();
            byte[] echo = new byte[10 * 1024];
            int len = in.read(echo);
            responseContent = (new String(echo, 0, len)).trim();
            int code = url_con.getResponseCode();
            if (code != 200) {
                responseContent = "ERROR" + code;
            }
        } catch (Exception e) {
            System.out.println("网络故障:" + e.toString());
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        return responseContent;

    }

    public static String doPost(String reqUrl, Map<String, String> parameters, String recvEncoding) {
        HttpURLConnection url_con = null;
        String responseContent = null;
        String vchartset = recvEncoding == "" ? SmsUtil.requestEncoding : recvEncoding;
        try {
            StringBuffer params = new StringBuffer();
            for (Iterator<?> iter = parameters.entrySet().iterator(); iter.hasNext();) {
                Entry<?, ?> element = (Entry<?, ?>) iter.next();
                params.append(element.getKey().toString());
                params.append("=");
                params.append(URLEncoder.encode(element.getValue().toString(), vchartset));
                params.append("&");
            }

            if (params.length() > 0) {
                params = params.deleteCharAt(params.length() - 1);
            }

            URL url = new URL(reqUrl);
            url_con = (HttpURLConnection) url.openConnection();
            url_con.setRequestMethod("POST");
            url_con.setConnectTimeout(SmsUtil.connectTimeOut);
            url_con.setReadTimeout(SmsUtil.readTimeOut);
            url_con.setDoOutput(true);
            byte[] b = params.toString().getBytes();
            url_con.getOutputStream().write(b, 0, b.length);
            url_con.getOutputStream().flush();
            url_con.getOutputStream().close();

            InputStream in = url_con.getInputStream();
            byte[] echo = new byte[10 * 1024];
            int len = in.read(echo);
            responseContent = (new String(echo, 0, len)).trim();
            int code = url_con.getResponseCode();
            if (code != 200) {
                responseContent = "ERROR" + code;
            }

        } catch (IOException e) {
            System.out.println("网络故障:" + e.toString());
        } finally {
            if (url_con != null) {
                url_con.disconnect();
            }
        }
        System.out.println(responseContent);
        return responseContent;
    }

    /**
     * 使用模版发送验证码
     * 
     * @param telephone 发送的手机
     * @param smsCode 手机验证码
     * @return 0#数字#数字 提交成功，格式：返回值#提交计费条数#提交成功号码数 100 发送失败 101 用户账号不存在或密码错误 102
     *         账号已禁用 103 参数不正确 105 短信内容超过500字、或为空、或内容编码格式不正确 106
     *         手机号码超过100个或合法的手机号码为空 108 余额不足 109 指定访问ip地址错误 110#(敏感词A,敏感词B)
     *         短信内容存在系统保留关键词，如有多个词，使用逗号分隔：110#(李老师,XX,成人) 114 模板短信序号不存在 115
     *         短信签名标签序号不存在
     */
    public static String sendSmsCodeUseTemp(String telephone, String smsCode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", SMS_ACCOUNT);// 此处填写用户账号
        map.put("scode", SMS_PASSWORD);// 此处填写用户密码
        map.put("mobile", telephone);// 此处填写发送号码
        map.put("tempid", "MB-2014052700");// 此处填写模板短信编号
        // map.put("extcode","1234");
        map.put("content", "@1@=" + smsCode);// 此处填写模板短信内容
        String temp = SmsUtil.doPost("http://mssms.cn:8000/msm/sdk/http/sendsmsutf8.jsp", map, "UTF-8");
        System.out.println("===="+temp);
        return temp;
    }

    /**
     * 不使用模版短信发送验证码
     * 
     * @param telephone 发送的手机
     * @param strCode 信息+验证码
     * @return 0#数字#数字 提交成功，格式：返回值#提交计费条数#提交成功号码数 100 发送失败 101 用户账号不存在或密码错误 102
     *         账号已禁用 103 参数不正确 105 短信内容超过500字、或为空、或内容编码格式不正确 106
     *         手机号码超过100个或合法的手机号码为空 108 余额不足 109 指定访问ip地址错误 110#(敏感词A,敏感词B)
     *         短信内容存在系统保留关键词，如有多个词，使用逗号分隔：110#(李老师,XX,成人) 114 模板短信序号不存在 115
     *         短信签名标签序号不存在
     */
    public static String sendSmsCode(String telephone, String strCode) {
        Map<String, String> map = new HashMap<String, String>();
        map.put("username", SMS_ACCOUNT);// 此处填写用户账号
        map.put("scode", SMS_PASSWORD);// 此处填写用户密码
        map.put("mobile", telephone);// 此处填写发送号码
        // map.put("extcode","1234");
        map.put("content", strCode);// 此处填写模板短信内容
        String temp = SmsUtil.doPost("http://mssms.cn:8000/msm/sdk/http/sendsms.jsp", map, "GBK");
        return temp;
    }
}
