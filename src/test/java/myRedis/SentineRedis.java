package com.gyh.myRedis;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.Pipeline;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPipeline;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.Transaction;
 
/**
 * @author gyh
 * Jedis分片连接池(分布式)
 */
public class SentineRedis {
 
    private static Jedis jedis;
    private static ShardedJedis shard;
    private static ShardedJedisPool pool;
 
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    	//单个节点
    	jedis = new Jedis("192.168.59.128", 6379);
    	
    	//Jedis分片连接池(分布式)
        List<JedisShardInfo> shards = Arrays.asList(
                new JedisShardInfo("127.0.0.1",6379)); 
        shard = new ShardedJedis(shards);
 
        GenericObjectPoolConfig goConfig = new GenericObjectPoolConfig();
        goConfig.setMaxTotal(100);
        goConfig.setMaxIdle(20);
        goConfig.setMaxWaitMillis(-1);
        goConfig.setTestOnBorrow(true);        
        pool = new ShardedJedisPool(goConfig, shards);
    }
 
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        jedis.disconnect();
        shard.disconnect();
        pool.destroy();
    }
 
  //字符串
  	@Test
  	public void testStr(){
  		//-----添加数据----------  
  		jedis.set("name","gyh");//向key-->name中放入了value-->gyh  
  		System.out.println(jedis.get("name"));//执行结果：xinxin  

  		jedis.append("name", " is my lover"); //拼接
  		System.out.println(jedis.get("name")); 
  		 //删除某个键
  		jedis.del("name"); 
  		System.out.println(jedis.get("name"));
  		//设置多个键值对
  		jedis.mset("name","gyh","age","27","qq","123456");
  		jedis.incr("age"); //进行加1操作
  		System.out.println(jedis.get("name") + "-" + jedis.get("age") + "-" + jedis.get("qq"));	

  	}
  	
  	//Hash：map类型
  	@Test
  	public  void testHash(){
  		//向myMap集合中添加一组k/v
  		jedis.hset("myMap", "name", "gyh"); 
  		Map<String,String> myMap = new HashMap<>();
  		myMap.put("age", "26");
  		myMap.put("sex", "女");
  		//批量添加数据
  		jedis.hmset("myMap", myMap);

  		//获取指定字段
  		String age = jedis.hget("myMap", "age");
  		if(age != null){
  			Integer age_ = Integer.valueOf(age);
  			System.out.println("age="+age_);
  		}
  		//获取整个map
  		Map<String,String> redisMap = jedis.hgetAll("myMap");
  		Set<Entry<String,String>> set = redisMap.entrySet();
  		for (Entry<String, String> entry : set) {
  			System.out.println("key="+entry.getKey()+";value="+entry.getValue());
  		}
  	}

  	//list集合，可做 栈 或 队列
  	@Test
  	public  void testList(){  
  		//开始前，先移除所有的内容  
  		jedis.del("myList"); 
  		System.out.println(jedis.lrange("myList",0,-1));  
  		//先向key myList中存放三条数据  
  		jedis.lpush("myList","aaa");  
  		jedis.lpush("myList","bbb","ccc"); //存放多个值
  		//再取出所有数据jedis.lrange是按范围取出，  
  		List<String> myList = jedis.lrange("myList",0,-1);
  		for (int i = 0; i < myList.size(); i++) {
  			System.out.println(i+">>>"+myList.get(i));
  		}
  		// 第一个是key，第二个是起始位置，第三个是结束位置，jedis.llen获取长度 -1表示取得所有  
  		System.out.println(jedis.lrange("myList",0,-1));  

  	}

  	//Set集合，无序不重复
  	@Test
  	public void testSet(){  
  		//添加  
  		jedis.sadd("mySet","liuling");  
  		jedis.sadd("mySet","xinxin");  
  		jedis.sadd("mySet","ling");  
  		jedis.sadd("mySet","zhangxinxin");
  		jedis.sadd("mySet","who");  
  	
  		//移除noname  
  		jedis.srem("mySet","who");  
  		Set<String> mySet = jedis.smembers("mySet");
  		for (String value : mySet) {
  			System.out.println(value);
  		}
  		
  		System.out.println("----------");
  		System.out.println(jedis.smembers("mySet"));//获取所有加入的value  
  		System.out.println(jedis.sismember("mySet", "who"));//判断 who 是否是user集合的元素  
  		System.out.println(jedis.srandmember("mySet"));  //随机返回一个值
  		System.out.println(jedis.scard("mySet"));//返回集合的元素个数  
  	} 

    @Test
    public void testTrans() {
    	System.out.println("===============================");
        long start = System.currentTimeMillis();
        Transaction tx = jedis.multi();
        for (int i = 0; i < 1000; i++) {
            tx.set("t" + i, "t" + i);
        }
        //System.out.println(tx.get("t1000").get());
 
        List<Object> results = tx.exec();
        long end = System.currentTimeMillis();
        System.out.println("Transaction SET: " + ((end - start)/1000.0) + " seconds");
    }
 
    @Test
    public void testPipelined() {
        Pipeline pipeline = jedis.pipelined();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 1000; i++) {
            pipeline.set("p" + i, "p" + i);
        }
        //System.out.println(pipeline.get("p1000").get());
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined SET: " + ((end - start)/1000.0) + " seconds");
    }
 
    @Test
    public void testPipelineTrans() {
        long start = System.currentTimeMillis();
        Pipeline pipeline = jedis.pipelined();
        pipeline.multi();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("" + i, "" + i);
        }
        pipeline.exec();
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("Pipelined transaction SET: " + ((end - start)/1000.0) + " seconds");
    }
 
    @Test
    public void testShard() {
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = shard.set("shard" + i, "n" + i);
        }
        long end = System.currentTimeMillis();
        System.out.println("shard SET: " + ((end - start)/1000.0) + " seconds");
    }
 
    @Test
    public void testShardpipelined() {
        ShardedJedisPipeline pipeline = shard.pipelined();
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            pipeline.set("sp" + i, "p" + i);
        }
        List<Object> results = pipeline.syncAndReturnAll();
        long end = System.currentTimeMillis();
        System.out.println("shardPipelined SET: " + ((end - start)/1000.0) + " seconds");
    }
 
    @Test
    public void testShardPool() {
        ShardedJedis sj = pool.getResource();
 
        long start = System.currentTimeMillis();
        for (int i = 0; i < 100000; i++) {
            String result = sj.set("spn" + i, "n" + i);
        }
        long end = System.currentTimeMillis();
        pool.returnResource(sj);
        System.out.println("shardPool SET: " + ((end - start)/1000.0) + " seconds");
    }
}