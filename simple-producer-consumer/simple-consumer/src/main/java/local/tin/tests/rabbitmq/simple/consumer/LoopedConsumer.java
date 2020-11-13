package local.tin.tests.rabbitmq.simple.consumer;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import java.io.IOException;
import java.util.Scanner;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.callbacks.EmptyCancelCallback;
import local.tin.tests.rabbitmq.base.factories.ChannelFactory;
import local.tin.tests.rabbitmq.base.factories.ConnectionsFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigConnectionFactory;
import local.tin.tests.rabbitmq.base.model.RabbitMQConfigSender;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageConsume;
import static local.tin.tests.rabbitmq.simple.consumer.Consumer.AUTO_ACKNOWLEDGE;
import static local.tin.tests.rabbitmq.simple.consumer.Consumer.CHARSET;
import static local.tin.tests.rabbitmq.simple.consumer.Consumer.HOST;
import static local.tin.tests.rabbitmq.simple.consumer.Consumer.QUEUE_NAME;
import local.tin.tests.rabbitmq.simple.consumer.callbacks.SimpleCancelCallback;
import local.tin.tests.rabbitmq.simple.consumer.callbacks.SimpleDeliverCallback;
import local.tin.tests.rabbitmq.simple.consumer.threads.ConsumerThread;
import org.apache.log4j.Logger;

/**
 *
 * @author benitodarder
 */
public class LoopedConsumer {

    public static final String HOST = "192.168.56.19";
    public static final String QUEUE_NAME = "hello";
    public static final String CHARSET = "UTF-8";
    public static final boolean AUTO_ACKNOWLEDGE = false;
    private static final Logger LOGGER = Logger.getLogger(LoopedConsumer.class);

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException, RabbitMQException, TimeoutException {
        RabbitMQConfigConnectionFactory rabbitMQConfigConnectionFactory = new RabbitMQConfigConnectionFactory();
        rabbitMQConfigConnectionFactory.setHost(HOST);
        Connection connection = ConnectionsFactory.getInstance().getConnection(rabbitMQConfigConnectionFactory);
        RabbitMQConfigSender rabbitMQConfigMessage = new RabbitMQConfigSender();
        rabbitMQConfigMessage.setConnection(connection);
        rabbitMQConfigMessage.setQueueName(QUEUE_NAME);
        Channel channel = ChannelFactory.getInstance().getChannel(rabbitMQConfigMessage);
        RabbitMQMessageConsume rabbitMQMessage = new RabbitMQMessageConsume();
        rabbitMQMessage.setRabbitMQConfigMessage(rabbitMQConfigMessage);
        rabbitMQMessage.setChannel(channel);
        rabbitMQMessage.setCharset(CHARSET);
        SimpleDeliverCallback simpleDeliverCallback = new SimpleDeliverCallback(CHARSET);
        rabbitMQMessage.setDeliveryCallback(simpleDeliverCallback);
        SimpleCancelCallback emptyCancelCallback = new SimpleCancelCallback();
        rabbitMQMessage.setCancelCallback(emptyCancelCallback);
        rabbitMQMessage.setAutoAcknowledge(AUTO_ACKNOWLEDGE);
        ConsumerThread consumerThread = new ConsumerThread(rabbitMQMessage);
        consumerThread.start();
        boolean exit = false;
        LOGGER.info("type stop to pause the process; type resume to resume; type exit to exit");
        while (!exit) {
            try {
                Scanner scanner = new Scanner(System.in);
                String line = scanner.nextLine().trim();
                if (line.equalsIgnoreCase("stop")) {
                    LOGGER.info("about to pause the channel");
                    consumerThread.pauseChannel();
                    LOGGER.info("channel paused");
                } else if (line.equalsIgnoreCase("exit")) {
                    LOGGER.info("about to exit");
                    consumerThread.closeChannel();
                    exit = true;
                } else if (line.equalsIgnoreCase("resume")) {
                    LOGGER.info("about to resume consuming");
                    consumerThread.restartChannel();
                }
            } catch (Exception ex) {
                LOGGER.error("Something when wrong: " + ex.getLocalizedMessage());
            }
        }
        LOGGER.info("Bye folks...");
    }

}
