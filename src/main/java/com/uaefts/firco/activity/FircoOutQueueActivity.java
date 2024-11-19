package com.uaefts.firco.activity;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.uaefts.firco.config.ApplicationContext;
import com.uaefts.firco.constants.Constant;
import com.uaefts.firco.mq.FircoOUTMQListerner;
import com.uaefts.firco.servie.FircoQueueService;

@Component
public class FircoOutQueueActivity implements Activity {

	private static final Logger log = LoggerFactory.getLogger(FircoOutQueueActivity.class);

	@Autowired
	FircoOUTMQListerner fircoOutmqListerner;

	@Autowired
	FircoQueueService fircoQueueService;

	@Override
	public Outcome execute(ApplicationContext context) throws Exception {
		log.info("FircoOutQueueActivity started");

		try {
			
			//Get Record through SP_GET_ITEMS_FOR_INAMLQ For Out Queue Processing Action - GETFINMSGS
			Map<String, Map<String, String>> messageIdsList = fircoQueueService.getItemsForInAMLQ(Constant.GET_FIN_MSGS);
			
			//Retrieved data from Firco Out Queue and update through SP_GET_ITEMS_FOR_INAMLQ - Action - UPDATEOUT 
			fircoOutmqListerner.fircoMessageReceiver(messageIdsList);
			
			log.info("FircoOutQueueActivity Completed");
			return Outcome.SUCCESS;
		}
		catch (Exception e) {
			log.error("FircoOutQueueActivity Processed Failed "+e.getMessage());
			return Outcome.FAILURE;
		}

	}

	@Override
	public String getName() {
		return "FircoOutQueueActivity";
	}

}

