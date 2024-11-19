package com.uaefts.firco.servie;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.uaefts.firco.constants.Constant;
import com.uaefts.firco.dao.FircoQueueDao;
import com.uaefts.firco.dto.FircoAckResponseDto;
import com.uaefts.firco.dto.FircoOutResponseDto;

@Service
public class FircoQueueService {
	
	private static final Logger log = LoggerFactory.getLogger(FircoQueueService.class);
	
	@Autowired
	FircoQueueDao fircoQueueDao;
	
	public Map<String, Object> fircoScannerMsgProcedure(String direction, String fileName, String amlvFile, String msgId, String amlStatus) {
		return fircoQueueDao.fircoScannerMsgProcedure(direction, fileName, amlvFile, msgId, amlStatus);
	}

	
	public void processFircoAckMqListernerResponse( FircoAckResponseDto ackResponseDto) {
		Map<String, String> parameters = readAckQueueResponse(ackResponseDto.getResponse(), ackResponseDto.getResponseCode());
	    insertOrUpdateBySPInAMLQ(Constant.INSERT_ACK_ACTION, null, parameters.get(Constant.STRING_F20), parameters.get(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID), "",ackResponseDto.getResponseCode() , ackResponseDto.getResponse(), null, null, null, null,null);
		insertOrUpdateBySPInAMLQ(Constant.UPDACK, null, parameters.get(Constant.STRING_F20), parameters.get(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID), "",ackResponseDto.getResponseCode() ,ackResponseDto.getResponse(), null, null, null,null,null);

	}
	
	private Map<String, String> readAckQueueResponse(String queueResponse, String responseStatus) {
		Map<String, String> params = new HashMap<>();
		String f20 = "";
		try {
			if(responseStatus.equals(Constant.FIRCO_MQ_RESPONSE_ERROR)) {
				f20 =(String) queueResponse.subSequence(0, queueResponse.indexOf("#"));
			}else if(responseStatus.equals(Constant.FIRCO_MQ_RESPONSE_HIT) || responseStatus.equals(Constant.FIRCO_MQ_RESPONSE_NO_HIT)) {	
				f20 = queueResponse.substring(queueResponse.indexOf("[RECORDTYP     X]")).split("#")[0] .replace("[RECORDTYP     X]", "").trim();
			}
			params.put(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID, f20.substring(f20.length()-4).trim());
			params.put(Constant.STRING_F20, f20.substring(0, f20.length()-4).trim());
			return params;
		}catch (Exception e) {
			log.error(e.getMessage()); 
		}
		return params;
	}
	
	public boolean insertOrUpdateBySPInAMLQ(String action,String msgId,String f20,String msgType,String msgSent,String initial_response_status,String initial_response_text,String final_response_status,String final_response_text,String fileNames ,String msgIdMq,String transactionId) {
		
		boolean isSPExecutedSucessfully=true;
		
		try {
			
			log.info("Execution of SP_GetItemsForInAMLQ from method - insertOrUpdateBySPInAMLQ started for action -"+action);
			log.info("Response for F20 - "+f20 +" msgId -"+msgId+" msgType -"+msgType+" msgSend "+msgSent+" initial Response Status"+initial_response_status
					+" Initial Response Text -"+initial_response_text+" Final Response Status -"+final_response_status+" Final Response Text - "+final_response_text
					+" File Name -"+fileNames+" msgIdMq  -"+msgIdMq);
			
			fircoQueueDao.getItemsForInAMLQ(action, msgId, f20, msgType, msgSent, initial_response_status, initial_response_text, final_response_status, final_response_text, fileNames, msgIdMq);
		
		}
		catch(Exception e) {
			log.error("Error while executing insertOrUpdateBySPInAMLQ method for action -"+action+e.getMessage());
			isSPExecutedSucessfully=false;
		}
		
		return isSPExecutedSucessfully;
	}
	
	public Map<String, Map<String, String>> getItemsForInAMLQ(String action) {
		return invokeInAMLQProcWithParams(fircoQueueDao.getItemsFircoOutQueueInAMLQ(action),action);
	}
	
	public void processFircoOutMqListernerResponse( FircoOutResponseDto outResponseDto, String strF20, Map<String, String> map) {
		insertOrUpdateBySPInAMLQ(Constant.UPDATE_OUT, map.get(Constant.MSG_ID), map.get(Constant.STRING_F20), map.get(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID), "", null , null, outResponseDto.getFinalResponseStatus(), outResponseDto.getFinalResonseText(), null, null,null);
	}
	
	public void getItemsForOutAMLQ(String action) {
		try {
		Map<String, Object> outputParam = fircoQueueDao.getItemsForOutAMLQ(action);
		
		if(outputParam.size()>0)
		invokeInAMLQProcWithParams(outputParam, Constant.UPDACK);
		
		}
		catch (Exception e) {
			log.error("Error while updating data of ACK Queue "+e.getMessage());
		}
		// for updateack
	}
	
	private Map<String, Map<String, String>> invokeInAMLQProcWithParams(Map<String, Object> outputParam, String action) {
		String msgTypeId = null;
		String sendInstRef = null;
		String strF20 = null;
		String strFile = null;
		String msgId = null;
		String msgIdMq = null;
		Map<String, String> procResponse =null;
		Map<String, Map<String, String>> messageIdsMapSet = new HashMap<>();
		for(Entry<String, Object> key: outputParam.entrySet()) {
			List<Map<String, Object>> outputList =(List<Map<String, Object>>) key.getValue();
			for (Map<String, Object> output : outputList) {
				msgTypeId = String.valueOf( output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID));
				sendInstRef = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_SEND_INST_REF));
				strFile = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_FILENAMES));
				msgId = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_ID));
				if(action.equals(Constant.INSERT)) {
					insertOrUpdateBySPInAMLQ(action, msgId, sendInstRef, msgTypeId, "",null , null, null, null, strFile, null,null);
				}
				if(action.equals(Constant.INSERT_ACK_ACTION)) {
					insertOrUpdateBySPInAMLQ(action, msgId, sendInstRef, msgTypeId, "",null , null, null, null, strFile, null,null);
				}
				else if(action.equals(Constant.UPDACK)) {
					msgTypeId = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID_STRING));
//					strFile = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_FILENAMES_STRING));
					msgId = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_ID_STRING));
					strF20 = String.valueOf(output.get(Constant.STRING_F20_STRING)).trim();
//					msgIdMq = String.valueOf(output.get(Constant.MSGID_MQ_STRING));
					String initial_response_text = String.valueOf(output.get(Constant.ACK_RESPONSE));
					String initial_response_status = String.valueOf(output.get(Constant.ACK_RESPONSE_STATUS));
					insertOrUpdateBySPInAMLQ(action, msgId, strF20, msgTypeId, "",initial_response_status , initial_response_text, null, null, null,null,null);
				}
				else {

					msgTypeId = String.valueOf( output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID_STRING));
					strFile = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_FILENAMES_STRING));
					msgId = String.valueOf(output.get(Constant.FIRCO_OUTPUT_PARAM_MSG_ID_STRING));
					strF20 = String.valueOf(output.get(Constant.STRING_F20_STRING));
					msgIdMq = String.valueOf(output.get(Constant.MSGID_MQ_STRING));
					procResponse = new HashMap<>();
					procResponse.put(Constant.FIRCO_OUTPUT_PARAM_MSG_TYPE_ID, msgTypeId);
					procResponse.put(Constant.FIRCO_OUTPUT_PARAM_FILENAMES, strFile);
					procResponse.put(Constant.FIRCO_OUTPUT_PARAM_MSG_ID, msgId);
					procResponse.put(Constant.STRING_F20, strF20);
					procResponse.put(Constant.MSGID_MQ, msgIdMq);
					messageIdsMapSet.put(msgId, procResponse);
				}
			}
		}
		return messageIdsMapSet;
	}

}
