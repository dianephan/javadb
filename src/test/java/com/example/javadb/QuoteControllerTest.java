package com.example.javadb;

import com.example.javadb.controller.QuoteController;
import com.example.javadb.model.Quote;
import com.example.javadb.repository.QuoteRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.http.ResponseEntity;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class QuoteControllerTest {

    @Test
    // make test for @GetMapping("/todo/{id}")
    public void testThatInvalidIdProducesA404(){
        // SETUP
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);

        Mockito.when(mockRepository.findById(100000L)).thenReturn(Optional.empty());
        QuoteController quoteController = new QuoteController(mockRepository);

        // CALL
        ResponseEntity<String> responseEntity = quoteController.readQuote(100000L);

        // ASSERTIONS
        assertEquals(404, responseEntity.getStatusCodeValue());
        assertNull( responseEntity.getBody() );
    }

    @Test
    public void testThatValidIdProducesA200(){

        // SETUP
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);

        Quote idQuote = new Quote();
        idQuote.setQuote("Quote phrase here");

        Mockito.when(mockRepository.findById(1L)).thenReturn(Optional.of(idQuote));
        QuoteController quoteController = new QuoteController(mockRepository);

        // CALL
        ResponseEntity<String> responseEntity = quoteController.readQuote(1L);

        // ASSERTIONS
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals("Quote phrase here", responseEntity.getBody() );
    }

    @Test
    public void testToDeleteQuote(){
        // SETUP
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController quoteController = new QuoteController(mockRepository);

        // CALL
        quoteController.deleteQuote(1L);

        // ASSERTIONS
        Mockito.verify(mockRepository).deleteById(1L);
    }

    @Test
    public void testToAddQuote(){
        // SETUP
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController quoteController = new QuoteController(mockRepository);

        Quote savedQuote = new Quote();
        savedQuote.setQuote("Quote phrase here");
        ArgumentCaptor<Quote> quoteCaptor = ArgumentCaptor.forClass(Quote.class);
        Mockito.when(mockRepository.save(quoteCaptor.capture())).thenReturn(savedQuote);
        // CALL
        Quote quote = quoteController.addQuote("Quote phrase here");

        // ASSERTIONS
        assertEquals("Quote phrase here", quote.getQuote());
        // does `quote` have the right content?
    }

    @Test
    public void getQuotesWithoutSearchParameterTest(){
        // SETUP
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController quoteController = new QuoteController(mockRepository);

        List<Quote> allQuotes = List.of(new Quote());
        Mockito.when(mockRepository.findAll()).thenReturn(allQuotes);

        // CALL
        List<Quote> quotes = quoteController.getQuotes(Optional.empty());

        // ASSERTIONS
        assertEquals(allQuotes, quotes);      // Optional.of("word") --> fail bc get another quotes list
        Mockito.verify(mockRepository).findAll();
        Mockito.verifyNoMoreInteractions(mockRepository);
    }

    @Test
    public void getContainingQuoteTest(){
        // SETUP
        QuoteRepository mockRepository = Mockito.mock(QuoteRepository.class);
        QuoteController quoteController = new QuoteController(mockRepository);

        List<Quote> containingQuotes = Collections.emptyList();
        Mockito.when(mockRepository.getContainingQuote("blah")).thenReturn(containingQuotes);

        // CALL
        List<Quote> quotes = quoteController.getQuotes(Optional.of("blah"));

        // ASSERTIONS
        assertEquals(containingQuotes, quotes);
        Mockito.verify(mockRepository).getContainingQuote("blah");
    }
    // did not need to put in phrases, only check if "getContainingQuote" function was called
    // and that the list it returned was the same list that "getQuotes" returned
}