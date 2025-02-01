package com.demo.cashcards;

import java.net.URI;
import java.util.ArrayList;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.CurrentSecurityContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/cashcards")
public class CashCardController {

    private CashCardRepository cardRepository;
    
    public CashCardController(CashCardRepository cardRepository){
        this.cardRepository = cardRepository;
    }

    @GetMapping("/{requestedId}")
    private ResponseEntity<CashCard> findById(@PathVariable Long requestedId){
        return this.cardRepository.findById(requestedId)
        .map(ResponseEntity::ok)
        .orElseGet(()-> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    private ResponseEntity<Void> createCashCard(@RequestBody CashCard cashCardRequest,UriComponentsBuilder ucb){
        CashCard savedCashCard = cardRepository.save(cashCardRequest);
        URI locationOfCashCard = ucb.path("cashcards/{id}")
                                    .buildAndExpand(savedCashCard.id()).toUri();
        return ResponseEntity.created(locationOfCashCard).build();
    }

    @GetMapping()
    private ResponseEntity<Iterable<CashCard>> findAll(@CurrentSecurityContext(expression = "authentication") Authentication authentication){
        // var filtered = new ArrayList<CashCard>();
        // this.cardRepository.findAll().forEach(
        //     cashCard ->{
        //         if(cashCard.owner().equals(authentication.getName())){
        //             filtered.add(cashCard);
        //         }
        //     }
        // );
        return ResponseEntity.ok(this.cardRepository.findByOwner(authentication.getName()));
    }
}
