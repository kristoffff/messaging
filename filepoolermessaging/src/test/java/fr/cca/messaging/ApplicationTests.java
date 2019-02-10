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
public class ApplicationTests {
    private String restUrl = "http://localhost:8080/messages";

    @Test
    public void contextLoads() throws InterruptedException {
        CountDownLatch cdl = new CountDownLatch(10);
        RestTemplate restTemplate = new RestTemplate();
        HttpEntity<Message> request = new HttpEntity<>(new Message(MessageType.START, "toto5", null));
        restTemplate.postForObject(restUrl, request, Message.class);

        ExecutorService es = Executors.newFixedThreadPool(10);
        for (int nbThread= 0;nbThread<10;nbThread++) {
            es.submit(() -> {
                try {
                    RestTemplate restTemplate2 = new RestTemplate();
                    for (int i = 0; i < 20000; i++) {
                        HttpEntity<Message> requestData = new HttpEntity<>(new Message(MessageType.DATA, "toto5", "message" + i));
                        restTemplate2.postForObject(restUrl, requestData, Message.class);
                    }
                }
                finally {
                    cdl.countDown();
                }

            });
        }
        cdl.await();
        HttpEntity<Message> requestEnd = new HttpEntity<>(new Message(MessageType.END, "toto5", null));
        restTemplate.postForObject(restUrl, requestEnd, Message.class);


    }

}