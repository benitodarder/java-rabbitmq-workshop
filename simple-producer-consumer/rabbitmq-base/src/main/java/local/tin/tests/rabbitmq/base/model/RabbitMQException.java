package local.tin.tests.rabbitmq.base.model;

/**
 *
 * @author benitodarder
 */
public class RabbitMQException extends Exception {

    public RabbitMQException() {
    }

    public RabbitMQException(String message) {
        super(message);
    }

    public RabbitMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public RabbitMQException(Throwable cause) {
        super(cause);
    }

    public RabbitMQException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
    
    
    
}
