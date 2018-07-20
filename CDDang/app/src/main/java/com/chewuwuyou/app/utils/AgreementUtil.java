package com.chewuwuyou.app.utils;

/**
 * 车当当相关协议的工具类
 *
 * @author yuyong
 */
public class AgreementUtil {
    // http://wei.cddang.com/mhwcw/agreement/preCddRule?type=2
    // type=1 车当当服务收费规则
    // type=2 车当当服务协议
    // type=3 车当当关于第三方服务商违章代办声明
    // type=4 车当当提现协议
    // type=5 车当当退款协议
    // type=6 车当当争议处理协议
    // type=7 成都车当当信息科技有限公司短信校验服务协议
    // type=8 成都车当当信息科技有限公司法律声明

    public static String getTitle(int type) {
        String title = null;
        switch (type) {
            case 1:
                title = "车当当服务费规则";
                break;
            case 2:
                title = "车当当服务协议";
                break;
            case 3:
                title = "车当当关于第三方服务商违章代办声明";
                break;
            case 4:
                title = "车当当提现协议";
                break;
            case 5:
                title = "车当当退款协议";
                break;
            case 6:
                title = "车当当争议处理协议";
                break;
            case 7:
                title = "成都车当当信息科技有限公司短信校验服务协议";
                break;
            case 8:
                title = "成都车当当信息科技有限公司法律声明";
                break;
            case 9:
                title = "车当当公告";
                break;
            case 10:
                title = "版权&隐私声明";
                break;
            case 11:
                title = "免责声明";
                break;
            default:
                break;
        }
        return title;
    }

}
