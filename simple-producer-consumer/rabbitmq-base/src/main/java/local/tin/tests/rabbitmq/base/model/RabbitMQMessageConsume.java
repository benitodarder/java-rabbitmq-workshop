package local.tin.tests.rabbitmq.base.model;


import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.DeliverCallback;

/**
 *
 * @author benitodarder
 */
public class RabbitMQMessageConsume extends RabbitMQMessage {
    
    private DeliverCallback deliveryCallback;
    private CancelCallback cancelCallback;
    private String consumerTag;
    private boolean autoAcknowledge;

    public DeliverCallback getDeliveryCallback() {
        return deliveryCallback;
    }

    public void setDeliveryCallback(DeliverCallback deliveryCallback) {
        this.deliveryCallback = deliveryCallback;
    }

    public String getConsumerTag() {
        return consumerTag;
    }

    public void setConsumerTag(String consumerTag) {
        this.consumerTag = consumerTag;
    }

    public boolean isAutoAcknowledge() {
        return autoAcknowledge;
    }

    public void setAutoAcknowledge(boolean autoAcknowledge) {
        this.autoAcknowledge = autoAcknowledge;
    }

    public CancelCallback getCancelCallback() {
        return cancelCallback;
    }

    public void setCancelCallback(CancelCallback cancelCallback) {
        this.cancelCallback = cancelCallback;
    }
    
    
}
