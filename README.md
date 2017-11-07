# myRedis
redis demo
Java操作redis的demo</br>
redis的单机、主从复制、集群 部署，参考我的印象笔记，【redis 安装笔记：单台/主从/集群】篇</br>
本项目中，在src/test/java 下，有对redis的简单操作例子，单机，主从，集群的连接和操作；</br>
还有spring整合redis，连接集群配置，把redisCluster注入到spring中，直接操作redis。</br>
redis集群是按槽存储的，具体存储模式参考笔记。所以不是每个主节点都存储全部的数据，但每个主从节点都可以查询数据，真正实现了分布式存储。
