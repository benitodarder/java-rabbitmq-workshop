package local.tin.tests.rabbitmq.base.exchanges;

import java.io.IOException;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageConsume;

/**
 *
 * @author benitodarder
 */
public class Consumer {
    
    private Consumer() {
    }
    
    public static Consumer getInstance() {
        return ConsumerHolder.INSTANCE;
    }
    
    private static class ConsumerHolder {

        private static final Consumer INSTANCE = new Consumer();
    }
    
    /**
     * Consumes the given queue with the given DeliverCallback and CancelCallback.
     * 
     * If no channel is given, a new one is created with available configuration.
     * 
     * @param rabbitMQMessageConsume as RabbitMQMessageConsume
     * @return consumer tag
     * @throws RabbitMQException 
     */
    public String consume(RabbitMQMessageConsume rabbitMQMessageConsume) throws RabbitMQException {
        String consumerTag = null;
        try {
            if (rabbitMQMessageConsume.getChannel() == null) {
                rabbitMQMessageConsume.setChannel(ChannelFactory.getInstance().getChannel(rabbitMQMessageConsume.getRabbitMQConfigMessage()));
            }
            consumerTag = rabbitMQMessageConsume.getChannel().basicConsume(rabbitMQMessageConsume.getRabbitMQConfigMessage().getQueueName(), rabbitMQMessageConsume.isAutoAcknowledge(), rabbitMQMessageConsume.getDeliveryCallback(), rabbitMQMessageConsume.getCancelCallback());
        } catch (IOException ex) {
            throw new RabbitMQException(ex);
        }
        return consumerTag;
    }
}
