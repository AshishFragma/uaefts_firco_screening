package com.uaefts.firco.activity;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.uaefts.firco.config.ApplicationContext;
import com.uaefts.firco.constants.Constant;
import com.uaefts.firco.servie.FircoQueueService;
import com.uaefts.firco.servie.PushMqService;

@Component
public class FircoSubmissionActivity implements Activity {

	private static final Logger log = LoggerFactory.getLogger(FircoSubmissionActivity.class);


	@Autowired
	private PushMqService pushMqService;

	@Autowired
	FircoQueueService fircoQueueService;

	@Value("${firco.in.header}")
	private String header;

	@Value("${firco.in.footer}")
	private String footer;

	@Override
	public Outcome execute(ApplicationContext context) {
		log.info("FircoSubmissionActivity started");

		try {

			String strHeader = header;
			String strFooter = footer;
			String msgTypeId = null;
			String strF20 = null;
			String strMsg = null;
			String strFile = null;
			String msgId = null;
			String transactionId = null;

			Map<String, Object> outputParam = fircoQueueService.fircoScannerMsgProcedure("INQ", null, null, null, null);

			// List<String> transactionIdList = new ArrayList<>();
			for (String key : outputParam.keySet()) {
				List<Map<String, Object>> outputList = (List<Map<String, Object>>) outputParam.get(key);
				for (Map<String, Object> output : outputList) {
					msgTypeId = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_MESSAGE_TYPE_ID_KEY));
					strF20 = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_SEND_INST_REF_KEY));
					strMsg = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_KEY));
					strFile = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_FILENAMES_KEY));
					msgId = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_ID_KEY));
					transactionId = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_TRANSACTION_ID_KEY));

					String messageId = generateMsgIdMq();
					boolean isSend = pushMqService.sendMessage(strHeader + strF20 + strMsg + strFooter, messageId);

					if (isSend) {
						//String msgSent = strHeader + strF20 + strMsg + strFooter;
						String msgSent = strF20+strMsg;
						fircoQueueService.insertOrUpdateBySPInAMLQ(Constant.INSERT, msgId, strF20, msgTypeId, msgSent,
								null, null, null, null, strFile, messageId, transactionId);
						// transactionIdList.add(transactionId);
					}

				}
			}

			log.info("FircoSubmissionActivity Completed");
			return Outcome.SUCCESS;

			// context.setTransactionIdList(transactionIdList);
		} catch (Exception e) {
			log.error("Failed to push data" + e.getMessage());
			return Outcome.FAILURE;
		}
	}

	private String generateMsgIdMq() {
		String paddingChar = "0";
		return UUID.randomUUID().toString().replace("-", "") + paddingChar.repeat(48);
	}

	@Override
	public String getName() {
		return "FircoSubmissionActivity";
	}

	/*
	 * Step
	 * 
	 * 1. Insert one row in transaction_step table with status pending and provide
	 * sequence id or status(Firco Submission) 2. call procedure
	 * AMLScannerForOtherMsg_FIRCO(‘INQ’,NULL, NULL,NULL,NULL); 3. success response
	 * then will update status as success in transaction_step table. 4. failed
	 * response then will update status as failed in transaction_step table. 5. In
	 * case of application error will retry process by calling
	 * AMLScannerForOtherMsg_FIRCO but we need to validate that is record already
	 * inserted of not.
	 */
}