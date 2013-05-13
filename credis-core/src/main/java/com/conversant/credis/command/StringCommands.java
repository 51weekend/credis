/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-9-25.
 */

package com.conversant.credis.command;

import com.conversant.credis.RedisException;

/**
 * Key-string相关命令.
 *
 * @author chengdong
 */
public interface StringCommands {

    /**
     * 将字符串值value关联到key
     * 
     * @param key
     * @param value
     * @return status code reply
     * @throws RedisException
     */
    String set(String key, String value) throws RedisException;

    /**
     * 将key中储存的数字值加1,如果key不存在,以0为key的初始值,然后执行INCR操作。
     * 
     * @param key
     * @return new value
     * @throws RedisException
     */
    long incr(String key) throws RedisException;

    /**
     * 将key中储存的数字值加n,如果key不存在,以0为key的初始值,然后执行INCRBY操作。
     * 
     * @param key
     * @param n
     * @return
     * @throws RedisException
     */
    long incrBy(String key, long n) throws RedisException;

    /**
     * 返回key所关联的字符串值,如果key不存在则返回特殊值nil.<BR>
     * 如果key对应的value不是字符串类型，返回一个错误，因为GET只能用于处理字符串值
     * 
     * @param key
     * @return
     * @throws RedisException
     */
    String get(String key) throws RedisException;

}
