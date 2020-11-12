package local.tin.tests.rabbitmq.simple.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import local.tin.tests.rabbitmq.base.callbacks.EmptyCancelCallback;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import local.tin.tests.rabbitmq.base.factories.ConnectionsFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigConnectionFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigSender;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageConsume;
import local.tin.tests.rabbitmq.simple.consumer.callbacks.SimpleDeliverCallback;

/**
 *
 * @author benitodarder
 */
public class Consumer {

    public static final String HOST = "192.168.56.19";
    public static final String QUEUE_NAME = "hello";
    public static final String CHARSET = "UTF-8";    
    public static final boolean AUTO_ACKNOWLEDGE = false;
    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws RabbitMQException {
       RabbitMQConfigConnectionFactory rabbitMQConfigConnectionFactory = new RabbitMQConfigConnectionFactory();
        rabbitMQConfigConnectionFactory.setHost(HOST);
        Connection connection = ConnectionsFactory.getInstance().getConnection(rabbitMQConfigConnectionFactory);
        RabbitMQConfigSender rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setConnection(connection);
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        Channel channel = ChannelFactory.getInstance().getChannel(rabbitMQConfigMessage);
        RabbitMQMessageConsume rabbitMQMessage = new RabbitMQMessageConsume();
        rabbitMQMessage.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessage.setChannel(channel);
        rabbitMQMessage.setCharset(CHARSET);
        SimpleDeliverCallback simpleDeliverCallback = new SimpleDeliverCallback(CHARSET);
        rabbitMQMessage.setDeliveryCallback(simpleDeliverCallback);
        EmptyCancelCallback emptyCancelCallback = new EmptyCancelCallback();
        rabbitMQMessage.setCancelCallback(emptyCancelCallback);
        rabbitMQMessage.setAutoAcknowledge(AUTO_ACKNOWLEDGE);
        local.tin.tests.rabbitmq.base.exchanges.Consumer.getInstance().consume(rabbitMQMessage);
    }
    
}
