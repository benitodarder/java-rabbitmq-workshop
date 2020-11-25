package local.tin.tests.simple.jms.consumer;

/**
 *
 * @author benitodarder
 */
import com.rabbitmq.jms.admin.RMQConnectionFactory;
import javax.jms.*;
import org.apache.log4j.Logger;

public class SimpleQueueReceiver {

    private static final Logger LOGGER = Logger.getLogger(SimpleQueueReceiver.class);
    public static final int RABBITMQ_PORT = 5672;
    public static final String RABBITMQ_HOST = "192.168.56.19";
    public static final String RABBITMQ_VIRTUAL_HOST = "/";
    public static final String PASWORD = "guest";
    public static final String USER_NAME = "guest";
    public static final String QUEUE_NAME = "sample_jms_queue";  
    
    public static void main(String[] args) {

        QueueConnectionFactory queueConnectionFactory = (QueueConnectionFactory) jmsConnectionFactory();
        QueueConnection queueConnection = null;
        QueueSession queueSession = null;
        try {
            queueConnection = queueConnectionFactory.createQueueConnection();
            queueSession = queueConnection.createQueueSession(false, Session.AUTO_ACKNOWLEDGE);
            Queue queue = queueSession.createQueue(QUEUE_NAME);
            QueueReceiver queueReceiver = queueSession.createReceiver(queue);
            queueConnection.start();
            while (true) {
                Message m = queueReceiver.receive(1);
                if (m != null) {
                    if (m instanceof TextMessage) {
                        LOGGER.info("Reading message: "  + ((TextMessage)m).getText());
                    } else {
                        LOGGER.info("Non text message received... Exiting.");
                        break;
                    }
                } 
            }
        } catch (JMSException e) {
            LOGGER.error("Exception occurred: ", e);
        } finally {
            try {
                if (queueSession != null) {
                    queueSession.close();
                }
                if (queueConnection != null) {

                    queueConnection.close();
                }
            } catch (JMSException e) {
                LOGGER.error("Pfff... Whatever... Exception occurred: ", e);
            }
        }
                

    }
    
    private static ConnectionFactory jmsConnectionFactory() {
        RMQConnectionFactory connectionFactory = new RMQConnectionFactory();
        connectionFactory.setUsername(USER_NAME);
        connectionFactory.setPassword(PASWORD);
        connectionFactory.setVirtualHost(RABBITMQ_VIRTUAL_HOST);
        connectionFactory.setHost(RABBITMQ_HOST);
        connectionFactory.setPort(RABBITMQ_PORT);
        return connectionFactory;
    }    
}
