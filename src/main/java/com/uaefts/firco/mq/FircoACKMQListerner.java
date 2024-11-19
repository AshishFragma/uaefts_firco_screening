
package com.uaefts.firco.mq;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.handler.annotation.Headers;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.uaefts.firco.constants.Constant;
import com.uaefts.firco.dto.FircoAckResponseDto;
import com.uaefts.firco.servie.FircoQueueService;

import jakarta.jms.JMSException;

@Component
public class FircoACKMQListerner {

	private static final Logger log = LoggerFactory.getLogger(FircoACKMQListerner.class);

	private final Executor fircoAckQueueExecutor = Executors.newFixedThreadPool(10);

	@Autowired
	FircoQueueService fircoQueueService;

	@Value("${firco.ack.queue.read.flag}")
	String fircoAckQueueResponse;

	@Value("${ack.mq.error.response}")
	String errorFilePath;

	@Value("${ack.mq.success.response}")
	String successFilePath;

	@Value("${firco.ack.feature.flag}")
	String fircoAckFeatureFlag;

	@Value("${Queue.init.response}")
	String fircoInitResponse;

	@JmsListener(destination = "${ibm.mq.push.queue}")	
	public void onMessage(@Payload String message, @Headers MessageHeaders messageHeaders)
			throws JMSException, IOException {

		log.info("Firco Ack Queue onMessage " + message);

		CompletableFuture.runAsync(() -> {
			try {
				processMessage(message);
			} catch (IOException e) {
				log.error("IOException in Firco Ack Queue - onMessage -"+e.getMessage());
			}
		}, fircoAckQueueExecutor).exceptionally(ex -> {

			log.error("Handle Error");
			
			return null;
		});

	}

	private void processMessage(String message) throws IOException {
		
		log.info("processMessage Started");
		
		FircoAckResponseDto ackResponseDto = null;
//		boolean isError = false;
		File file = null;
		
		if (fircoAckFeatureFlag.equals("true")) {
			ackResponseDto = new FircoAckResponseDto();
			if (fircoAckQueueResponse.equals(Constant.FIRCO_MQ_RESPONSE_HIT)
					|| fircoAckQueueResponse.equals(Constant.FIRCO_MQ_RESPONSE_NO_HIT)) {
				file = new File(successFilePath);
			} else {
				file = new File(errorFilePath);
			}
			System.out.println("Processing file ::" + file.getName());
			if (file.exists()) {
				byte[] bytes = Files.readAllBytes(file.toPath());
				ackResponseDto.setResponse(new String(bytes));
				ackResponseDto.setResponseCode(fircoAckQueueResponse);
				fircoQueueService.processFircoAckMqListernerResponse(ackResponseDto);

			}
		} else {
			log.info("Message fetch from the queue successfully.");
			String responseCode = queueResponseContains(message);

			if (responseCode != null) {
				ackResponseDto = new FircoAckResponseDto();
				ackResponseDto.setResponse(message);
				ackResponseDto.setResponseCode(responseCode);

				fircoQueueService.processFircoAckMqListernerResponse(ackResponseDto);
			}
		}
	}

	private String queueResponseContains(String queueResponse) {
		String defaultMsgTypes = fircoInitResponse;
		String[] tmp = defaultMsgTypes.split(Pattern.quote("$"));
		String msgType = "";
		for (int i = 0; i < tmp.length && msgType.length() < 1; i++) {
			msgType = queueResponse.substring(365, 368).contains(tmp[i].trim()) ? tmp[i].trim() : "";
		}
		return msgType;
	}

	public String getErrorFilePath() {
		return errorFilePath;
	}

	public String getSuccessFilePath() {
		return successFilePath;
	}

	public void setErrorFilePath(String errorFilePath) {
		this.errorFilePath = errorFilePath;
	}

	public void setSuccessFilePath(String successFilePath) {
		this.successFilePath = successFilePath;
	}

}
