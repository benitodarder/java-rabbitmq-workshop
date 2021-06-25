package local.tin.tests.rabbitmq.base.threads;

import java.io.IOException;
import java.util.concurrent.TimeoutException;
import local.tin.tests.rabbitmq.base.exchanges.Consumer;
import local.tin.tests.rabbitmq.base.model.RabbitMQException;
import local.tin.tests.rabbitmq.base.model.RabbitMQMessageConsume;
import org.apache.log4j.Logger;

/**
 * Consume a RabbitMQ.
 *
 * <ul>
 * <li>Starts with Thread.start</li>
 * <li>Pauses consumption</li>
 * <li>Restarts consumption</li>
 * <li>Returns last consumer tag</li>
 * </ul>
 *
 * @author benitodarder
 */
public class ConsumerThread extends Thread implements IConsumerThread {

    public static final String WARNING_MESSAGE_PAUSE = "Comsumer thread already paused or not started. No action performed.";
    public static final String WARNING_MESSAGE_RESTART = "Comsumer thread already running or not started still. No action performed.";
    public static final String WARNING_MESSAGE_RUN = "Comsumer thread already running. No action performed.";
    private static final Logger LOGGER = Logger.getLogger(ConsumerThread.class);
    private final RabbitMQMessageConsume consumer;
    private boolean consumerThreadPaused;
    private boolean consumerThreadStarted;
    private String consumerTag;

    public ConsumerThread(RabbitMQMessageConsume consumer) {
        this.consumer = consumer;
        consumerThreadPaused = false;
        consumerThreadStarted = false;
    }

    @Override
    public void pauseChannel() throws RabbitMQException {
        if (consumerThreadStarted && !consumerThreadPaused) {
            try {
                consumer.getChannel().basicCancel(consumerTag);
                consumerThreadPaused = true;
            } catch (IOException ex) {
                throw new RabbitMQException(ex);
            }
        } else {
            LOGGER.warn(WARNING_MESSAGE_PAUSE);
        }
    }

    @Override
    public void restartChannel() throws RabbitMQException {
        if (consumerThreadStarted && consumerThreadPaused) {
            startChannel();
        } else {
            LOGGER.warn(WARNING_MESSAGE_RESTART);
        }
    }

    @Override
    public void run() {
        if (!consumerThreadStarted) {
            try {
                startChannel();
            } catch (RabbitMQException ex) {
                LOGGER.error("Unexpected RabbitMQException running channel.", ex);
            }
        } else {
            LOGGER.warn(WARNING_MESSAGE_RUN);
        }
    }

    @Override
    public void closeChannel() throws RabbitMQException {
        try {
            LOGGER.debug("About to close the channel");
            consumer.getChannel().close();
            LOGGER.debug("Channel closed");
            LOGGER.debug("About to close the connection");
            consumer.getRabbitMQConfigMessage().getConnection().close();
            LOGGER.debug("Connection closed");
        } catch (IOException | TimeoutException ex) {
            throw new RabbitMQException(ex);
        }
    }

    private void startChannel() throws RabbitMQException {
        consumerTag = Consumer.getInstance().consume(consumer);
        consumerThreadStarted = true;
    }

    @Override
    public String getConsumerTag() {
        return consumerTag;
    }
    
    @Override
    public synchronized void start() {
        try {
            startChannel();
        } catch (RabbitMQException ex) {
            LOGGER.error("Unexpected RabbitMQException starting channel.", ex);
        }
        super.start();
    }
}
