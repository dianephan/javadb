package com.example.javadb;

import com.example.javadb.controller.QuoteController;
import com.example.javadb.model.Quote;
import com.example.javadb.repository.QuoteRepository;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Optional;

public class ControllerTest {

    @Test
    public void getQuotesTest(){
        // set up
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController quoteController = new QuoteController(mockRepository);
        List<Quote> allQuotes = (List<Quote>) List.of(new Quote());
        Mockito.when(mockRepository.findAll()).thenReturn(
                allQuotes
        );

        // call getQuotes function from quoteController class
        List<Quote> quotes = quoteController.getQuotes(Optional.empty());
        assertEquals(allQuotes, quotes);      // Optional.of("word") --> fail bc get another quotes list
        Mockito.verify(mockRepository).findAll();         // function doesnt work
    }

    @Test
    // make test for @GetMapping("/todo/{id}")
    public void testThatInvalidIdProducesA404(){
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController mockController = new QuoteController(mockRepository);
        ResponseEntity<String> mockItem = mockController.readQuote(100000L);
        assertEquals(404, mockItem.getStatusCodeValue());
    }

    @Test
    public void testThatValidIdProducesA200(){
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        Quote idQuote = new Quote();
        Mockito.when(mockRepository.getReferenceById(1L)).thenReturn(
                "Quote phrase here"
        );
        QuoteController mockController = new QuoteController(mockRepository);

        ResponseEntity<String> mockItem = mockController.readQuote(1L);

        assertEquals(200, mockItem.getStatusCodeValue());
        assertEquals("Quote phrase here", mockItem.getBody() );
    }

    @Test
    public void testToAddQuote(){
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController quoteController = new QuoteController(mockRepository);

        List<Quote> allQuotes = (List<Quote>) List.of(new Quote());
        quoteController.addQuote("blah fsfs");
        Mockito.when(mockRepository.findAll()).thenReturn(
                allQuotes
        );

        quoteController.addQuote("blah fsfs");
        List<Quote> quotes = quoteController.getQuotes();
        assertEquals(allQuotes, quotes);      // Optional.of("word") --> fail bc get another quotes list
    }

    // ????
    @Test
    public void getContainingQuoteTest(){
        // set up
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController quoteController = new QuoteController(mockRepository);
        List<Quote> allQuotes = (List<Quote>) List.of(new Quote());
//        Mockito.when(mockRepository.findAll().contains("blah")).thenReturn(
//                allQuotes
//        );

        // call getContainingQuote
        List<Quote> quotes = mockRepository.getContainingQuote("blah");
        assertEquals(allQuotes, quotes);
    }


}