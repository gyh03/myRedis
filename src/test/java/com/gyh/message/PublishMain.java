package com.gyh.message;

import java.util.HashSet;
import java.util.Set;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @author gyh
 * main�������Է�����Ϣ
 */
public class PublishMain {
	private static  JedisCluster jc;

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
		sendMessage(channel, "��������");
	}

	/**
	 * ������Ϣ
	 *
	 * @param channel Ƶ��
	 * @param message ��Ϣ
	 */
	public static void sendMessage(final String channel, final String message) {
	    Thread thread = new Thread(() -> {
			Long publish = jc.publish(channel, message);
	        System.out.println(String.format("��������: {%s} Ƶ��������Ϣ{%s} - {%s}", channel, message, publish));
	    });
	    System.out.println("�����߳�����:");
	    thread.setName("publishThread");
	    thread.start();
	}
}
