package local.tin.tests.rabbitmq.base.factories;

import local.tin.tests.rabbitmq.base.factories.ConnectionsFactory;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigConnectionFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Mockito.verify;
import org.powermock.api.mockito.PowerMockito;
import static org.powermock.api.mockito.PowerMockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

/**
 *
 * @author benitodarder
 */
@RunWith(PowerMockRunner.class)
@PrepareForTest({ConnectionFactory.class, ConnectionsFactory.class})
public class ConnectionsFactoryTest {

    private static final String HOST_NAME = "hoooost";   
    private static final String PASSWORD = "pass";
    private static final String USER_NAME = "user";     
    private Connection mockedConnection;        
    private ConnectionFactory mockedConnectionFactory;

    @Before
    public void setUp() throws Exception {
     
    }
    
    @Test
    public void getConnection_assigns_hostname() throws RabbitMQException, Exception {
        mockedConnectionFactory = mock(ConnectionFactory.class);
        PowerMockito.whenNew(ConnectionFactory.class).withNoArguments().thenReturn(mockedConnectionFactory);
        mockedConnection = mock(Connection.class);         
        when(mockedConnectionFactory.newConnection()).thenReturn(mockedConnection);           
        RabbitMQConfigConnectionFactory rabbitMQConfigConnectionFactory = new RabbitMQConfigConnectionFactory();
        rabbitMQConfigConnectionFactory.setHost(HOST_NAME);
        
        ConnectionsFactory.getInstance().getConnection(rabbitMQConfigConnectionFactory);
        
        verify(mockedConnectionFactory).setHost(HOST_NAME);
    }
    
    @Test(expected = RabbitMQException.class)
    public void getConnection_throws_expected_exception_when_host_is_null() throws RabbitMQException, Exception {      
        RabbitMQConfigConnectionFactory rabbitMQConfigConnectionFactory = new RabbitMQConfigConnectionFactory();
        
        ConnectionsFactory.getInstance().getConnection(rabbitMQConfigConnectionFactory);
        
    }    
    
    
    @Test
    public void getConnection_assigns_username_password() throws RabbitMQException, Exception {
        mockedConnectionFactory = mock(ConnectionFactory.class);
        PowerMockito.whenNew(ConnectionFactory.class).withNoArguments().thenReturn(mockedConnectionFactory);
        mockedConnection = mock(Connection.class);         
        when(mockedConnectionFactory.newConnection()).thenReturn(mockedConnection);           
        RabbitMQConfigConnectionFactory rabbitMQConfigConnectionFactory = new RabbitMQConfigConnectionFactory();
        rabbitMQConfigConnectionFactory.setHost(HOST_NAME);
        rabbitMQConfigConnectionFactory.setUserName(USER_NAME);
        rabbitMQConfigConnectionFactory.setPassword(PASSWORD);
        
        ConnectionsFactory.getInstance().getConnection(rabbitMQConfigConnectionFactory);
        
        verify(mockedConnectionFactory).setUsername(USER_NAME);
        verify(mockedConnectionFactory).setPassword(PASSWORD);
    }     
}
