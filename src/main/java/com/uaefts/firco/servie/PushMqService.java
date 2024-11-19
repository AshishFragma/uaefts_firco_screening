package com.uaefts.firco.servie;

import jakarta.jms.TextMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.JmsException;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

@Service
public class PushMqService {

	private static final Logger log = LoggerFactory.getLogger(PushMqService.class);

	@Value("${ibm.mq.push.queue}")
	private String pushQueue;

	@Autowired
	private JmsTemplate jmsTemplate;

	public boolean sendMessage(String messageData,String messageId) {
		boolean isSend = false;
		try {
			
			jmsTemplate.send(pushQueue,session->{
				TextMessage message=session.createTextMessage(messageData);
				message.setStringProperty("messageId", messageId);
				return message;
			});

			jmsTemplate.convertAndSend(pushQueue, messageData);
			isSend = true;
			log.info("Message sent successfully to queue: {}", pushQueue);

		}
		catch(JmsException ex){
			log.error("JmsException : FIRCO Submission Exception Occures - "+ex.getMessage());
			isSend = false;
		}
		catch(Exception e) {
			log.error("Exceptoin Occures while sending message - "+e.getMessage(),e);
			isSend = false;
		}
		
		return isSend;
	}
}
