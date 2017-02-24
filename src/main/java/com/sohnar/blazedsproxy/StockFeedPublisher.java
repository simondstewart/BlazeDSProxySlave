package com.sohnar.blazedsproxy;

import java.math.BigDecimal;
import java.util.Random;

import org.graniteds.tutorial.feed.entities.StockPrice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import flex.messaging.messages.AsyncMessage;

@Component
public class StockFeedPublisher {

    private static final String[] STOCKS = { "AAPL", "GOOG", "MSFT", "AMZN" };

    @Autowired
    private JmsTemplate jmsTemplate;
    
    private StockPrice[] stockPrices = new StockPrice[STOCKS.length];

    private Random random = new Random(System.currentTimeMillis());

    public StockFeedPublisher() {
        for (int i = 0; i < stockPrices.length; i++)
            stockPrices[i] = new StockPrice(STOCKS[i], new BigDecimal(50.0 + random.nextDouble()*100.0));
    }

    // tag::server-publish[]
//    @Scheduled(fixedRate=100000)
    public void publish() {
        for (int i = 0; i < stockPrices.length; i++) {
            double inc = random.nextDouble()-0.5;
            stockPrices[i].setPrice(stockPrices[i].getPrice().add(new BigDecimal(inc)));
        }

//        AsyncMessage message = new AsyncMessage(); // <1>
//        message.setDestination("feedTopic");
//        message.setHeader(AsyncMessage.SUBTOPIC_HEADER, "NASDAQ");
//        message.setBody(stockPrices);
//        gravity.publishMessage(message);
        
        jmsTemplate.convertAndSend("feedTopic.NASDAQ", stockPrices);
    }
    // end::server-publish[]
}
