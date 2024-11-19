package com.uaefts.firco.mq;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import com.uaefts.firco.constants.Constant;
import com.uaefts.firco.dto.FircoOutResponseDto;
import com.uaefts.firco.servie.FircoQueueService;

import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.TextMessage;

@Component
public class FircoOUTMQListerner {

	private static final Logger log = LoggerFactory.getLogger(FircoOUTMQListerner.class);

	private final String outQueueName;

	public FircoOUTMQListerner(@Value("${ibm.mq.out.queue}") String outQueueName) {
		this.outQueueName = outQueueName;
	}

	@Value("${firco.out.queue.read.flag}")
	String fircoOutQueueResponse;

	@Value("${firco.out.mq.block.response}")
	String fircoOutMqBlockFilePath;

	@Value("${firco.out.mq.release.response}")
	String fircoOutMqReleaseFilePath;

	@Value("${firco.out.feature.flag}")
	String fircoOutFeatureFlag;

	@Autowired
	private JmsTemplate jmsTemplate;

	@Autowired
	FircoQueueService fircoQueueService;

	
	public void fircoMessageReceiver(Map<String, Map<String, String>> messageIdsList) {
		FircoOutResponseDto outResponseDto = new FircoOutResponseDto();
		String queueResponse = "";

		try {
			File file = null;
			if (fircoOutFeatureFlag.equals("true")) {
				if (fircoOutQueueResponse.equals(Constant.FIRCO_MQ_RESPONSE_RELEASE)) {
					file = new File(fircoOutMqReleaseFilePath);
				}
				if (file.exists()) {
					byte[] bytes = Files.readAllBytes(file.toPath());
					outResponseDto.setFinalResonseText(new String(bytes));
					outResponseDto.setFinalResponseStatus(queueResponseContains(outResponseDto.getFinalResonseText()));
					for (Entry<String, Map<String, String>> set : messageIdsList.entrySet()) {
						fircoQueueService.processFircoOutMqListernerResponse(outResponseDto, set.getKey(),
								set.getValue());
					}
				}
			} else {

				for (Entry<String, Map<String, String>> set : messageIdsList.entrySet()) {
					String messageId = set.getKey();
					String selector = "messageId = '" + set.getKey() + "'";
					Message message = jmsTemplate.receiveSelected(outQueueName, selector);

					log.info("Message From Firco Out Queue - "+message);
					
					if (message instanceof TextMessage) {
						try {
							queueResponse = ((TextMessage) message).getText();
							String msgType = queueResponseContains(queueResponse);
							if (queueResponse.contains(messageId) && (msgType != null && !msgType.isBlank())) {
								outResponseDto.setFinalResonseText(queueResponse);
								outResponseDto.setFinalResponseStatus(msgType);
								fircoQueueService.processFircoOutMqListernerResponse(outResponseDto, messageId,
										set.getValue());
								
								log.info(outQueueName + " Response Status :: " + outResponseDto.getFinalResponseStatus());
								
							} else {
								log.error("Message with Id: " + messageId + ", not contains in queue response : "
										+ queueResponse);
							}

						} catch (JMSException e) {
							log.error("Failed to read message from out queue " + e.getMessage());
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} 
	}

	private String queueResponseContains(String queueResponse) {
		String defaultMsgTypes = "002$003$005$009$010$011$012";
		String[] tmp = defaultMsgTypes.split(Pattern.quote("$"));
		String msgType = "";
		for (int i = 0; i < tmp.length && msgType.length() < 1; i++) {
			msgType = queueResponse.substring(365, 368).contains(tmp[i].trim()) ? tmp[i].trim() : "";
		}
		return msgType;
	}

}
