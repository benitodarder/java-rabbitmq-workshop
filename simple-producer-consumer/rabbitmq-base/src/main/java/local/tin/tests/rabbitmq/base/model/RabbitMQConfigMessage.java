package local.tin.tests.rabbitmq.base.model;

import com.rabbitmq.client.Connection;

/**
 *
 * @author benitodarder
 */
public class RabbitMQConfigMessage extends RabbitMQConfigConnectionFactory {
    
    private Connection connection;
    private String queueName;

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String name) {
        this.queueName = name;
    }

    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }

}
