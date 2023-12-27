package com.jef.designpattern.action.chainOfResponsibility;

import com.alibaba.fastjson.JSON;
import com.jef.util.PrintUtil;

import java.text.ParseException;
import java.util.Date;

/**
 * 固定审批流设计
 *
 * @author Jef
 * @date 2021/12/11
 */
public class NodeFixedAuthLink extends AuthLink {

    public NodeFixedAuthLink(String levelUserId, String levelUserName, int node) throws ParseException {
        super(levelUserId, levelUserName, node);
    }

    @Override
    public void doAuth(String uId, String orderId, Date authDate) {
        Date date = AuthService.queryAuthInfo(levelUserId, orderId);
        if (null == date) {
            AuthInfo authInfo = new AuthInfo("0001", "单号：", orderId, " 状态：待审批人【", levelUserName, "】审批");
            PrintUtil.printf("%s", JSON.toJSONString(authInfo));
            AuthService.auth(levelUserId, orderId, levelUserName, "审批人", authDate, this);
        }
    }


}