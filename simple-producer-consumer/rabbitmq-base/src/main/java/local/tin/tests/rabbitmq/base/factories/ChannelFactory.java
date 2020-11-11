package local.tin.tests.rabbitmq.base.factories;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigReceiver;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigSender;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;

/**
 *
 * @author benitodarder
 */
public class ChannelFactory {

    private ChannelFactory() {
    }

    public synchronized static ChannelFactory getInstance() throws RabbitMQException {

        return ChannelFactoryHolder.INSTANCE;
    }

    private static class ChannelFactoryHolder {

        private static final ChannelFactory INSTANCE = new ChannelFactory();
    }

    /**
     * Returns the channel corresponding to the configuration.
     * 
     * If configuration is null a new connection is created. Requires host name.
     * 
     * @param rabbitMQConfig extends RabbitMQConfigMessage
     * @return Channel 
     * @throws RabbitMQException 
     */
    public Channel getChannel(RabbitMQConfigMessage rabbitMQConfig) throws RabbitMQException {
        Channel channel = null;
        try {
            if (rabbitMQConfig.getConnection() != null) {
            channel = rabbitMQConfig.getConnection().createChannel();
            } else {
                channel = ConnectionsFactory.getInstance().getConnection(rabbitMQConfig).createChannel();
            }
            if (rabbitMQConfig instanceof RabbitMQConfigSender) {
                RabbitMQConfigSender rabbitMQConfigSender = (RabbitMQConfigSender) rabbitMQConfig;
                if (rabbitMQConfig instanceof RabbitMQConfigReceiver) {
                    RabbitMQConfigReceiver rabbitMQConfigReceiver = (RabbitMQConfigReceiver) rabbitMQConfig;
                    channel.basicQos(rabbitMQConfigReceiver.getBasicQoS());
                }
                channel.queueDeclare(rabbitMQConfigSender.getQueueName(), rabbitMQConfigSender.isDurable(), false, false, null);
            } else {
                throw new RabbitMQException("Unexpected RabbitMQConfig:" + rabbitMQConfig.getClass().getSimpleName());
            }
        } catch (IOException ioe) {
            throw new RabbitMQException(ioe);
        }
        return channel;
    }
}
