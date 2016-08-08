package com.xueqiu.fund.selenium.test;


import com.xueqiu.fund.selenium.service.FundHeaders;

import java.util.Map;

public class BaseTest {
    protected static Map<String,String> instanceHeaders = FundHeaders.getInstanceHeaders();
}
