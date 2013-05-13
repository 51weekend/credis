/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-8-3.
 */

package com.conversant.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 日志工具类.
 *
 * @author chengdong
 */
public class LoggerUtils {

    private static final Logger error = LoggerFactory.getLogger("com.conversant.looger.error");
    private static final Logger request = LoggerFactory.getLogger("com.conversant.looger.request");

    public static void error(String errorMessage, Throwable e) {
        e.printStackTrace();
        error.error(errorMessage, e);
    }

    public static void error(String errorMessage) {
        error.error(errorMessage);
    }

    public static void request(String infoMessage) {
        request.info(infoMessage);
    }

}
