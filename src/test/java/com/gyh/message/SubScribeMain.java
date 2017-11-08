package com.gyh.message;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

import com.gyh.publish.ChatSubscribe;
/**
 * @author gyh
 * main方法测试订阅消息
 */
public class SubScribeMain {
	private static  JedisCluster jc;
	private static ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	static{
		Set<HostAndPort> jedisClusterNode = new HashSet<HostAndPort>();
		jedisClusterNode.add(new HostAndPort("192.168.59.129", 7000));
		jedisClusterNode.add(new HostAndPort("192.168.59.129", 7001));
		jedisClusterNode.add(new HostAndPort("192.168.59.129", 7002));
		jedisClusterNode.add(new HostAndPort("192.168.59.129", 7003));
		jedisClusterNode.add(new HostAndPort("192.168.59.129", 7004));
		jedisClusterNode.add(new HostAndPort("192.168.59.129", 7005));
		//GenericObjectPoolConfig goConfig = new GenericObjectPoolConfig();
		//JedisCluster jc = new JedisCluster(jedisClusterNode,2000,100, goConfig);
		JedisPoolConfig cfg = new JedisPoolConfig();
		cfg.setMaxTotal(100);
		cfg.setMaxIdle(20);
		cfg.setMaxWaitMillis(-1);
		cfg.setTestOnBorrow(true); 
		jc = new JedisCluster(jedisClusterNode,6000,1000,cfg);	  
	}

	public static void main(String[] args) {
		final String  channel = "cctv1";
		subscribeChannel(channel, new ChatSubscribe());
	}

	/**
	 * 订阅频道
	 *
	 * @param channel          频道
	 * @param roomSubListerner
	 */
	public static void subscribeChannel(final String channel, final ChatSubscribe roomSubListerner) {

		cachedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				jc.subscribe(roomSubListerner, channel);
			}
		});
	}
}
