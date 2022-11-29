package com.example.javadb.controller;

import com.example.javadb.model.Quote;
import com.example.javadb.repository.QuoteRepository;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class QuoteController {
    @Autowired
    private static final Logger LOG = LoggerFactory.getLogger(QuoteController.class);
    private final String ACCOUNT_SID = System.getenv("TWILIO_ACCOUNT_SID");
    private final String AUTH_TOKEN = System.getenv("TWILIO_AUTH_TOKEN");
    private final String TWILIO_MESSAGING_SERVICE_SID = System.getenv("TWILIO_MESSAGING_SERVICE_SID");
    private final String PHONE_NUMBER = System.getenv("PHONE_NUMBER");

    // constructor
    public QuoteController(QuoteRepository quoteRepository) {
        this.quoteRepository = quoteRepository;
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
    }
    private final QuoteRepository quoteRepository;

    @GetMapping("/quotes")
    public List<Quote> getQuotes(@RequestParam("search") Optional<String> searchParam){
        return searchParam
                .map(quoteRepository::getContainingQuote)
                .orElse(quoteRepository.findAll());
    }

    @GetMapping("/quotes/{quoteId}" )
    public ResponseEntity<String> readQuote(@PathVariable("quoteId") Long id) {
        return ResponseEntity.of(quoteRepository.findById(id).map( Quote::getQuote ));
    }

    @PostMapping("/quotes")
    public Quote addQuote(@RequestBody String quote) {
        Quote q = new Quote();
        q.setQuote(quote);
        return quoteRepository.save(q);
    }

    @RequestMapping(value="/quotes/{quoteId}", method=RequestMethod.DELETE)
    public void deleteQuote(@PathVariable(value = "quoteId") Long id) {
        quoteRepository.deleteById(id);
    }

    @GetMapping(value = "/sms")
    @ResponseBody
    public String sendSMS() {
        long min = 6;
        long max = 12;
        long random = (long)(Math.random()*(max-min+1)+min);

        Quote quote = quoteRepository.findById(random).get();
        String returnString = quote.getQuote();
        Message message = Message.creator(
                        new com.twilio.type.PhoneNumber(PHONE_NUMBER),
                        TWILIO_MESSAGING_SERVICE_SID,
                        returnString)
                .create();
        LOG.info("Message SID is {}", message.getSid());
        return message.getSid() + " sent successfully";
    }
}
