package com.uaefts.firco.config;

public class TransactionContext {

	private Long transactionRequestId;
	private Long transactionId;
	private Long transactionStepId;
	private String transactionStatus;
	private String transactionStepStatus;
	private String transactionRequestStatus;
	private String message;
	private String payload;
	private String fileLocation;
	private String fileName;
	private int retryCount;
	private String activityName;
	private String createDate;
	private String errorMsg;
	private boolean isDuplicateFTRMsg;
	private boolean isRetry;
	private String msgType;
	private String nextActivityPayload;
	
	
	
	

	public String getNextActivityPayload() {
		return nextActivityPayload;
	}

	public void setNextActivityPayload(String nextActivityPayload) {
		this.nextActivityPayload = nextActivityPayload;
	}

	public TransactionContext() {

	}
	
	public TransactionContext(Long transactionRequestId,String msgType, String fileLocation,boolean isDuplicateFTRMsg,String fileName) {
		this.transactionRequestId=transactionRequestId;
		this.msgType=msgType;
		this.fileLocation=fileLocation;
		this.isDuplicateFTRMsg=isDuplicateFTRMsg;
		this.fileName=fileName;
	}

	public TransactionContext(Long transactionId, Long transactionStepId, String transactionStatus,
			String transactionStepStatus, int retryCount) {
		this.transactionId = transactionId;
		this.transactionStepId = transactionStepId;
		this.transactionStatus = transactionStatus;
		this.transactionStepStatus = transactionStepStatus;
		this.retryCount = retryCount;
	}

	public TransactionContext(Long transactionId, Long transactionStepId, String transactionStatus,
			String transactionStepStatus, String message, String fileLocation, String payload, int retryCount,String msgType,boolean isDuplicateFTRMsg) {
		this.transactionId = transactionId;
		this.transactionStepId = transactionStepId;
		this.transactionStatus = transactionStatus;
		this.transactionStepStatus = transactionStepStatus;
		this.message = message;
		this.payload = payload;
		this.fileLocation = fileLocation;
		this.retryCount = retryCount;
		this.msgType=msgType;
		this.isDuplicateFTRMsg=isDuplicateFTRMsg;
	}
	

	public TransactionContext(Long transactionId, Long transactionStepId, Long transactionRequestId, String payload,String msgType,String fileName) {
		this.transactionId = transactionId;
		this.transactionStepId = transactionStepId;
		this.transactionRequestId=transactionRequestId;
		this.payload=payload;
		this.msgType=msgType;
		this.fileName=fileName;
		
	}

	public String getTransactionRequestStatus() {
		return transactionRequestStatus;
	}

	public void setTransactionRequestStatus(String transactionRequestStatus) {
		this.transactionRequestStatus = transactionRequestStatus;
	}

	public Long getTransactionRequestId() {
		return transactionRequestId;
	}

	public void setTransactionRequestId(Long transactionRequestId) {
		this.transactionRequestId = transactionRequestId;
	}

	public String getMsgType() {
		return msgType;
	}

	public void setMsgType(String msgType) {
		this.msgType = msgType;
	}

	public boolean isRetry() {
		return isRetry;
	}

	public void setRetry(boolean isRetry) {
		this.isRetry = isRetry;
	}

	public boolean isDuplicateFTRMsg() {
		return isDuplicateFTRMsg;
	}

	public void setDuplicateFTRMsg(boolean isDuplicateFTRMsg) {
		this.isDuplicateFTRMsg = isDuplicateFTRMsg;
	}

	public Long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getTransactionStepId() {
		return transactionStepId;
	}

	public void setTransactionStepId(Long transactionStepId) {
		this.transactionStepId = transactionStepId;
	}

	public String getTransactionStatus() {
		return transactionStatus;
	}

	public void setTransactionStatus(String transactionStatus) {
		this.transactionStatus = transactionStatus;
	}

	public String getTransactionStepStatus() {
		return transactionStepStatus;
	}

	public void setTransactionStepStatus(String transactionStepStatus) {
		this.transactionStepStatus = transactionStepStatus;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getPayload() {
		return payload;
	}

	public void setPayload(String payload) {
		this.payload = payload;
	}

	public String getFileLocation() {
		return fileLocation;
	}

	public void setFileLocation(String fileLocation) {
		this.fileLocation = fileLocation;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getRetryCount() {
		return retryCount;
	}

	public void setRetryCount(int retryCount) {
		this.retryCount = retryCount;
	}

	public String getActivityName() {
		return activityName;
	}

	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	public String getCreateDate() {
		return createDate;
	}

	public void setCreateDate(String createDate) {
		this.createDate = createDate;
	}

	public String getErrorMsg() {
		return errorMsg;
	}

	public void setErrorMsg(String errorMsg) {
		this.errorMsg = errorMsg;
	}

}
