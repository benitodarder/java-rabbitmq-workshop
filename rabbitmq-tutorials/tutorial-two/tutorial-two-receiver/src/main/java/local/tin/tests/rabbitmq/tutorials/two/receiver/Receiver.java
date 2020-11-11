package local.tin.tests.rabbitmq.tutorials.two.receiver;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import org.apache.log4j.Logger;

public class Receiver {

    private static final Logger LOGGER = Logger.getLogger(Receiver.class);
    private final static String QUEUE_NAME = "hello";
    private static final boolean AUTO_ACKNOWLEDGE=false;

    public static void main(String[] argv) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("192.168.56.19");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");

            System.out.println(" [x] Received '" + message + "'");
            try {
                doWork(message);
            } catch (InterruptedException ex) {
                LOGGER.error(ex.getLocalizedMessage());
            } finally {
                System.out.println(" [x] Done");
                channel.basicAck(delivery.getEnvelope().getDeliveryTag(), AUTO_ACKNOWLEDGE);
            }
        };
//        boolean autoAck = true; // acknowledgment is covered below
        channel.basicConsume(QUEUE_NAME, AUTO_ACKNOWLEDGE, deliverCallback, consumerTag -> {});
    }

    private static void doWork(String task) throws InterruptedException {
        for (char ch : task.toCharArray()) {
            if (ch == '.') {
                Thread.sleep(1000);
            }
        }
    }
}
