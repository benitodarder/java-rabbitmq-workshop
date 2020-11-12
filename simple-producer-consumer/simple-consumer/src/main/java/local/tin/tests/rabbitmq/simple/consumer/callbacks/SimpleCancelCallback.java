package local.tin.tests.rabbitmq.base.callbacks;

import com.rabbitmq.client.CancelCallback;
import java.io.IOException;

/**
 * Implements an empty CancelCallback
 *
 * @author benitodarder
 */
public class EmptyCancelCallback implements CancelCallback {

    @Override
    public void handle(String string) throws IOException {
        
    }
    
}
