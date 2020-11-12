package local.tin.tests.rabbitmq.base.exchanges;

import java.io.IOException;
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
    
    public void consume(RabbitMQMessageConsume rabbitMQMessageConsume) throws RabbitMQException {
        try {
            rabbitMQMessageConsume.getChannel().basicConsume(rabbitMQMessageConsume.getRabbitMQConfigMessage().getQueueName(), rabbitMQMessageConsume.isAutoAcknowledge(), rabbitMQMessageConsume.getDeliveryCallback(), rabbitMQMessageConsume.getCancelCallback());
        } catch (IOException ex) {
            throw new RabbitMQException(ex);
        }
    }
}
