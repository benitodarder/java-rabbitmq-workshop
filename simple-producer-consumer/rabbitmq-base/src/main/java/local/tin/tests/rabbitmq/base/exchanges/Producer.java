package local.tin.tests.rabbitmq.base.exchanges;

import com.rabbitmq.client.Channel;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
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
    
    /**
     * Sends the message payload to the given channel.
     * 
     * If channel is null, a new channel is created with included configuration 
     * details and added to the RabbitMQMessage.
     * 
     * @param rabbitMQMessage as RabbitMQMessageSend
     * @throws RabbitMQException 
     */
    public void send(RabbitMQMessageSend rabbitMQMessage) throws RabbitMQException {
        try {
            if (rabbitMQMessage.getChannel() == null) {
                rabbitMQMessage.setChannel(ChannelFactory.getInstance().getChannel(rabbitMQMessage.getRabbitMQConfigMessage()));
            }
            Channel channel = rabbitMQMessage.getChannel();
            channel.basicPublish("", rabbitMQMessage.getRabbitMQConfigMessage().getQueueName(), rabbitMQMessage.getMessageProperties(), rabbitMQMessage.getPayload().getBytes(rabbitMQMessage.getCharset()));
        } catch (IOException ex) {
            throw new RabbitMQException(ex);
        } finally {
            if (rabbitMQMessage.isCloseChannel()) {
                try {
                    rabbitMQMessage.getChannel().close();
                } catch (IOException | TimeoutException ex) {
                    LOGGER.error("Unexpected IOException/TimeoutException closing the channel inside finally...", ex);
                } 
            }            
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
