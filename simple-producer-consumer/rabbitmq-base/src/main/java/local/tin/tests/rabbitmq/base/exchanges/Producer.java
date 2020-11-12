package local.tin.tests.rabbitmq.base.exchanges;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageSend;
import org.apache.log4j.Logger;

/**
 *
 * @author benitodarder
 */
public class Producer {
    
    private static final Logger LOGGER = Logger.getLogger(Producer.class);
    
    private Producer() {
    }
    
    public static Producer getInstance() {
        return ProducerHolder.INSTANCE;
    }
    
    private static class ProducerHolder {

        private static final Producer INSTANCE = new Producer();
    }
    
    public void send(RabbitMQMessageSend rabbitMQMessage) throws RabbitMQException {
        try {
            Channel channel = rabbitMQMessage.getChannel();
            channel.basicPublish("", rabbitMQMessage.getRabbitMQConfigMessage().getQueueName(), rabbitMQMessage.getMessageProperties(), rabbitMQMessage.getPayload().getBytes(rabbitMQMessage.getCharset()));
        } catch (IOException ex) {
            throw new RabbitMQException(ex);
        } finally {
            if (rabbitMQMessage.isCloseConnection()) {
                try {
                    rabbitMQMessage.getRabbitMQConfigMessage().getConnection().close();
                } catch (IOException ex) {
                    LOGGER.error("Unexpected IOException closing the connection inside finally...", ex);
                }
            }
        }
    }
            
}
