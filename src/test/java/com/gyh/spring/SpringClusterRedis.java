package com.gyh.spring;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import redis.clients.jedis.JedisCluster;

/**
 * @author gyh
 * spring 整合redis集群
 */
@RunWith(SpringJUnit4ClassRunner.class)     //表示继承了SpringJUnit4ClassRunner类  
@ContextConfiguration(locations = {"classpath:spring-mvc.xml"})  
public class SpringClusterRedis {

	@Autowired
	private JedisCluster redisCluster;
	
	//字符串
	@Test
	public void testStr(){
		//-----添加数据----------  
		redisCluster.set("name","gyh");//向key-->name中放入了value-->gyh  
		System.out.println(redisCluster.get("name"));//执行结果：xinxin  

		redisCluster.append("name", " is my lover"); //拼接
		System.out.println(redisCluster.get("name")); 
		//删除某个键
		redisCluster.del("name"); 
		System.out.println(redisCluster.get("name"));
		//设置多个键值对
		redisCluster.mset("name","gyh","age","27","qq","123456");
		redisCluster.incr("age"); //进行加1操作
		System.out.println(redisCluster.get("name") + "-" + redisCluster.get("age") + "-" + redisCluster.get("qq"));	
		redisCluster.save();

	}

	//Hash：map类型
	@Test
	public  void testHash(){
		//向myMap集合中添加一组k/v
		redisCluster.hset("myMap", "name", "gyh"); 
		Map<String,String> myMap = new HashMap<>();
		myMap.put("age", "26");
		myMap.put("sex", "女");
		//批量添加数据
		redisCluster.hmset("myMap", myMap);

		//获取指定字段
		String age = redisCluster.hget("myMap", "age");
		if(age != null){
			Integer age_ = Integer.valueOf(age);
			System.out.println("age="+age_);
		}
		//获取整个map
		Map<String,String> redisMap = redisCluster.hgetAll("myMap");
		Set<Entry<String,String>> set = redisMap.entrySet();
		for (Entry<String, String> entry : set) {
			System.out.println("key="+entry.getKey()+";value="+entry.getValue());
		}
	}

	//list集合，可做 栈 或 队列
	@Test
	public  void testList(){  
		//开始前，先移除所有的内容  
		redisCluster.del("myList"); 
		System.out.println(redisCluster.lrange("myList",0,-1));  
		//先向key myList中存放三条数据  
		redisCluster.lpush("myList","aaa");  
		redisCluster.lpush("myList","bbb","ccc"); //存放多个值
		//再取出所有数据jedis.lrange是按范围取出，  
		List<String> myList = redisCluster.lrange("myList",0,-1);
		for (int i = 0; i < myList.size(); i++) {
			System.out.println(i+">>>"+myList.get(i));
		}
		// 第一个是key，第二个是起始位置，第三个是结束位置，redisCluster.llen获取长度 -1表示取得所有  
		System.out.println(redisCluster.lrange("myList",0,-1));  

	}

	//Set集合，无序不重复
	@Test
	public void testSet(){  
		//添加  
		redisCluster.sadd("mySet","liuling");  
		redisCluster.sadd("mySet","xinxin");  
		redisCluster.sadd("mySet","ling");  
		redisCluster.sadd("mySet","zhangxinxin");
		redisCluster.sadd("mySet","who");  

		//移除noname  
		redisCluster.srem("mySet","who");  
		Set<String> mySet = redisCluster.smembers("mySet");
		for (String value : mySet) {
			System.out.println(value);
		}

		System.out.println("----------");
		System.out.println(redisCluster.smembers("mySet"));//获取所有加入的value  
		System.out.println(redisCluster.sismember("mySet", "who"));//判断 who 是否是user集合的元素  
		System.out.println(redisCluster.srandmember("mySet"));  //随机返回一个值
		System.out.println(redisCluster.scard("mySet"));//返回集合的元素个数  
	} 

	
}