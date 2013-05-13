package com.conversant.credis.provider;

import java.util.Map;

import com.conversant.credis.RedisException;

/**
 * 方便在spring中配置，在init时将配置中的provider注册到RedisProviders中
 * 
 * @author chengdong
 */
public class RedisProvidersFactory {

    protected Map<String, ClientProvider> profiles;
    
    protected boolean usePool; // 是否用pool;

    public Map<String, ClientProvider> getProfiles() {
        return profiles;
    }

    public void setProfiles(Map<String, ClientProvider> profiles) {
        this.profiles = profiles;
    }

    // 需由spring触发 init-method="init"
    synchronized public void init() throws RedisException {
        RedisProviders.setProfiles(profiles);
    }

}
