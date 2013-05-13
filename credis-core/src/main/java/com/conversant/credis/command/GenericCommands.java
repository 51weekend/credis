package com.conversant.credis.command;

import com.conversant.credis.RedisException;

/**
 * key的通用命令
 * 
 * @see http://redis.io/commands#generic
 * @see http://redis.readthedocs.org/en/latest/
 * @author chengdong
 */
public interface GenericCommands {

    /**
     * 移除给定的一个或多个key
     * 
     * @param keys
     * @return 返回删除的元素个数
     * @throws RedisException
     */
    long remove(String... keys) throws RedisException;

    /**
     * 返回给定key的剩余生存时间(time to live)(以秒为单位)
     * 
     * @param key
     * @return
     * @throws RedisException
     */
    long ttl(String key) throws RedisException;

    /**
     * 检查给定key是否存在
     * 
     * @param key
     * @return
     * @throws RedisException
     */
    boolean exists(String key) throws RedisException;

    /**
     * 为给定key设置生存时间。当key过期时，它会被自动删除。<BR>
     * 在Redis中，带有生存时间的key被称作“易失的”(volatile) <BR>
     * 在低于2.1.3版本的Redis中，已存在的生存时间不可覆盖 <BR>
     * 从2.1.3版本开始，key的生存时间可以被更新，也可以被PERSIST命令移除 <BR>
     * (详情参见 http://redis.io/topics/expire)
     * 
     * @param key
     * @param seconds
     * @return
     * @throws RedisException
     */
    long expire(String key, int seconds) throws RedisException;
    
    /**
     * 将 key 改名为 newkey 。<BR>
     * 当 key 和 newkey 相同，或者 key 不存在时，返回一个错误.<BR>
     * 当 newkey 已经存在时， RENAME 命令将覆盖旧值.
     * @param oldkey
     * @param newkey
     * @return
     */
    String rename(String oldkey, String newkey);

}
