package local.tin.tests.rabbitmq.base.exchanges;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessage;

/**
 *
 * @author benitodarder
 */
public class Producer {
    
    private Producer() {
    }
    
    public static Producer getInstance() {
        return ProducerHolder.INSTANCE;
    }
    
    private static class ProducerHolder {

        private static final Producer INSTANCE = new Producer();
    }
    
    public void send(RabbitMQMessage rabbitMQMessage) throws RabbitMQException {
        try {
            Channel channel = rabbitMQMessage.getChannel();
            channel.basicPublish("", rabbitMQMessage.getRabbitMQConfigMessage().getQueueName(), rabbitMQMessage.getMessageProperties(), rabbitMQMessage.getPayload().getBytes(rabbitMQMessage.getCharset()));
        } catch (IOException ex) {
            throw new RabbitMQException(ex);
        }
    }
            
}
