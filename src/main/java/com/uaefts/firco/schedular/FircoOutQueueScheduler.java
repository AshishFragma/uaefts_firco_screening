package com.uaefts.firco.schedular;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.uaefts.firco.activity.FircoOutQueueActivity;
import com.uaefts.firco.activity.Outcome;
import com.uaefts.firco.config.ApplicationContext;
import com.uaefts.firco.config.TransactionContext;

@RestController
@RequestMapping("/api/")
public class FircoOutQueueScheduler {
	
	private static final Logger log = LoggerFactory.getLogger(FircoOutQueueScheduler.class);
	
	@Autowired
	FircoOutQueueActivity fircoOutQueueActivity;


	//private final OrchestrationLayer fircoOutQueueOrchestrator;

	//public FircoOutQueueScheduler(@Qualifier("fircoOutQueueOrchestrator") OrchestrationLayer fircoOutQueueOrchestrator) {
		//this.fircoOutQueueOrchestrator = fircoOutQueueOrchestrator;
	//}

	//@Scheduled(fixedDelay = 10000) // will implement listener in this
    //@SchedulerLock(name = "FIRCO_OUT_QUEUE", lockAtMostFor = "10m", lockAtLeastFor = "5m")
	@GetMapping("/out")
	public void pollFircoOutQueue() throws Exception {
		ApplicationContext context = new ApplicationContext();
		TransactionContext transactionContext=new TransactionContext(); 
		context.setTransactionContext(transactionContext);
		Outcome outcome = fircoOutQueueActivity.execute(context);
		if (outcome == Outcome.SUCCESS) {
			log.info(" Poll Firco Out Activity Done");
			System.out.println("SUCCESS");
		} else {
			log.error(" Poll Firco Out Activity Failed");
			System.out.println("FAILED");
		}

	}
}
