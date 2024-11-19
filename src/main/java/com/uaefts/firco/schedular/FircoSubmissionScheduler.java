package com.uaefts.firco.schedular;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uaefts.firco.activity.FircoSubmissionActivity;
import com.uaefts.firco.activity.Outcome;
import com.uaefts.firco.config.ApplicationContext;
import com.uaefts.firco.config.TransactionContext;

@RestController
@RequestMapping("/api/")
public class FircoSubmissionScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(FircoSubmissionScheduler.class);

	
	@Autowired
	FircoSubmissionActivity fircoSubmissionActivity;

	//private final OrchestrationLayer fircoSubmissionOrchestrator;

	//public FircoSubmissionScheduler(@Qualifier("fircoSubmissionOrchestrator") OrchestrationLayer fircoSubmissionOrchestrator) {
		//this.fircoSubmissionOrchestrator = fircoSubmissionOrchestrator;
	//}

	//@Scheduled(fixedDelay = 10000) // will implement listener in this
    //@SchedulerLock(name = "FIRCO_PUSH_QUEUE", lockAtMostFor = "5m", lockAtLeastFor = "1m")
	@GetMapping("/push")
	public void pushDataInFirco() throws Exception {
		ApplicationContext context = new ApplicationContext();
		TransactionContext transactionContext=new TransactionContext(); 
		context.setTransactionContext(transactionContext);
		//Outcome outcome = fircoSubmissionOrchestrator.execute(context,null);
		Outcome outcome=fircoSubmissionActivity.execute(context);
		if (outcome == Outcome.SUCCESS) {
			log.info(" Poll Firco Submission Activity Done");
			System.out.println("SUCCESS");
		} else {
			log.error(" Poll Firco Submission Activity Failed");
			System.out.println("FAILED");
		}

	}
}
