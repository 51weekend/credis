package com.conversant.credis.command;

import java.util.List;

import com.conversant.credis.RedisException;

/**
 * Key-List 相关命令
 * 
 * @author chengdong
 */
public interface ListCommands {

    /**
     * 将值value插入到列表key的表头。 如果key不存在，一个空列表会被创建并执行LPUSH操作。
     * 
     * @param key
     * @param value
     * @return list的元素个数
     * @throws ReidsException
     */
    long lpush(String key, String value) throws RedisException;

    /**
     * 返回列表key中，下标为index的元素。 <BR>
     * 下标(index)参数start和stop都以0为底，0表示列表的第一个元素，以1表示列表的第二个元素，以此类推<BR>
     * 你也可以使用负数下标，以-1表示列表的最后一个元素，-2表示列表的倒数第二个元素，以此类推。
     * 
     * @param key
     * @param index
     * @return
     * @throws ReidsException
     */
    String lindex(String key, long index) throws RedisException;

    /**
     * 对一个列表进行trim，让列表只保留指定区间内的元素，不在指定区间之内的元素都将被删除。<BR>
     * eg: 执行命令LTRIM list 0 2，表示只保留列表list的前三个元素，其余元素全部删除。
     * 
     * @param key
     * @param start
     * @param end
     * @return
     * @throws ReidsException
     */
    String ltrim(String key, int start, int end) throws RedisException;

    /**
     * 返回列表key中指定区间内的元素，区间以偏移量start和stop指定<BR>
     * 也可以使用负数下标，-1表示列表的最后一个元素，-2倒数第二，以此类推<BR>
     * 注意，区间范围 start和end都是闭区间，比如区间0,10会返回11个元素，而不是10个
     * 
     * @param key
     * @param start
     * @param end
     * @return
     * @throws ReidsException
     */
    List<String> lrange(String key, long start, long end) throws RedisException;
    
    /**
     * 返回列表长度
     * 
     * @param key
     * @return
     * @throws ReidsException
     */
    long llen(String key) throws RedisException;
    
    /**
     * 根据参数count的值，移除列表中与参数value相等的元素<BR>
     * count的值可以是以下几种：<BR>
     * count > 0: 从表头开始向表尾搜索，移除与value相等的元素，数量为count <BR>
     * count < 0: 从表尾开始向表头搜索，移除与value相等的元素，数量为count的绝对值 <BR>
     * count = 0: 移除表中所有与value相等的值 <BR>
     * 
     * @param key
     * @param count
     * @param value
     * @return
     * @throws ReidsException
     */
    long lrem(final String key, final long count, final String value) throws RedisException;

}
