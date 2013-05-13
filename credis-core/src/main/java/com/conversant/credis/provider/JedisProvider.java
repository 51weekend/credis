package com.conversant.credis.provider;

import java.util.ArrayList;
import java.util.List;

import redis.clients.jedis.JedisShardInfo;

import com.conversant.credis.RedisClient;

/**
 * 采用Jedis客户端
 * 
 * @author chengdong
 */
public class JedisProvider implements ClientProvider {

    // 格式为： 127.0.0.1:6379,10.1.2.242:6379 多个之间用逗号分隔
    // 对于server的权重统一用默认值
    protected String servers;

    private int timeout = 3000; // 3seconds

    private boolean usePool = true;

    public String getServers() {
        return servers;
    }

    public void setServers(String servers) {
        this.servers = servers;
    }

    public boolean isUsePool() {
        return usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public RedisClient buildClient() {
        List<JedisShardInfo> shardInfoList = new ArrayList<JedisShardInfo>();
        for (String server : servers.split("[,]")) {
            String[] sa = server.split("[:]");
            if (sa.length == 2) {
                String host = sa[0];
                int port = Integer.parseInt(sa[1]);
                // new JedisShardInfo(host, port);
                shardInfoList.add(new JedisShardInfo(host, port, timeout));
            }
        }
        return new ShardedJedisWrapper(shardInfoList, usePool);
    }
}
