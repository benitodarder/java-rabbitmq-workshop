package local.tin.tests.rabbitmq.base.threads;

import local.tin.tests.rabbitmq.base.model.RabbitMQException;


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
public interface IConsumerThread {

    /**
     * Pauses a started and non paused channel.
     * 
     * @throws RabbitMQException 
     */
    public void pauseChannel() throws RabbitMQException;
    
    /**
     * Restarts a started and paused channel.
     * 
     * @throws RabbitMQException 
     */
    public void restartChannel() throws RabbitMQException;
    
    /**
     * Closes then channel and the underlying connection.
     * 
     * @throws RabbitMQException 
     */
    public void closeChannel() throws RabbitMQException;
    
    /**
     * Returns last consumer tag retrieved.
     * 
     * @return String 
     */
    public String getConsumerTag();
}
