package local.tin.tests.rabbitmq.base.exchanges;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigSender;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageSend;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author benitodarder
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ChannelFactory.class})
public class ProducerTest {
    
    private static final String QUEUE_NAME = "queueueueue";
    private static final String CHARSET = "UTF-8";
    private static final String PAYLOAD = "Payload wayne...";
    private static ChannelFactory mockedChannelFactory;
    private Connection mockedConnection;
    private Channel mockedChannel;
    private RabbitMQConfigMessage rabbitMQConfigMessage;
    private RabbitMQMessageSend rabbitMQMessage;
    
    
    @BeforeClass
    public static void setUpClass() {
        mockedChannelFactory = mock(ChannelFactory.class);
    }
    
    @Before
    public void setUp() throws RabbitMQException {
        PowerMockito.mockStatic(ChannelFactory.class);
        when(ChannelFactory.getInstance()).thenReturn(mockedChannelFactory);
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
    
    @Test
    public void send_creates_channel_when_is_not_informed() throws RabbitMQException, UnsupportedEncodingException, IOException {
        rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        rabbitMQMessage = new RabbitMQMessageSend();
        rabbitMQMessage.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessage.setChannel(null);
        rabbitMQMessage.setCharset(CHARSET);
        rabbitMQMessage.setPayload(PAYLOAD);
        when(mockedChannelFactory.getChannel(rabbitMQConfigMessage)).thenReturn(mockedChannel);
        
        
        Producer.getInstance().send(rabbitMQMessage);
        
        verify(mockedChannelFactory).getChannel(rabbitMQConfigMessage);
    }    
    
    @Test
    public void send_closes_channel_when_told() throws RabbitMQException, UnsupportedEncodingException, IOException, TimeoutException {
        rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setConnection(mockedConnection);
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        rabbitMQMessage = new RabbitMQMessageSend();
        rabbitMQMessage.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessage.setChannel(mockedChannel);
        rabbitMQMessage.setCharset(CHARSET);
        rabbitMQMessage.setPayload(PAYLOAD);
        rabbitMQMessage.setCloseChannel(true);
        
        Producer.getInstance().send(rabbitMQMessage);
        
        verify(mockedChannel).close();
    }     
}
