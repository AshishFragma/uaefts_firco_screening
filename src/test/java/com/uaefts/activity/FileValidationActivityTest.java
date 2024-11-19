package com.uaefts.activity;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Value;

import com.uaefts.activity.FileValidationActivity;
import com.uaefts.activity.Outcome;
import com.uaefts.context.ApplicationContext;
import com.uaefts.context.FileValidationActivityContext;
import com.uaefts.context.TransactionContext;
import com.uaefts.dao.FileProcessingDao;
import com.uaefts.repository.CBInputContentRepository;
import com.uaefts.repository.TransactionRepository1;
import com.uaefts.service.EmailService;
import com.uaefts.service.TransactionService;

@RunWith(MockitoJUnitRunner.class)
public class FileValidationActivityTest {
/*
	@Value("${file.incoming.directory}")
	private String incomingDirectory;

	@Value("${file.processing.folder}")
	private String processingFolder;

	@Value("${file.processed.folder}")
	private String processedFolder;

	@Value("${file.exception.folder}")
	private String exceptionFolder;

	@Value("${file.ftrexception.folder}")
	private String ftrExceptionFolder;

	@Value("${file.duplicate.folder}")
	private String duplicateFolder;
	
    @Mock
    private TransactionRepository1 transactionRepository;

    @Mock
    private CBInputContentRepository cbInputRepository;

    @Mock
    private TransactionService transactionService;

    @Mock
    private FileProcessingDao fileProcessingDao;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private FileValidationActivity fileValidationActivity;

    private ApplicationContext context;
    private File ftrFile;
    private File shaFile;

    @Before
    public void setUp() {
        // Initialize context, files, and set properties
        context = new ApplicationContext();
        TransactionContext transactionContext=new TransactionContext();
        transactionContext.setMsgType("102");
        context.setTransactionContext(transactionContext);
        FileValidationActivityContext fileValidationContext = new FileValidationActivityContext();
        ftrFile = mock(File.class);
        shaFile = mock(File.class);
        
        when(ftrFile.getName()).thenReturn("E026033C1021022409250000076");
        when(shaFile.getName()).thenReturn("E026033C1021022409250000076");

        fileValidationContext.setFtrFile(ftrFile);
        fileValidationContext.setShaFile(shaFile);
        context.setFileValidationActivityContext(fileValidationContext);
        
        fileValidationActivity=Mockito.spy(fileValidationActivity);
        
        duplicateFolder="/";
    }

    @Test
    public void testExecuteSuccess() throws Exception {
        // Mock the file names
        when(ftrFile.getName()).thenReturn("E026033C1021022409250000076.FTR");
        when(shaFile.getName()).thenReturn("E026033C1021022409250000076.FTR.SHA");

        TransactionContext transactionContext=new TransactionContext();
        transactionContext.setMsgType("102");
        context.setTransactionContext(transactionContext);
        // Mock file content validations and moves
        doNothing().when(transactionService).updateTransactionContext(anyString(), anyString(), anyString(), anyString(), eq(context), anyString());

        // Mock the validation to return true
        doReturn(true).when(fileValidationActivity).validateFTRContent(any(File.class), anyString(),  eq(context));

        // Call the method under test
        Outcome outcome = fileValidationActivity.execute(context);

        // Verify success outcome
        assertEquals(Outcome.SUCCESS, outcome);

        // Verify that transaction context updates were called
        verify(transactionService, times(1)).updateTransactionContext(eq("PENDING"), anyString(), anyString(), anyString(), eq(context), anyString());
        verify(transactionService, times(1)).updateTransactionContext(eq("SUCCESS"), anyString(), anyString(), anyString(), eq(context), anyString());
    }

    @Test
    public void testExecuteDuplicateFTR() throws Exception {
        // Mock duplicate message check
        context.getTransactionContext().setDuplicateFTRMsg(true);

        // Mock file move to duplicate directory
        doNothing().when(transactionService).updateTransactionContext(anyString(), anyString(), anyString(), anyString(), eq(context), anyString());
        doNothing().when(fileProcessingDao).findFTSMsgsFile(anyString(), anyString(), anyInt());

        Outcome outcome = fileValidationActivity.execute(context);

        // Verify DUPLICATE outcome
        assertEquals(Outcome.DUPLICATE, outcome);

        // Verify that transaction context updates were called
        verify(transactionService, times(1)).updateTransactionContext(eq("DUPLICATE"), anyString(), anyString(), anyString(), eq(context), anyString());
    }

    @Test
    public void testExecuteFTRValidationFailure() throws Exception {
        // Mock invalid FTR content
        when(ftrFile.getName()).thenReturn("E026033C1021022409250000076.FTR");
        when(shaFile.getName()).thenReturn("E026033C1021022409250000076.FTR.SHA");

        // Force FTR content validation to fail
        doReturn(false).when(fileValidationActivity).validateFTRContent(any(), anyString(),  eq(context));

        Outcome outcome = fileValidationActivity.execute(context);

        // Verify FAILURE outcome
        assertEquals(Outcome.FAILURE, outcome);

        // Verify transaction step update was made for failure
        verify(transactionService, times(1)).updateTransactionContext(eq("FAILED"), anyString(), anyString(), anyString(), eq(context), anyString());
    }

    @Test
    public void testExecuteWithException() throws Exception {
        // Mock an exception during execution
        when(ftrFile.getName()).thenReturn("E026033C1021022409250000076.FTR");
        when(shaFile.getName()).thenReturn("E026033C1021022409250000076.FTR.SHA");

        // Simulate exception in validation logic
        doThrow(new IOException("Test Exception")).when(fileValidationActivity).validateFTRContent(any(), anyString(), eq(context));

        Outcome outcome = fileValidationActivity.execute(context);

        // Verify FAILURE outcome due to exception
        assertEquals(Outcome.FAILURE, outcome);

        // Verify that exception handling and transaction updates were made
        verify(transactionService, times(1)).updateTransactionContext(eq("RETRY"), anyString(), anyString(), anyString(), eq(context), anyString());
    }
   
    @Test
    public void testHandleValidationFailure() throws Exception {
        // Setup the test case for validation failure scenario
        String msgType = "102";
        String currentDate = LocalDate.now().toString();
        String fileName = "testFile.FTR";

        // Mock the file and its name
        when(ftrFile.getName()).thenReturn(fileName);
        when(shaFile.getName()).thenReturn(fileName + ".SHA");

        // Invoke the private method handleValidationFailure
        doNothing().when(emailService).sendFailureNotification(anyString(), anyString(), eq(context));
        // Use Reflection to access the private method if necessary
        fileValidationActivity.handleValidationFailure(ftrFile, shaFile, fileName, msgType, currentDate, 12345L, context);

        // Verify email notification was sent and transaction context was updated
        verify(emailService, times(1)).sendFailureNotification(anyString(), anyString(), eq(context));
        verify(transactionService, times(1)).updateTransactionContext(eq("FAILED"), anyString(), anyString(), anyString(), eq(context), anyString());
    }
    */
}
