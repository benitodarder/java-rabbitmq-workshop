package local.tin.tests.rabbitmq.base.model;

/**
 *
 * @author benitodarder
 */
public class RabbitMQConfigReceiver extends RabbitMQConfigSender {

    private int basicQoS;
    private boolean autoAcknowledge;

    public int getBasicQoS() {
        return basicQoS;
    }

    public void setBasicQoS(int basicQoS) {
        this.basicQoS = basicQoS;
    }

    public boolean isAutoAcknowledge() {
        return autoAcknowledge;
    }

    public void setAutoAcknowledge(boolean autoAcknowledge) {
        this.autoAcknowledge = autoAcknowledge;
    }
    
    
}
