package com.gyh.message;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import redis.clients.jedis.JedisCluster;
@Component
public class SubScribeMessage {
	private Logger logger = LoggerFactory.getLogger(SubScribeMessage.class);

	private ExecutorService cachedThreadPool = Executors.newCachedThreadPool();

	@Resource
	private JedisCluster jedisCluster;
	/**
	 * 订阅频道
	 *
	 * @param channel          频道
	 * @param roomSubListerner
	 */
	public void subscribeChannel(final String channel, final ChatSubscribe roomSubListerner) {

		cachedThreadPool.execute(new Runnable() {
			@Override
			public void run() {
				jedisCluster.subscribe(roomSubListerner, channel);
			}
		});
	}
}
