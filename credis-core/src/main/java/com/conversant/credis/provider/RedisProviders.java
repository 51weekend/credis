package com.conversant.credis.provider;

import java.util.HashMap;
import java.util.Map;

import com.conversant.credis.RedisClient;
import com.conversant.credis.RedisClientName;
import com.conversant.credis.RedisException;

/**
 * 对RedisClient的全局管理(注册，获取)
 * 
 * @author chengdong
 */
public class RedisProviders {

    protected static Map<String, RedisClient> created = new HashMap<String, RedisClient>();

    protected static Map<String, ClientProvider> profiles = new HashMap<String, ClientProvider>();

    public synchronized static RedisClient getClient(RedisClientName clusterName) throws RedisException {
        if (!created.containsKey(clusterName.getName())) {
            if (!profiles.containsKey(clusterName.getName())) {
                throw new RedisException("can't find [" + clusterName.getName() + "]");
            }
            ClientProvider pvd = profiles.get(clusterName.getName());
            RedisClient client = pvd.buildClient();
            created.put(clusterName.getName(), client);
        }
        return created.get(clusterName.getName());
    }

    public synchronized static void addProfile(String clusterName, ClientProvider pvd) {
        if (clusterName != null && pvd != null) {
            profiles.put(clusterName, pvd);
        }
    }

    public synchronized static void setProfiles(Map<String, ClientProvider> srvs) {
        if (srvs != null) {
            profiles.putAll(srvs);
        }
    }

}
