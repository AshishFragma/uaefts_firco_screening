package com.uaefts.activity;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import com.uaefts.constant.Constant;
import com.uaefts.context.ApplicationContext;
import com.uaefts.context.FileContentInsertActivityContext;
import com.uaefts.dao.FileProcessingDao;
import com.uaefts.service.TransactionService;

@RunWith(MockitoJUnitRunner.class)
public class FileContentInsertActivityTest {

 /*   @Mock
    private FileProcessingDao fileProcessingDao;

    @Mock
    private TransactionService transactionService;

    @InjectMocks
    private FileContentInsertActivity fileContentInsertActivity;

    private ApplicationContext context;

    @Before
    public void setUp() {
        context = new ApplicationContext();
        context.setTransactionContext(new com.uaefts.context.TransactionContext());
    }

    @Test
    public void testExecuteSuccessForMsgType103() throws Exception {
        // Set up the context
        String payload = "CTD|data1\nCTD|data2\nCTC|dataC";
        context.getTransactionContext().setPayload(payload);
        context.getTransactionContext().setMsgType("103");
        context.getTransactionContext().setFileLocation("testFileLocation");
        context.getTransactionContext().setTransactionId(1l);
        context.getTransactionContext().setFileName("E026033C1021022409250000076.FTR");

        // Mock the behavior of transactionService
        doNothing().when(transactionService).updateTransactionContext(eq(Constant.SUCESS), any(), any(), any(), eq(context), any());
        doNothing().when(fileProcessingDao).insertDataIntoDb(any(), any(), any(), any());

        // Execute the method
        Outcome outcome = fileContentInsertActivity.execute(context);

        // Verify the outcomes
        assertEquals(Outcome.SUCCESS, outcome);
        verify(transactionService, times(1)).updateTransactionContext(eq(Constant.SUCESS), eq(Constant.FILE_CONTENET_INSERT_MSG), eq("testFileLocation"), eq(payload), eq(context), any());
        verify(fileProcessingDao, times(1)).insertDataIntoDb(eq("CTD|data1"), eq("CTC|dataC"), eq(12345), eq("E026033C1021022409250000076.FTR"));
    }

    @Test
    public void testExecuteSuccessForMsgType102() throws Exception {
        // Set up the context
        String payload = "CTD|data1\nCTD|data2\nCTC|dataC";
        context.getTransactionContext().setPayload(payload);
        context.getTransactionContext().setMsgType("102");
        context.getTransactionContext().setFileLocation("testFileLocation");
        context.getTransactionContext().setTransactionId(1l);
        context.getTransactionContext().setFileName("E026033C1021022409250000076.FTR");

        // Mock the behavior of transactionService
        doNothing().when(transactionService).updateTransactionContext(eq(Constant.SUCESS), any(), any(), any(), eq(context), any());
        doNothing().when(fileProcessingDao).insertDataIntoDb(any(), any(), any(), any());

        // Execute the method
        Outcome outcome = fileContentInsertActivity.execute(context);

        // Verify the outcomes
        assertEquals(Outcome.SUCCESS, outcome);
        verify(transactionService, times(1)).updateTransactionContext(eq(Constant.SUCESS), eq(Constant.FILE_CONTENET_INSERT_MSG), eq("testFileLocation"), eq(payload), eq(context), any());
        verify(fileProcessingDao, times(2)).insertDataIntoDb(eq("CTD|data1"), eq("CTC|dataC"), eq(1), eq("E026033C1021022409250000076.FTR"));
        verify(fileProcessingDao, times(1)).insertDataIntoDb(eq("CTD|data2"), eq("CTC|dataC"), eq(1), eq("E026033C1021022409250000076.FTR"));
    }

    @Test
    public void testExecuteFailureDueToInvalidFile() throws Exception {
        // Set up the context with invalid payload (missing CTC)
        String payload = "CTD|data1\nCTD|data2";
        context.getTransactionContext().setPayload(payload);
        context.getTransactionContext().setMsgType("103");
        context.getTransactionContext().setFileLocation("testFileLocation");
        context.getTransactionContext().setTransactionId(1l);
        context.getTransactionContext().setFileName("E026033C1021022409250000076.FTR");

        // Execute the method
        Outcome outcome = fileContentInsertActivity.execute(context);

        // Verify the outcomes
        assertEquals(Outcome.RETRY, outcome);
        verify(transactionService, times(1)).updateTransactionContext(eq(Constant.RETRY), eq("Exception in FileContentInsertActivity"), eq("testFileLocation"), eq(payload), eq(context), any());
        assertEquals("Invalid FTR file: Missing CTD or CTC section", context.getTransactionContext().getErrorMsg());
    }

    @Test
    public void testExecuteFailureDueToException() throws Exception {
        // Set up the context
        String payload = "CTD|data1\nCTD|data2\nCTC|dataC";
        context.getTransactionContext().setPayload(payload);
        context.getTransactionContext().setMsgType("999"); // Unsupported message type
        context.getTransactionContext().setFileLocation("testFileLocation");
        context.getTransactionContext().setTransactionId(1l);
        context.getTransactionContext().setFileName("E026033C1021022409250000076.FTR");

        // Execute the method
        Outcome outcome = fileContentInsertActivity.execute(context);

        // Verify the outcomes
        assertEquals(Outcome.RETRY, outcome);
        verify(transactionService, times(1)).updateTransactionContext(eq(Constant.RETRY), eq("Unsupported message type: 999"), eq("testFileLocation"), eq(payload), eq(context), any());
        assertEquals("Unsupported message type: 999", context.getTransactionContext().getErrorMsg());
    }
    
    */
}

