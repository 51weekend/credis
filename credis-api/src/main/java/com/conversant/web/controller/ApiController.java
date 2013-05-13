/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-11-22.
 */

package com.conversant.web.controller;

import static com.conversant.web.model.ResultModel.SERVER_ERROR;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.conversant.credis.RedisClientName;
import com.conversant.credis.RedisException;
import com.conversant.credis.provider.RedisProviders;
import com.conversant.logger.LoggerUtils;
import com.conversant.web.model.ResultModel;

/**
 * TODO.
 * 
 * @author chengdong
 */
@Controller
public class ApiController {

	@RequestMapping
	@ResponseBody
	public ResultModel get(
			@RequestParam(value = "key", required = false) String key) {
		ResultModel result = new ResultModel();

		try {
			RedisProviders.getClient(RedisClientName.PERSISTENCE).get(key);
		} catch (RedisException e) {
			LoggerUtils.error("get value by key error", e);
			result.setResultCode(SERVER_ERROR);
		}

		return result;
	}

	@RequestMapping
	@ResponseBody
	public ResultModel set(
			@RequestParam(value = "key", required = false) String key,
			@RequestParam(value = "value", required = false) String value) {
		
		ResultModel result = new ResultModel();

		try {
			RedisProviders.getClient(RedisClientName.PERSISTENCE).set(key, value);
		} catch (RedisException e) {
			LoggerUtils.error("set value error", e);
			result.setResultCode(SERVER_ERROR);
		}

		return result;
		
		

	}

}
