package local.tin.tests.rabbitmq.simple.consumer.callbacks;

import com.rabbitmq.client.CancelCallback;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Implements a simple CancelCallback
 *
 * @author benitodarder
 */
public class SimpleCancelCallback implements CancelCallback {
    
    private static final Logger LOGGER = Logger.getLogger(SimpleCancelCallback.class);

    @Override
    public void handle(String string) throws IOException {
        LOGGER.info("SimpleCancelCallback: " + string);
    }
    
}
