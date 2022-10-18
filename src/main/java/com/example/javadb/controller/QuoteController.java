package com.example.javadb.controller;

import com.example.javadb.model.Quote;
import com.example.javadb.repository.QuoteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class QuoteController {
    @Autowired
    private QuoteRepository quoteRepository;

    @GetMapping("/quotes")
//    Quotes?search=friend
    // optional = value might or might ont be there
    public List<Quote> getQuotes(@RequestParam("search") Optional<String> searchParam){
//        return quoteRepository.findAll();
        return searchParam.map( param->quoteRepository.getContainingQuote(param) )
                .orElse(quoteRepository.findAll());
    }

    @GetMapping("/quotes/{quoteId}" )
    public ResponseEntity<String> readQuote(@PathVariable("quoteId") Long id) {
//        used optionals :: - either get Quote or if null return "random quote" to prevent null ptr exceptions
//        return quoteRepository.findById(id).map(Quote::getQuote).orElse("random quote");

//        return quoteRepository.findById(id)
//                .map(quote -> ResponseEntity.ok(quote.getQuote()))
//                .orElse(ResponseEntity.notFound().build());         // notFound returns a builder so add .build()
        return ResponseEntity.of(quoteRepository.findById(id).map(Quote::getQuote));
// equivalent to first comment (.of is written for us)
// .of is static method / common name to handle this situation. creates objects for u
    }

    @PostMapping("/quotes")
    public Quote addQuote(@RequestBody String quote) {
        Quote q = new Quote();
        q.setQuote(quote);
        return quoteRepository.save(q);
    }

}
