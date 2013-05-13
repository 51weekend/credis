package com.conversant.credis.provider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.pool.impl.GenericObjectPool;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.util.Hashing;

import com.conversant.credis.RedisClient;
import com.conversant.credis.RedisException;

/**
 * 基于ShardedJedisPool
 * 
 * @author chengdong
 */
public class ShardedJedisWrapper implements RedisClient {

    private ShardedJedisPool pool;

    private List<JedisShardInfo> shardInfoList;

    private boolean usePool = true;

    /**
     * 构造一个基于ShardedJedisPool的包装
     * 
     * @param servers 格式为：127.0.0.1:6379,10.20.1.2:6379 多个服务器间用","号分割
     */
    public ShardedJedisWrapper(String servers) {
        if (servers == null || servers.isEmpty() || servers.indexOf(":") < 0) {
            throw new IllegalArgumentException("servers error: " + servers);
        }

        List<JedisShardInfo> shardInfoList = new ArrayList<JedisShardInfo>();
        for (String server : servers.split("[,]")) {
            String[] sa = server.split("[:]");
            if (sa.length == 2) {
                String host = sa[0];
                int port = Integer.parseInt(sa[1]);
                shardInfoList.add(new JedisShardInfo(host, port));
            }
        }
        if (shardInfoList.isEmpty())
            throw new IllegalArgumentException("servers illegal: " + servers);
        this.pool = new ShardedJedisPool(new GenericObjectPool.Config(), shardInfoList);
    }

    /**
     * @param shardInfoList
     */
    public ShardedJedisWrapper(List<JedisShardInfo> shardInfoList, boolean usePool) {
        this.usePool = usePool;
        this.shardInfoList = shardInfoList;
        if (this.usePool) {
            GenericObjectPool.Config cfg = new GenericObjectPool.Config();
            // 默认开启 testOnBorrow
            cfg.maxActive = 300;
            cfg.maxIdle = 300;
            cfg.testOnBorrow = true;
            cfg.testOnReturn = true;
            cfg.testWhileIdle = true;
            this.pool = new ShardedJedisPool(cfg, shardInfoList);
        }
    }

    protected ShardedJedis fetchJedisConnector() throws RedisException {
        try {
            if (usePool && pool != null) {
                return pool.getResource();
            }
            ShardedJedis jedis = new ShardedJedis(this.getShardInfoList(), Hashing.MURMUR_HASH);
            return jedis;
        } catch (Exception ex) {
            throw new RedisException("Cannot get Jedis connection", ex);
        }
    }

    private void returnResource(ShardedJedis shardedJedis) {
        if (usePool && pool != null) {
            pool.returnResource(shardedJedis);
        } else {
            try {
                shardedJedis.disconnect();
            } catch (Exception e) {
            }
        }

    }

    private void returnBrokenResource(ShardedJedis shardedJedis) {
        if (usePool && pool != null) {
            pool.returnBrokenResource(shardedJedis);
        } else {
            try {
                shardedJedis.disconnect();
            } catch (Exception e) {
            }
        }
    }

    // 计算一致hash

    public List<JedisShardInfo> getShardInfoList() {
        return shardInfoList;
    }

    public void setShardInfoList(List<JedisShardInfo> shardInfoList) {
        this.shardInfoList = shardInfoList;
    }

    public boolean isUsePool() {
        return usePool;
    }

    public void setUsePool(boolean usePool) {
        this.usePool = usePool;
    }

    public ShardedJedisWrapper(List<JedisShardInfo> shardInfoList, GenericObjectPool.Config cfg) {
        this.pool = new ShardedJedisPool(cfg, shardInfoList);
    }

    @Override
    public String set(String key, String value) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.set(key, value);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public long incr(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.incr(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public long incrBy(String key, long n) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.incrBy(key, n);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public long ttl(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.ttl(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public boolean exists(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.exists(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public long expire(String key, int seconds) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.expire(key, seconds);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public long remove(String... keys) throws RedisException {
        long r = 0;
        for (String k : keys) {
            ShardedJedis jedis = null;
            boolean failed = false;
            try {
                jedis = this.fetchJedisConnector();
                r += jedis.del(k);
            } catch (Exception e) {
                failed = true;
                this.returnBrokenResource(jedis);
                throw new RedisException(e);
            } finally {
                if (jedis != null && !failed)
                    this.returnResource(jedis);
            }
        }
        return r;
    }

    @Override
    public String rename(String oldkey, String newkey) {
        throw new UnsupportedOperationException("ShardedJedis not support rename");
    }

    @Override
    public String get(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.get(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    // 测试每个连接是否ok，可能抛出JedisConnectionException
    @SuppressWarnings("unused")
    private boolean testConnection(ShardedJedis shardedJedis) {
        for (Jedis jedis : shardedJedis.getAllShards()) {
            jedis.ping();
        }
        return true;
    }

    @Override
    public long lpush(String key, String value) throws RedisException {

        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.lpush(key, value);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public String lindex(String key, long index) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.lindex(key, index);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public String ltrim(String key, int start, int end) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.ltrim(key, start, end);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public List<String> lrange(String key, long start, long end) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.lrange(key, start, end);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public long llen(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.llen(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    @Override
    public long lrem(String key, long count, String value) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.lrem(key, count, value);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public long hset(String key, String field, String value) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hset(key, field, value);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public long hsetnx(String key, String field, String value) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hsetnx(key, field, value);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public String hmset(String key, Map<String, String> hash) throws RedisException {
        if (hash == null || hash.size() == 0) {
            return "";
        }
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hmset(key, hash);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public String hget(String key, String field) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hget(key, field);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public Map<String, String> hgetAll(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hgetAll(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public List<String> hmget(String key, String... fields) throws RedisException {
        if (fields == null || fields.length == 0) {
            return new ArrayList<String>();
        }
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hmget(key, fields);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public long hlen(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hlen(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public Set<String> hkeys(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hkeys(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public List<String> hvals(String key) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hvals(key);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

    public boolean hexists(String key, String field) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hexists(key, field);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

   

    @Override
    public long hincrby(String key, String field, long increment) throws RedisException {
        ShardedJedis jedis = null;
        boolean failed = false;
        try {
            jedis = this.fetchJedisConnector();
            return jedis.hincrBy(key, field, increment);
        } catch (Exception e) {
            failed = true;
            this.returnBrokenResource(jedis);
            throw new RedisException(e);
        } finally {
            if (jedis != null && !failed)
                this.returnResource(jedis);
        }
    }

	@Override
	public long hdel(String key, String... field) throws RedisException {
		// TODO Auto-generated method stub
		return 0;
	}

}
