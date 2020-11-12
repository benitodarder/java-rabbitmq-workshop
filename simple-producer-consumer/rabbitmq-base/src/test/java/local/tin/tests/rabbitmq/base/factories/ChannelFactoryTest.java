package local.tin.tests.rabbitmq.base.factories;

import local.tin.tests.rabbitmq.base.factories.ConnectionsFactory;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.io.IOException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigMessage;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigReceiver;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigSender;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

/**
 *
 * @author benitodarder
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConnectionsFactory.class})
public class ChannelFactoryTest {

    private static final int BASIC_QOS = 666;
    private static final boolean ACKNOWLEDGE = false;
    private static final boolean DURABLE = true;
    private static final String QUEUE_NAME = "Name";
    private static ConnectionsFactory mockedConnectionsFactory;
    private Connection mockedConnection;
    private Channel mockedChannel;

    @BeforeClass
    public static void setUpClass() {
        mockedConnectionsFactory = mock(ConnectionsFactory.class);
    }

    @Before
    public void setUp() throws Exception {
        PowerMockito.mockStatic(ConnectionsFactory.class);
        when(ConnectionsFactory.getInstance()).thenReturn(mockedConnectionsFactory);
        mockedConnection = mock(Connection.class);
        when(mockedConnectionsFactory.getConnection(any())).thenReturn(mockedConnection);
        mockedChannel = mock(Channel.class);
        when(mockedConnection.createChannel()).thenReturn(mockedChannel);
    }

    @Test
    public void getChannel_returns_expected_channel() throws RabbitMQException, IOException {
        RabbitMQConfigSender configSender = new RabbitMQConfigSender();
        configSender.setConnection(mockedConnection);
        configSender.setQueueName(QUEUE_NAME);
        configSender.setDurable(DURABLE);

        Channel result = ChannelFactory.getInstance().getChannel(configSender);

        assertThat(result, equalTo(mockedChannel));
    }

    @Test
    public void getChannel_assigns_server_fields() throws RabbitMQException, IOException {
        RabbitMQConfigSender configSender = new RabbitMQConfigSender();
        configSender.setConnection(mockedConnection);
        configSender.setQueueName(QUEUE_NAME);
        configSender.setDurable(DURABLE);

        ChannelFactory.getInstance().getChannel(configSender);

        verify(mockedChannel).queueDeclare(QUEUE_NAME, DURABLE, false, false, null);
    }

    @Test
    public void getChannel_assigns_receiver_fields() throws RabbitMQException, IOException {
        RabbitMQConfigReceiver configSender = new RabbitMQConfigReceiver();
        configSender.setConnection(mockedConnection);        
        configSender.setQueueName(QUEUE_NAME);
        configSender.setDurable(DURABLE);
        configSender.setAutoAcknowledge(ACKNOWLEDGE);
        configSender.setBasicQoS(BASIC_QOS);

        ChannelFactory.getInstance().getChannel(configSender);

        verify(mockedChannel).basicQos(BASIC_QOS);
        verify(mockedChannel).queueDeclare(QUEUE_NAME, DURABLE, false, false, null);
    }

    @Test(expected = RabbitMQException.class)
    public void getChannel_throws_exception_for_unexpected_config() throws RabbitMQException, IOException {
        RabbitMQConfigMessage configSender = new RabbitMQConfigMessage();
        configSender.setConnection(mockedConnection);    
        
        ChannelFactory.getInstance().getChannel(configSender);

    }
    
    @Test
    public void getChannel_gets_a_new_connection_when_is_not_included() throws RabbitMQException {
        RabbitMQConfigSender rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setHost("hoooost");
        
        ChannelFactory.getInstance().getChannel(rabbitMQConfigMessage);
        
        verify(mockedConnectionsFactory).getConnection(rabbitMQConfigMessage);
    }
    
    @Test
    public void getChannel_assigns_the_connection_to_the_config_message_when_a_new_connection_is_created() throws RabbitMQException {
        RabbitMQConfigSender rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setHost("hoooost");
        
        ChannelFactory.getInstance().getChannel(rabbitMQConfigMessage);
        
        assertThat(rabbitMQConfigMessage.getConnection(), equalTo(mockedConnection));
    }    
}
