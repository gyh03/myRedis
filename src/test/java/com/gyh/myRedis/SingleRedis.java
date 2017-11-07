package com.gyh.myRedis;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;

import redis.clients.jedis.Jedis;

/**
 * @author gyh
 * 单机redis连接
 */
public class SingleRedis {

	//单机redis连接
	private static Jedis jedis = new Jedis("192.168.59.128",6379);

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

}
