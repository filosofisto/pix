//package gov.bacen.pix.config;
//
//import com.ibm.mq.jms.MQConnectionFactory;
//import com.ibm.msg.client.wmq.WMQConstants;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.jms.annotation.EnableJms;
////import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
//
//import javax.jms.ConnectionFactory;
//import javax.jms.JMSException;
//import javax.jms.Session;
//
//@Configuration
//@EnableJms
//public class MqJmsConfig {
//
////    @Bean
////    public ConnectionFactory mqConnectionFactory(IbmMqProperties props) throws Exception {
////        com.ibm.mq.jakarta.jms.MQConnectionFactory cf = new com.ibm.mq.jakarta.jms.MQConnectionFactory();
////        cf.setQueueManager(props.queueManager());
////        cf.setChannel(props.channel());
////        cf.setConnectionNameList(props.connName());
////        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
////        cf.setStringProperty(WMQConstants.USERID, props.user());
////        cf.setStringProperty(WMQConstants.PASSWORD, props.password());
////
////        return cf;
////    }
//
//    @Bean
//    public ConnectionFactory mqConnectionFactory(IbmMqProperties props) throws JMSException {
//        MQConnectionFactory cf = new MQConnectionFactory();
//        cf.setQueueManager(props.queueManager());
//        cf.setChannel(props.channel());
//        cf.setConnectionNameList(props.connName());
//        cf.setTransportType(WMQConstants.WMQ_CM_CLIENT);
//        cf.setStringProperty(WMQConstants.USERID, props.user());
//        cf.setStringProperty(WMQConstants.PASSWORD, props.password());
//
//        return cf;
//    }
//
////    @Bean
////    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
////            ConnectionFactory connectionFactory, IbmMqProperties props) {
////        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
////        factory.setConnectionFactory(connectionFactory);
////        factory.setConcurrency("1-5");
////        factory.setSessionTransacted(true);
////
////        return factory;
////    }
//
//    @Bean
//    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory(
//            javax.jms.ConnectionFactory connectionFactory) {
//
//        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
//        factory.setConnectionFactory(connectionFactory);
//        factory.setConcurrency("1-5");
//        factory.setSessionTransacted(true);
//        factory.setSessionAcknowledgeMode(Session.SESSION_TRANSACTED);
//        factory.setPubSubDomain(false); // queue, not topic
//        factory.setErrorHandler(t -> {
//            System.err.println("MQ Listener error: " + t.getMessage());
//            t.printStackTrace();
//        });
//
//        return factory;
//    }
//}
//
