package local.tin.tests.rabbitmq.base.model;


import com.rabbitmq.client.AMQP.BasicProperties;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.MessageProperties;
import java.nio.charset.StandardCharsets;

/**
 *
 * @author benitodarder
 */
public class RabbitMQMessage {
    
    private RabbitMQConfigMessage rabbitMQConfigMessage;
    private Channel channel;
    private BasicProperties messageProperties;
    private String payload;
    private String charset;
    
    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public BasicProperties getMessageProperties() {
        return messageProperties;
    }

    public void setMessageProperties(BasicProperties messageProperties) {
        this.messageProperties = messageProperties;
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
