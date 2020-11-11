package local.tin.tests.rabbitmq.base.factories;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigConnectionFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;

/**
 *
 * @author benitodarder
 */
public class ConnectionsFactory {
    
    private ConnectionsFactory() {
    }
    
    public static ConnectionsFactory getInstance() {
        return ConnectionsFactoryHolder.INSTANCE;
    }
    
    private static class ConnectionsFactoryHolder {

        private static final ConnectionsFactory INSTANCE = new ConnectionsFactory();
    }
    
    public Connection getConnection(RabbitMQConfigConnectionFactory rabbitMQConfigConnectionFactory) throws RabbitMQException {
        try {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(rabbitMQConfigConnectionFactory.getHost());
            return connectionFactory.newConnection();
        } catch (IOException | TimeoutException ex) {
            throw new RabbitMQException(ex);
        }
    }
}
