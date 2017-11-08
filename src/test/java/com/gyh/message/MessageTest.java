package com.gyh.message;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * @author gyh
 * 发布订阅消息
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = {"classpath:spring-mvc.xml"})  

public class MessageTest {

	@Autowired
	private SubScribeMessage subScribeMessage;
	@Autowired
	private PublishMessage publishMessage;
	
	private final String channel = "myCctv";
	
	@Test
	public void subScribeTest() {
		subScribeMessage.subscribeChannel(channel, new ChatSubscribe());
	}
	
	@Test
	public void publishTest() {
		publishMessage.sendMessage(channel, "hello 111111>>>");
	}

}
