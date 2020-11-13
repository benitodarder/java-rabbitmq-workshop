package local.tin.tests.rabbitmq.base.callbacks;

import com.rabbitmq.client.CancelCallback;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 * Implements an empty CancelCallback
 *
 * @author benitodarder
 */
public class EmptyCancelCallback implements CancelCallback {
    
    private static final Logger LOGGER = Logger.getLogger(EmptyCancelCallback.class);

    @Override
    public void handle(String string) throws IOException {
        LOGGER.warn("Empty CancelCallback handled");
    }
    
}
