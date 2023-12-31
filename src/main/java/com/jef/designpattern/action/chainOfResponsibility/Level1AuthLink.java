package com.jef.designpattern.action.chainOfResponsibility;

import com.jef.util.PrintUtil;

import com.alibaba.fastjson.JSON;

import java.text.ParseException;
import java.util.Date;

/**
 * 一级审批
 *
 * @author Jef
 * @date 2021/12/11
 */
public class Level1AuthLink extends AuthLink {
    private Date beginDate = f.parse("2021-06-11 00:00:00");
    private Date endDate = f.parse("2021-06-20 23:59:59");


    public Level1AuthLink(String levelUserId, String levelUserName) throws ParseException {
        super(levelUserId, levelUserName);
    }

    public void doAuth(String uId, String orderId, Date billDate) {
        if (beginDate.before(billDate) && endDate.after(billDate)) {
            Date date = AuthService.queryAuthInfo(levelUserId, orderId);
            if (null == date) {
                AuthInfo authInfo = new AuthInfo("0001", "单号：", orderId, " 状态：待一级审批人【", levelUserName, "】审批");
                PrintUtil.printf("%s", JSON.toJSONString(authInfo));
                AuthService.auth(levelUserId, orderId, levelUserName, "一级审批人", billDate, this);
            }
        }
    }
}