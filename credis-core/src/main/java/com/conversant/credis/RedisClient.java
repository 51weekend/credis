/*
 * Copyright (c) 2012 Shanda Corporation. All rights reserved.
 *
 * Created on 2012-9-25.
 */

package com.conversant.credis;

import com.conversant.credis.command.GenericCommands;
import com.conversant.credis.command.HashCommands;
import com.conversant.credis.command.ListCommands;
import com.conversant.credis.command.StringCommands;

/**
 * 有关redis的命令参考：http://redis.readthedocs.org/en/latest/ <BR>
 * 没有定义所有redis支持的命令，只列出了我们目前需要的
 *
 * @author chengdong
 */
public interface RedisClient extends GenericCommands, StringCommands, ListCommands, HashCommands {

}
