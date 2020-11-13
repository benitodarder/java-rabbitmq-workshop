package local.tin.tests.rabbitmq.base.exchanges;

import com.rabbitmq.client.CancelCallback;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageConsume;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
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
public class ConsumerTest {
    
    private static final String CONSUMER_TAG = "consumer tag";    
    private static final String QUEUE_NAME = "queueueueue";
    private static final String CHARSET = "UTF-8";    
    private static ChannelFactory mockedChannelFactory;
    private Channel mockedChannel;
    private RabbitMQConfigMessage rabbitMQConfigMessage;
    private DeliverCallback mockedDeliverCallback;
    private CancelCallback mockedCancelCallback;
    private RabbitMQMessageConsume rabbitMQMessageConsume;
    
    @BeforeClass
    public static void setUpClass() {
        
        mockedChannelFactory = mock(ChannelFactory.class);
    }
        
    
    @Before
    public void setUp() throws RabbitMQException {
        PowerMockito.mockStatic(ChannelFactory.class);
        when(ChannelFactory.getInstance()).thenReturn(mockedChannelFactory);        
        mockedChannel = mock(Channel.class);
        mockedDeliverCallback = mock(DeliverCallback.class);
        mockedCancelCallback = mock(CancelCallback.class);
    }
    
    @Test
    public void consume_waits_for_incoming_messages_with_given_properties() throws RabbitMQException, IOException {
        rabbitMQConfigMessage = new RabbitMQConfigMessage();
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        rabbitMQMessageConsume = new RabbitMQMessageConsume();
        rabbitMQMessageConsume.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessageConsume.setChannel(mockedChannel);
        rabbitMQMessageConsume.setDeliveryCallback(mockedDeliverCallback);
        rabbitMQMessageConsume.setCancelCallback(mockedCancelCallback);
        
        Consumer.getInstance().consume(rabbitMQMessageConsume);
     
        verify(mockedChannel).basicConsume(QUEUE_NAME, rabbitMQMessageConsume.isAutoAcknowledge(), mockedDeliverCallback, mockedCancelCallback);
    }
    
    @Test
    public void consume_creates_new_channel_when_not_informed() throws RabbitMQException, IOException {
        rabbitMQConfigMessage = new RabbitMQConfigMessage();
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        rabbitMQMessageConsume = new RabbitMQMessageConsume();
        rabbitMQMessageConsume.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessageConsume.setChannel(null);
        rabbitMQMessageConsume.setDeliveryCallback(mockedDeliverCallback);
        rabbitMQMessageConsume.setCancelCallback(mockedCancelCallback);
        when(mockedChannelFactory.getChannel(rabbitMQConfigMessage)).thenReturn(mockedChannel);
        
        Consumer.getInstance().consume(rabbitMQMessageConsume);
     
         verify(mockedChannelFactory).getChannel(rabbitMQConfigMessage);
    }    
    
    @Test
    public void consume_returns_consumer_tag() throws RabbitMQException, IOException {
        rabbitMQConfigMessage = new RabbitMQConfigMessage();
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        rabbitMQMessageConsume = new RabbitMQMessageConsume();
        rabbitMQMessageConsume.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessageConsume.setChannel(mockedChannel);
        rabbitMQMessageConsume.setDeliveryCallback(mockedDeliverCallback);
        rabbitMQMessageConsume.setCancelCallback(mockedCancelCallback);
        when(mockedChannel.basicConsume(QUEUE_NAME, rabbitMQMessageConsume.isAutoAcknowledge(), mockedDeliverCallback, mockedCancelCallback)).thenReturn(CONSUMER_TAG);
        
        String result = Consumer.getInstance().consume(rabbitMQMessageConsume);
     
        assertThat(result, equalTo(CONSUMER_TAG));
    }    

}
