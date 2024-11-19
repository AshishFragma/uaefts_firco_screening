package com.uaefts.firco.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.jms.core.JmsTemplate;

import com.ibm.mq.jakarta.jms.MQQueueConnectionFactory;

import jakarta.jms.ConnectionFactory;
import jakarta.jms.JMSException;


@Configuration
@EnableJms
public class MQConfig {
	
	private static final Logger log = LoggerFactory.getLogger(MQConfig.class);


    @Value("${firco_ibm_mq_qm}")
    private String firco_ibm_mq_qm;

    @Value("${firco_ibm_mq_channel}")
    private String firco_ibm_mq_channel;

    @Value("${firco_ibm_mq_connname}")
    private String firco_ibm_mq_connname;

    @Value("${firco_ibm_mq_username}")
    private String firco_ibm_mq_username;

    @Value("${firco_ibm_mq_password}")
    private String firco_ibm_mq_password;

    @Value("${firco_ibm_mq_host}")
    private String firco_ibm_mq_host;
    
    @Value("${firco_ibm_mq_port}")
    private int firco_ibm_mq_port;
    
    @Value("$(firco_ibm.queueManagerNames)")
    private String[] queueManagerNames;
    

    @Bean
    public ConnectionFactory connectionFactory() throws JMSException {
        log.info("-------------FIRCOQUEUE CONNECTION FACTORY CONFIG--------------");

        MQQueueConnectionFactory factory = new MQQueueConnectionFactory();
        factory.setQueueManager(firco_ibm_mq_qm);
        factory.setChannel(firco_ibm_mq_channel);
        factory.setConnectionNameList(firco_ibm_mq_connname);
        factory.setTransportType(1); // 1 = Client
       // factory.setStringProperty("UserID", firco_ibm_mq_username);
       // factory.setStringProperty("Password", firco_ibm_mq_password);
        log.info("-------------FIRCOQUEUE CONNECTION FACTORY--------------");
        return factory;
    }

    @Bean
    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory){
        log.info("-------------FIRCOQUEUE JMS TEMPLATE CONFIG--------------");
        JmsTemplate jmsTemplate = new JmsTemplate();
        jmsTemplate.setConnectionFactory(connectionFactory);
        log.info("-------------FIRCOQUEUE JMS TEMPLATE--------------");
        log.info("FIRCOQUEUE jmsTemplate: {}",jmsTemplate);
        return jmsTemplate;
    }
    
    /*
    @Bean
    public MQQueueManager mqQueueManager() throws MQException {
    	Map<String,MQQueueManager> queueManagers=new HashMap<>();
    	MQEnvironment.hostname=firco_ibm_mq_host;
    	MQEnvironment.port=firco_ibm_mq_port;
    	MQEnvironment.channel=firco_ibm_mq_channel;
    	
    	//create an MQQueueManager for each queue manager name
    	
    	return new MQQueueManager(firco_ibm_mq_qm);
    	
    	
    	
    }*/
    
}
