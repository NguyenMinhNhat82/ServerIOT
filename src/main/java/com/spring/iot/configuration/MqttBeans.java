package com.spring.iot.configuration;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.iot.dto.Status;
import com.spring.iot.entities.Sensor;
import com.spring.iot.entities.SensorValue;
import com.spring.iot.entities.Station;
import com.spring.iot.services.SensorService;
import com.spring.iot.services.SensorValueService;
import com.spring.iot.services.SheetService;
import com.spring.iot.services.StationService;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.json.JSONArray;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.integration.annotation.ServiceActivator;
import org.springframework.integration.channel.DirectChannel;
import org.springframework.integration.core.MessageProducer;
import org.springframework.integration.mqtt.core.DefaultMqttPahoClientFactory;
import org.springframework.integration.mqtt.core.MqttPahoClientFactory;
import org.springframework.integration.mqtt.inbound.MqttPahoMessageDrivenChannelAdapter;
import org.springframework.integration.mqtt.outbound.MqttPahoMessageHandler;
import org.springframework.integration.mqtt.support.DefaultPahoMessageConverter;
import org.springframework.integration.mqtt.support.MqttHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.MessageHandler;
import org.springframework.messaging.MessagingException;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;


@Configuration
public class MqttBeans {
    @Autowired
    private SimpMessagingTemplate simpMessagingTemplate;

    @Autowired
    private SensorService sensorService;

    @Autowired
    private StationService stationService;

    @Autowired
    private SensorValueService sensorValueService;

    @Autowired
    private SheetService sheetService;

    @Bean
    public MqttPahoClientFactory mqttClientFactory() {
        try {
            DefaultMqttPahoClientFactory factory = new DefaultMqttPahoClientFactory();
            MqttConnectOptions options = new MqttConnectOptions();
            options.setServerURIs(new String[]{"tcp://mqttserver.tk:1883"});
            options.setCleanSession(true);
            options.setUserName("innovation");
            options.setPassword("Innovation_RgPQAZoA5N".toCharArray());
            options.setAutomaticReconnect(true);
            options.setKeepAliveInterval(100);
            factory.setConnectionOptions(options);
            return factory;

        }catch (Exception exception){
            System.out.println("mqttClientFactory:\n"+ exception.getMessage());
            return null;
        }

    }

    @Bean
    public MessageChannel mqttInputChannel() {
        return new DirectChannel();
    }

    @Bean
    public MessageProducer inbound() {
        try {
            MqttPahoMessageDrivenChannelAdapter adapter = new MqttPahoMessageDrivenChannelAdapter("serverIn",
                    mqttClientFactory(), "/innovation/airmonitoring/NBIOTs");

            adapter.setCompletionTimeout(5000);
            adapter.setConverter(new DefaultPahoMessageConverter());
            adapter.setQos(2);
            adapter.setOutputChannel(mqttInputChannel());
            return adapter;
        }catch (Exception exception){
            System.out.println("inbound:\n"+exception.getMessage());
            return null;
        }

    }


    @Bean
    @ServiceActivator(inputChannel = "mqttInputChannel")
    public MessageHandler handler() {
        return new MessageHandler() {
            @Override
            public void handleMessage(Message<?> message) throws MessagingException {
//                System.out.println(message.getPayload());
                String topic = message.getHeaders().get(MqttHeaders.RECEIVED_TOPIC).toString();
                DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                Calendar cal = Calendar.getInstance();
                JSONArray myjson = null;
                try {
                    myjson = new JSONArray( "["+message.getPayload().toString() +"]");
                    List<Station> listStationInJSON = new ArrayList<>();
                    for(int i =0 ; i < myjson.length();i++){
                        Map<String,Object> result =
                                new ObjectMapper().readValue(myjson.get(0).toString(), HashMap.class);
                        Station getStation = stationService.findStattionByID(result.get("station_id").toString());
                        getStation.setName(result.get("station_name").toString());
                        getStation.setActive(true);
                        listStationInJSON.add(getStation);
                        Station station = stationService.addOrUpdate(getStation);


                        List<?> sensor = (List<?>) result.get("sensors");
                        Set<Sensor> sensors = new HashSet<>();
                        for(int j =0 ; j < sensor.size();j++){
                            Map<String,String> obj = new HashMap<>((Map) sensor.get(j)) ;
                            Sensor s = sensorService.findSensorByID(obj.get("id"));
                            sensorService.addOrUpdate(s);

                            ZoneId zid = ZoneId.of("Asia/Saigon");
                            SensorValue sensorValue = new SensorValue(0,String.valueOf(obj.get("value")),
                                    LocalDateTime.now(zid),s);
//                            Sensor s = new Sensor(obj.get("id"), String.valueOf(obj.get("value")),station);
                            sensorValueService.addOrUpdate(sensorValue);
                        }
                        stationService.setNonActiveForStation(listStationInJSON);
//                        sheetService.updateGoogleSheet();
                        com.spring.iot.dto.Message m = new com.spring.iot.dto.Message("server", "client", message.getPayload().toString(), dateFormat.format(cal.getTime()), Status.MESSAGE);
                        simpMessagingTemplate.convertAndSendToUser(m.getReceiverName(), "/private", m);
//                        sheetService.update();
                        System.out.println(message.getPayload());
                    }
                }catch (JSONException | IOException e){
                    System.out.println("MessageHandler:\n"+ e.getMessage());
                    throw new RuntimeException(e);
                }

            }

        }

                ;
    }



    @Bean
        public MessageChannel mqttOutboundChannel () {
            return new DirectChannel();
        }

        @Bean
        @ServiceActivator(inputChannel = "mqttOutboundChannel")
        public MessageHandler mqttOutbound () {
        try {
            //clientId is generated using a random number
            MqttPahoMessageHandler messageHandler = new MqttPahoMessageHandler("serverOut", mqttClientFactory());
            messageHandler.setAsync(true);
            messageHandler.setDefaultTopic("/innovation/airmonitoring/NBIOTs");
            messageHandler.setDefaultRetained(false);
            return messageHandler;
        }catch (Exception exception){
            System.out.println("mqttOutbound:\n"+exception.getMessage());
            return null;
        }

        }

    }
