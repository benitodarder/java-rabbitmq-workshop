package local.tin.tests.rabbitmq.base.model;


import com.rabbitmq.client.Channel;

/**
 *
 * @author benitodarder
 */
public class RabbitMQMessage {
    
    private RabbitMQConfigMessage rabbitMQConfigMessage;
    private Channel channel;
    private String payload;
    private String charset;
    
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getPayload() {
        return payload;
    }

    public void setPayload(String payload) {
        this.payload = payload;
    }

    public RabbitMQConfigMessage getRabbitMQConfigMessage() {
        return rabbitMQConfigMessage;
    }

    public void setRabbitMQConfigMessage(RabbitMQConfigMessage rabbitMQConfigMessage) {
        this.rabbitMQConfigMessage = rabbitMQConfigMessage;
    }

    public String getCharset() {
        return charset;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

}
