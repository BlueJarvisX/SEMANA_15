package com.whasupp.app.data;

import android.util.Log;
import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.net.ssl.SSLSocketFactory;

/**
 * Singleton MqttRepository Corregido.
 * Implementa manejo de hilos (Threading) para operaciones de red.
 */
public class MqttRepository {
    private static MqttRepository instance;
    private MqttClient client;
    private final ExecutorService executor; // Hilo dedicado a la red

    // TUS Credenciales HiveMQ
    private static final String BROKER = "ssl://cefcfb50683944afb36f3f3697ea836c.s1.eu.hivemq.cloud:8883";
    private static final String USER = "Kevin";
    private static final String PASS = "kevin54321T";
    private static final String CLIENT_ID = MqttClient.generateClientId();

    private MqttRepository() {
        // Creamos un ejecutor de un solo hilo para procesar tareas de red en orden
        executor = Executors.newSingleThreadExecutor();
        try {
            client = new MqttClient(BROKER, CLIENT_ID, new MemoryPersistence());
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public static synchronized MqttRepository getInstance() {
        if (instance == null) instance = new MqttRepository();
        return instance;
    }

    public void connect(Runnable onSuccess, Runnable onError) {
        executor.execute(() -> {
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName(USER);
            options.setPassword(PASS.toCharArray());
            options.setCleanSession(true);
            options.setKeepAliveInterval(60);
            options.setAutomaticReconnect(true);

            // Configuración SSL necesaria para HiveMQ Cloud
            try {
                options.setSocketFactory(SSLSocketFactory.getDefault());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                if (client != null && !client.isConnected()) {
                    Log.d("MQTT_DEBUG", "Conectando al broker...");
                    client.connect(options);
                    Log.d("MQTT_DEBUG", "¡Conectado exitosamente!");
                    if (onSuccess != null) onSuccess.run();
                } else if (client != null && client.isConnected()) {
                    if (onSuccess != null) onSuccess.run();
                }
            } catch (MqttException e) {
                Log.e("MQTT_DEBUG", "Error de conexión: " + e.getMessage());
                e.printStackTrace();
                if (onError != null) onError.run();
            }
        });
    }

    public void subscribe(String topic, IMqttMessageListener listener) {
        executor.execute(() -> {
            try {
                if (client != null && client.isConnected()) {
                    client.subscribe(topic, 1, listener);
                    Log.d("MQTT_DEBUG", "Suscrito al tópico: " + topic);
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }

    public void publish(String topic, String payload) {
        executor.execute(() -> {
            try {
                if (client != null && client.isConnected()) {
                    MqttMessage message = new MqttMessage(payload.getBytes());
                    message.setQos(1);
                    client.publish(topic, message);
                    Log.d("MQTT_DEBUG", "Mensaje enviado: " + payload);
                } else {
                    Log.e("MQTT_DEBUG", "Error: Cliente desconectado, no se envió mensaje.");
                }
            } catch (MqttException e) {
                e.printStackTrace();
            }
        });
    }
}