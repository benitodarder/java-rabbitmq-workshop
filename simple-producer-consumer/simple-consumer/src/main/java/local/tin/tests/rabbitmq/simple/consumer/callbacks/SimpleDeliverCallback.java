package local.tin.tests.rabbitmq.simple.consumer.callbacks;

import com.rabbitmq.client.Channel;
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
    private final Channel channel;

    public SimpleDeliverCallback(String charset, Channel channel) {
        this.charset = charset;
        this.channel = channel;
    }
    

    
    @Override
    public void handle(String string, Delivery dlvr) throws IOException {
        LOGGER.info(new String(dlvr.getBody(), charset));
        channel.basicAck(dlvr.getEnvelope().getDeliveryTag(), false);
    }

    public String getCharset() {
        return charset;
    }
    
    
}
