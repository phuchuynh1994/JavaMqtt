import ca.uhn.fhir.context.FhirContext;
import ca.uhn.fhir.rest.client.api.IGenericClient;
import com.google.gson.Gson;
import de.fraunhofer.isst.health.digitalerengel.DataWindow;
import org.eclipse.paho.client.mqttv3.*;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

public class Main {

    private static Gson gson = new Gson();
    public static final String CLIENT_ID = MqttClient.generateClientId();
    public static final String BROKER_URL = "tcp://192.168.2.114:1883";
    public static final String topic = "patient/rawecg";

    private static void sendToMosquitto() {
        MemoryPersistence persistence = new MemoryPersistence();
        int qos = 2;
        ArrayList<Integer> list = new ArrayList<>();

        try {
            MqttClient sampleClient = new MqttClient(BROKER_URL, CLIENT_ID, persistence);

            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(false);
            connOpts.setAutomaticReconnect(true);
            sampleClient.connect(connOpts);
            System.out.println("Connected");

            sampleClient.setCallback(new MqttCallbackExtended() {
                @Override
                public void connectComplete(boolean b, String s) {
                    if(b) System.out.println("reconnect");
                    else System.out.println("connect");
                }

                @Override
                public void connectionLost(Throwable cause) {
                    cause.printStackTrace();
                    System.out.println("Connection Lost");
                }



                @Override
                public void messageArrived(String topic, MqttMessage message) throws Exception {
                    DataWindow dataWindow = gson.fromJson(message.toString(),DataWindow.class);
                    list.add(message.getId());
                    Collections.sort(list);
                    System.out.println(gson.toJson(list));
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                }
            });
            //sampleClient.subscribe("rawecg/hrv");
            sampleClient.subscribe(topic,qos);

        }catch (MqttException e){
            e.printStackTrace();
        }
    }



    public static void main(String[] args) {
        sendToMosquitto();
    }



}