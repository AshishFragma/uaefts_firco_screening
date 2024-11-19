package com.uaefts.firco.dao;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.stereotype.Repository;

import com.uaefts.firco.constants.Constant;

@Repository
public class FircoQueueDao {
	
	@Autowired
	private JdbcTemplate jdbcTemplete;
	
	public void getItemsForInAMLQ(String action, String msgId, String f20, String msgType, String msgSent,
			String initial_response_status, String initial_response_text, String final_response_status,
			String final_response_text, String fileName, String msgIdMq) {
		Map<String, Object> inputParameters = new HashMap<String, Object>();
		inputParameters.put(Constant.SP_PARAM_ACTION, action);
		createInputParamsForStoredProc( inputParameters,  msgId,  f20,  msgType,  msgSent,
				 initial_response_status,  initial_response_text,  final_response_status,
				 final_response_text,  fileName,  msgIdMq);
		getSimpleJdbcCall().withProcedureName(Constant.SP_GET_ITEMS_FOR_INAMLQ).execute(inputParameters);
	}
	
	public Map<String, Object> fircoScannerMsgProcedure(String direction, String fileName, String amlvFile,
			String msgId, String amlStatus) {
		Map<String, Object> inPutParameters = new HashMap<>();
		inPutParameters.put(Constant.FIRCO_AML_STATUS, amlStatus);
		inPutParameters.put(Constant.FIRCO_AMLV_FILE, amlvFile);
		inPutParameters.put(Constant.FIRCO_DIRECTION, direction);
		inPutParameters.put(Constant.FIRCO_IN_FILE_NAME, fileName);
		inPutParameters.put(Constant.MSG_ID, msgId);
		return getSimpleJdbcCall().withProcedureName(Constant.P_AML_SCANNER_FOR_OTHER_MSG_FIRCO).execute(inPutParameters);
	}
	
	private Map<String, Object> createInputParamsForStoredProc(Map<String, Object> inputParameters, String msgId, String f20, String msgType,
			String inputMsg, String initial_response_status, String initial_response_text, String final_response_status,
			String final_response_text, String fileName, String msgIdMq) {

		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_MSG_ID, msgId);
		inputParameters.put(Constant.STRING_F20, f20);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID, msgType);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_MSG_SENT, inputMsg);
		inputParameters.put(Constant.INITIAL_RESPONSE_STATUS, initial_response_status);
		inputParameters.put(Constant.INITIAL_RESPONSE_TEXT, initial_response_text);
		inputParameters.put(Constant.FINAL_RESPONSE_STATUS, final_response_status);
		inputParameters.put(Constant.FINAL_RESPONSE_TEXT, final_response_text);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_FILENAMES, fileName);
		inputParameters.put(Constant.MSGID_MQ, msgIdMq);
		//inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_TRANSACTION_ID, transactionId);
		return inputParameters;
	}

	public Map<String, Object> getItemsFircoOutQueueInAMLQ(String action) {
		Map<String, Object> inputParameters = new HashMap<>();
		inputParameters.put(Constant.SP_PARAM_ACTION, action);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_MSG_ID, null);
		inputParameters.put(Constant.STRING_F20, null);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_MSG_SENT, null);
		inputParameters.put(Constant.INITIAL_RESPONSE_STATUS, null);
		inputParameters.put(Constant.INITIAL_RESPONSE_TEXT, null);
		inputParameters.put(Constant.FINAL_RESPONSE_STATUS, null);
		inputParameters.put(Constant.FINAL_RESPONSE_TEXT, null);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_FILENAMES, null);
		inputParameters.put(Constant.MSGID_MQ, null);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID, null);
		inputParameters.put(Constant.FIRCO_OUTPUT_PARAM_TRANSACTION_ID_KEY, null);
		return getSimpleJdbcCall().withProcedureName(Constant.SP_GET_ITEMS_FOR_INAMLQ).execute(inputParameters);
	}
	
	public Map<String, Object> getItemsForOutAMLQ(String action){
		Map<String, Object> inputParameters = new HashMap<String, Object>();
		inputParameters.put(Constant.SP_PARAM_ACTION, action);
		createInputParamsForStoredProc(inputParameters,  null,  null,  null,  null,null,  null,  null,null,  null,  null); 
		return getSimpleJdbcCall().withProcedureName(Constant.SP_GET_ITEMS_FOR_OUT_AMLQ).execute(inputParameters);
	}
	
	
	
	public SimpleJdbcCall getSimpleJdbcCall() {
		return new SimpleJdbcCall(jdbcTemplete);
	}

}
