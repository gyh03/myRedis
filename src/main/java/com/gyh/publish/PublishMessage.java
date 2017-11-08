package com.gyh.publish;


import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;

@Component
public class PublishMessage {
	private Logger logger = LoggerFactory.getLogger(PublishMessage.class);

	@Resource
	private JedisCluster jedisCluster;

	/**
	 * 发布消息
	 *
	 * @param channel 频道
	 * @param message 信息
	 */
	public void sendMessage(final String channel, final String message) {
	    Thread thread = new Thread(() -> {
	        Long publish = jedisCluster.publish(channel, message);
	        logger.info("服务器在: {} 频道发布消息{} - {}", channel, message, publish);
	    });
	    logger.info("发布线程启动:");
	    thread.setName("publishThread");
	    thread.start();
	}
}
