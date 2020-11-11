package local.tin.tests.rabbitmq.base.model;

/**
 *
 * @author benitodarder
 */
public class RabbitMQConfigSender extends RabbitMQConfigMessage {
    
    private boolean durable;


    public boolean isDurable() {
        return durable;
    }

    public void setDurable(boolean durable) {
        this.durable = durable;
    }
    
}
