package com.chewuwuyou.app.transition_utils;

import com.chewuwuyou.app.transition_constant.Constants;

/**
 * Created by yuyong on 16/10/13.
 * 判断用户角色
 * 1、商家
 * 2、品牌商家
 * 3、会员商家
 * 4、省代
 * 5、市代
 * 6、区代
 */

public class UserRoleUtil {
    /**
     * 是品牌商家或会员商家
     *
     * @param role 用户角色
     * @return false 非商家  true商家
     */
    public static boolean isBusiness(int role) {
        if (role == Constants.UserTag.ROLE_A || role == Constants.UserTag.ROLE_B) {
            return true;
        }
        return false;
    }

}
