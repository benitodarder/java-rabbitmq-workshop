package local.tin.tests.rabbitmq.simple.consumer.threads;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.exchanges.Consumer;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageConsume;
import org.apache.log4j.Logger;

/**
 *
 * @author benitodarder
 */
public class ConsumerThread extends Thread {
    
    private static final Logger LOGGER = Logger.getLogger(ConsumerThread.class);
    private final RabbitMQMessageConsume consumer;
    private String consumerTag;
    
    public ConsumerThread(RabbitMQMessageConsume consumer) {
        this.consumer = consumer;
    }
    
    public void pauseChannel() throws IOException {
        consumer.getChannel().basicCancel(consumerTag);
    }
            
    
    public void restartChannel() {
        this.run();
    }

    @Override
    public void run() {
        try {
            consumerTag = Consumer.getInstance().consume(consumer);
        } catch (RabbitMQException ex) {
            LOGGER.error("Unexpected RabbitMQException runnin... ", ex);
        }
    }
    
    public void closeChannel() throws IOException, TimeoutException {
        LOGGER.info("About to close the channel");
        consumer.getChannel().close();
        LOGGER.info("Channel closed");
        LOGGER.info("About to close the connection");
        consumer.getRabbitMQConfigMessage().getConnection().close();
        LOGGER.info("Connection closed");
        LOGGER.info("About to interrupt the thread");
        this.interrupt();
        LOGGER.info("Thread interrupted.");
    }
}
