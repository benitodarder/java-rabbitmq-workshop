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
import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;
import org.apache.log4j.Logger;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Consumer.class, Logger.class})
public class ConsumerThreadTest {

    private static Consumer mockedConsumer;
    private static Logger mockedLogger;
    private RabbitMQMessageConsume mockedRabbitMQMessageConsume;
    private Channel mockedChannel;
    private Connection mockedConnection;
    private RabbitMQConfigMessage mockRabbitMQConfigMessage;
    private ConsumerThread consumerThread;

    @BeforeClass
    public static void setUpClass() {
        mockedLogger = mock(Logger.class);
        mockedConsumer = mock(Consumer.class);
    }

    @Before
    public void setUp() {
        PowerMockito.mockStatic(Logger.class);
        when(Logger.getLogger(ConsumerThread.class)).thenReturn(mockedLogger);
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
        consumerThread.start();
    }

    @Test
    public void pauseChannel_calls_channel_cancel() throws IOException, RabbitMQException {
        Whitebox.setInternalState(consumerThread, "consumerThreadStarted", true);
        Whitebox.setInternalState(consumerThread, "consumerThreadPaused", false);

        consumerThread.pauseChannel();

        verify(mockedChannel).basicCancel(anyString());
    }

    @Test
    public void run_consumes_message() throws RabbitMQException {
        Whitebox.setInternalState(consumerThread, "consumerThreadStarted", false);
        Whitebox.setInternalState(consumerThread, "consumerThreadPaused", false);       
        
        consumerThread.run();

        verify(mockedConsumer, atLeast(1)).consume(any(RabbitMQMessageConsume.class));
    }

    @Test
    public void closeChannel_closes_channel_and_connection() throws IOException, TimeoutException, RabbitMQException {

        consumerThread.closeChannel();

        verify(mockedChannel).close();
        verify(mockedConnection).close();
    }

    @Test
    public void restartChannel_consumes_message() throws RabbitMQException {
        Whitebox.setInternalState(consumerThread, "consumerThreadStarted", true);
        Whitebox.setInternalState(consumerThread, "consumerThreadPaused", true);        

        consumerThread.restartChannel();

        verify(mockedConsumer, atLeast(1)).consume(any(RabbitMQMessageConsume.class));
    }

    @Test
    public void getConsumerTag_returns_running_consumer_tag() throws RabbitMQException {
        when(mockedConsumer.consume(any(RabbitMQMessageConsume.class))).thenReturn("Consumer Tag");
        Whitebox.setInternalState(consumerThread, "consumerThreadStarted", true);
        Whitebox.setInternalState(consumerThread, "consumerThreadPaused", true);
        consumerThread.restartChannel();

        String result = consumerThread.getConsumerTag();

        assertThat(result, equalTo("Consumer Tag"));
    }

    @Test(expected = RabbitMQException.class)
    public void pauseChannel_throws_expected_exception_when_basicCancel_fails() throws IOException, RabbitMQException {
        IOException mockedIOException = mock(IOException.class);
        PowerMockito.doThrow(mockedIOException).when(mockedChannel).basicCancel(anyString());
        Whitebox.setInternalState(consumerThread, "consumerThreadStarted", true);
        Whitebox.setInternalState(consumerThread, "consumerThreadPaused", false);

        consumerThread.pauseChannel();

    }

    @Test(expected = RabbitMQException.class)
    public void closeChannel_throws_expected_exception_when_closing_channel_fails() throws IOException, RabbitMQException, TimeoutException {
        TimeoutException mockedIOException = mock(TimeoutException.class);
        PowerMockito.doThrow(mockedIOException).when(mockedChannel).close();

        consumerThread.closeChannel();

    }

    @Test(expected = RabbitMQException.class)
    public void closeChannel_throws_expected_exception_when_closing_connection_fails() throws IOException, RabbitMQException, TimeoutException {
        IOException mockedIOException = mock(IOException.class);
        PowerMockito.doThrow(mockedIOException).when(mockedConnection).close();

        consumerThread.closeChannel();

    }

    @Test
    public void pauseChannel_warns_when_pausing_an_already_paused_channel() throws RabbitMQException, IOException {
        consumerThread.pauseChannel();

        consumerThread.pauseChannel();

        verify(mockedLogger, atLeast(1)).warn(ConsumerThread.WARNING_MESSAGE_PAUSE);
    }

    @Test
    public void pauseChannel_warns_when_pausing_a_non_started_channel() throws RabbitMQException, IOException {
        consumerThread = new ConsumerThread(mockedRabbitMQMessageConsume);

        consumerThread.pauseChannel();

        verify(mockedLogger, atLeast(1)).warn(ConsumerThread.WARNING_MESSAGE_PAUSE);
    }

    @Test
    public void restartChannel_warns_when_restarting_a_running_channel() throws RabbitMQException, IOException {
        Whitebox.setInternalState(consumerThread, "consumerThreadStarted", true);
        Whitebox.setInternalState(consumerThread, "consumerThreadPaused", false);

        consumerThread.restartChannel();

        verify(mockedLogger, atLeast(1)).warn(ConsumerThread.WARNING_MESSAGE_RESTART);
    }

    @Test
    public void restartChannel_warns_when_restarting_a_not_started_channel() throws RabbitMQException, IOException {
        Whitebox.setInternalState(consumerThread, "consumerThreadStarted", false);
        Whitebox.setInternalState(consumerThread, "consumerThreadPaused", false);

        consumerThread.restartChannel();

        verify(mockedLogger, atLeast(1)).warn(ConsumerThread.WARNING_MESSAGE_RESTART);
    }

    @Test
    public void run_on_running_thread_warns() throws RabbitMQException {
        consumerThread.run();

        consumerThread.run();

        verify(mockedLogger, atLeast(1)).warn(ConsumerThread.WARNING_MESSAGE_RUN);
    }
    
    @Test
    public void run_on_non_running_thread_starts_consuming() throws RabbitMQException {
        consumerThread = new ConsumerThread(mockedRabbitMQMessageConsume);

        consumerThread.run();
          
        verify(mockedConsumer, atLeast(1)).consume(any(RabbitMQMessageConsume.class));
    }    
}
