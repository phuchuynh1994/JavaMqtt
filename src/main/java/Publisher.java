import com.google.gson.Gson;
import org.apache.kafka.common.protocol.types.Field;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class Publisher {

    private static Gson gson = new Gson();
    public static final String Client_ID = "paho78473798513800";
    public static final String brocker_URL = "tcp://192.168.2.115:1883";

    public void connect() throws MqttException {

        MqttClient mqttClient = new MqttClient(brocker_URL, Client_ID, new MemoryPersistence());
        MqttConnectOptions connOpt = new MqttConnectOptions();
        connOpt.setCleanSession(true);
        mqttClient.connect(connOpt);

        System.out.println("connected");
        System.out.println(Client_ID);
        System.out.println(Main.topic);
        mqttClient.setCallback(new MqttCallback() {
            @Override
            public void connectionLost(Throwable cause) {
                cause.printStackTrace();
                System.out.println("Connection Lost");
            }

            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                System.out.println("Received message: "+mqttMessage.toString());
                MqttEcgRecord ecgRecord = gson.fromJson(mqttMessage.toString(), MqttEcgRecord.class);
                //long ping = System.currentTimeMillis() - ecgRecord.getDatetime();
                System.out.println("Received Message from topic: " + ", with content: " + mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }

        });
        mqttClient.subscribe(Main.topic);
    }

    public static void main(String[] args){
        Publisher publisher = new Publisher();
        try {
            publisher.connect();
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
