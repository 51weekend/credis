package com.conversant.credis.command;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.conversant.credis.RedisException;

/**
 * Key-Hash 相关命令
 * 
 * @author chengdong 
 */
public interface HashCommands {

    /**
     * 将哈希表key中的域field的值设为value。<BR>
     * 如果key不存在，一个新的哈希表被创建并进行HSET操作。
     * 
     * @param key
     * @param field
     * @param value
     * @return
     * @throws RedisException
     */
    long hset(String key, String field, String value) throws RedisException;

    /**
     * 将哈希表key中的域field的值设为value，如果field不存在的话
     * 
     * @param key
     * @param field
     * @param value
     * @return
     * @throws RedisException
     */
    long hsetnx(String key, String field, String value) throws RedisException;

    /**
     * 同时将多个field - value(域-值)对设置到哈希表key中
     * 
     * @param key
     * @param hash
     * @return
     * @throws RedisException
     */
    String hmset(String key, Map<String, String> hash) throws RedisException;

    /**
     * 返回哈希表key中给定域field的值。
     * 
     * @param key
     * @param field
     * @return
     * @throws RedisException
     */
    String hget(String key, String field) throws RedisException;

    /**
     * 返回哈希表key中，所有的域和值。
     * 
     * @param key
     * @return
     * @throws RedisException
     */
    Map<String, String> hgetAll(String key) throws RedisException;

    /**
     * 返回哈希表key中，一个或多个给定域的值<BR>
     * 如果给定的域不存在于哈希表，那么返回一个nil值。
     * 
     * @param key
     * @param fields
     * @return
     * @throws RedisException
     */
    List<String> hmget(String key, String... fields) throws RedisException;

    /**
     * 返回Hash表中的元素个数
     * 
     * @param key
     * @return
     * @throws RedisException
     */
    long hlen(String key) throws RedisException;

    /**
     * 返回Hash表中的keys
     * 
     * @param key
     * @return
     * @throws RedisException
     */
    Set<String> hkeys(String key) throws RedisException;

    /**
     * 返回Hash表中的values
     * 
     * @param key
     * @return
     * @throws RedisException
     */
    List<String> hvals(String key) throws RedisException;

    /**
     * 查看哈希表key中，给定域field是否存在
     * 
     * @param key
     * @param field
     * @return
     * @throws RedisException
     */
    boolean hexists(String key, String field) throws RedisException;

    /**
     * 删除哈希表key中的一个或多个指定域
     * 
     * @param key
     * @param field
     * @return
     * @throws RedisException
     */
    long hdel(String key, String... field) throws RedisException;

    /**
     * 查看哈希表key中，给定域field 值加increment
     * 
     * @param key
     * @param field
     * @param increment
     * @return
     * @throws RedisException
     */
    long hincrby(String key, String field, long increment) throws RedisException;
}
