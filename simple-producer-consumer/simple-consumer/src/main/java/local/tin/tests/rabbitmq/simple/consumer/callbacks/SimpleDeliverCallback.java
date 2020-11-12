package local.tin.tests.rabbitmq.simple.consumer.callbacks;

import com.rabbitmq.client.DeliverCallback;
import com.rabbitmq.client.Delivery;
import java.io.IOException;
import org.apache.log4j.Logger;

/**
 *
 * @author benitodarder
 */
public class SimpleDeliverCallback implements DeliverCallback {

    private static final Logger LOGGER = Logger.getLogger(SimpleDeliverCallback.class);
    private final String charset;

    public SimpleDeliverCallback(String charset) {
        this.charset = charset;
    }
    
    @Override
    public void handle(String string, Delivery dlvr) throws IOException {
        LOGGER.info(new String(dlvr.getBody(), charset));
    }

    public String getCharset() {
        return charset;
    }
    
    
}
