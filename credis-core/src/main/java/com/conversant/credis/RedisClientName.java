/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-9-25.
 */

package com.conversant.credis;

/**
 * redis 客户端名称.
 * 
 * @author chengdong
 */
public enum RedisClientName {

	PERSISTENCE("persistenceCluster");

	private String name;

	RedisClientName(String name) {
		this.name = name;
	}

	public String getName() {
		return this.name;
	}

}
