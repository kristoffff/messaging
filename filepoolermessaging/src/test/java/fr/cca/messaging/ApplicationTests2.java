package fr.cca.messaging;

import fr.cca.messaging.model.Message;
import fr.cca.messaging.model.MessageType;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.RestTemplate;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
public class ApplicationTests2 {
    private String restUrl = "http://localhost:8080/messages";

    @Test
    public void contextLoads() throws InterruptedException {
        RestTemplate restTemplate = new RestTemplate();

        HttpEntity<Message> requestEnd = new HttpEntity<>(new Message(MessageType.END, "toto5", null));
        restTemplate.postForObject(restUrl, requestEnd, Message.class);


    }

}