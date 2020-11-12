package local.tin.tests.rabbitmq.base.exchanges;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigSender;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageSend;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;

/**
 *
 * @author benitodarder
 */
public class ProducerTest {
    
    private static final String QUEUE_NAME = "queueueueue";
    private static final String CHARSET = "UTF-8";
    private static final String PAYLOAD = "Payload wayne...";
    private Connection mockedConnection;
    private Channel mockedChannel;
    private RabbitMQConfigMessage rabbitMQConfigMessage;
    private RabbitMQMessageSend rabbitMQMessage;
    
    
    @Before
    public void setUp() {
        mockedChannel = mock(Channel.class);
        mockedConnection = mock(Connection.class);
    }
    
    @Test
    public void send_uses_basicPublish_with_given_data() throws RabbitMQException, UnsupportedEncodingException, IOException {
        rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        rabbitMQMessage = new RabbitMQMessageSend();
        rabbitMQMessage.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessage.setChannel(mockedChannel);
        rabbitMQMessage.setCharset(CHARSET);
        rabbitMQMessage.setPayload(PAYLOAD);
        
        Producer.getInstance().send(rabbitMQMessage);
        
        verify(mockedChannel).basicPublish("", QUEUE_NAME, null, PAYLOAD.getBytes(CHARSET));
    }
    
    @Test
    public void send_closes_connection_when_told() throws RabbitMQException, UnsupportedEncodingException, IOException {
        rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setConnection(mockedConnection);
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        rabbitMQMessage = new RabbitMQMessageSend();
        rabbitMQMessage.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessage.setChannel(mockedChannel);
        rabbitMQMessage.setCharset(CHARSET);
        rabbitMQMessage.setPayload(PAYLOAD);
        rabbitMQMessage.setCloseConnection(true);
        
        Producer.getInstance().send(rabbitMQMessage);
        
        verify(mockedConnection).close();
    }    
}
