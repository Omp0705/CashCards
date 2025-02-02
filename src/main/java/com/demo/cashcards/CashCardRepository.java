package com.demo.cashcards;

import org.springframework.data.repository.CrudRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;


public interface CashCardRepository extends CrudRepository<CashCard,Long> {

    Iterable<CashCard> findByOwner(String owner);

    default Iterable<CashCard> finAll(){
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String owner  = authentication.getName();
        return findByOwner(owner);
    }
} 
