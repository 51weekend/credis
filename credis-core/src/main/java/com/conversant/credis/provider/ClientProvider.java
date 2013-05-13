package com.conversant.credis.provider;

import com.conversant.credis.RedisClient;

/**
 * RedisClient提供者接口
 * 
 * @author zhaopeng.xuzp Mar 22, 2012 4:40:36 PM
 */
public interface ClientProvider {

    RedisClient buildClient();
}
