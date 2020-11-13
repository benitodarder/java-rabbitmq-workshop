package local.tin.tests.rabbitmq.base.threads;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.exchanges.Consumer;
import local.tin.tests.rabbitmq.base.factories.ConnectionsFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageConsume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.anyString;
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
@PrepareForTest({Consumer.class})
public class ConsumerThreadTest {
    
    private static Consumer mockedConsumer;
    private RabbitMQMessageConsume mockedRabbitMQMessageConsume;
    private Channel mockedChannel;
    private Connection mockedConnection;
    private RabbitMQConfigMessage mockRabbitMQConfigMessage;
    private ConsumerThread consumerThread;
    
    @BeforeClass
    public static void setUpClass() {
        mockedConsumer = mock(Consumer.class);
    }
    
    @Before
    public void setUp() {
        PowerMockito.mockStatic(Consumer.class);
        when(Consumer.getInstance()).thenReturn(mockedConsumer);
        mockedRabbitMQMessageConsume = mock(RabbitMQMessageConsume.class);
        mockedChannel = mock(Channel.class);
        when(mockedRabbitMQMessageConsume.getChannel()).thenReturn(mockedChannel);
        mockedConnection = mock(Connection.class);
        mockRabbitMQConfigMessage = mock(RabbitMQConfigMessage.class);
        when(mockRabbitMQConfigMessage.getConnection()).thenReturn(mockedConnection);
        when(mockedRabbitMQMessageConsume.getRabbitMQConfigMessage()).thenReturn(mockRabbitMQConfigMessage);
        consumerThread = new ConsumerThread(mockedRabbitMQMessageConsume);
    }
    
    @Test
    public void pauseChannel_calls_channel_cancel() throws IOException {
        
        consumerThread.pauseChannel();
        
        verify(mockedChannel).basicCancel(anyString());
    }
    
    @Test
    public void run_consumes_message() throws RabbitMQException {
        
        consumerThread.run();
        
        verify(mockedConsumer).consume(mockedRabbitMQMessageConsume);
    }
    
    @Test
    public void closeChannel_closes_channel_and_connection() throws IOException, TimeoutException {
        
        consumerThread.closeChannel();
        
        verify(mockedChannel).close();
        verify(mockedConnection).close();
    }
}
