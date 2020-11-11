package local.tin.tests.rabbitmq.simple.producer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import local.tin.tests.rabbitmq.base.exchanges.Producer;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import local.tin.tests.rabbitmq.base.factories.ConnectionsFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigConnectionFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigSender;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessage;

/**
 *
 * @author benitodarder
 */
public class Sender {

    public static final String HOST = "192.168.56.19";
    public static final String QUEUE_NAME = "hello";
    public static final String CHARSET = "UTF-8";
    
    /**
     * @param args the command line arguments
     * @throws local.tin.tests.rabbitmq.base.model.RabbitMQException
     */
    public static void main(String[] args) throws RabbitMQException, IOException {
        RabbitMQConfigConnectionFactory rabbitMQConfigConnectionFactory = new RabbitMQConfigConnectionFactory();
        rabbitMQConfigConnectionFactory.setHost(HOST);
        Connection connection = ConnectionsFactory.getInstance().getConnection(rabbitMQConfigConnectionFactory);
        RabbitMQConfigSender rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setConnection(connection);
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        Channel channel = ChannelFactory.getInstance().getChannel(rabbitMQConfigMessage);
        RabbitMQMessage rabbitMQMessage = new RabbitMQMessage();
        rabbitMQMessage.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessage.setChannel(channel);
        rabbitMQMessage.setCharset(CHARSET);
        if (args.length == 0) {
            rabbitMQMessage.setPayload("Hello world!!");
        } else {
            rabbitMQMessage.setPayload(args[0]);
        }
        Producer.getInstance().send(rabbitMQMessage);
        rabbitMQConfigMessage.getConnection().close();
    }
    
}
