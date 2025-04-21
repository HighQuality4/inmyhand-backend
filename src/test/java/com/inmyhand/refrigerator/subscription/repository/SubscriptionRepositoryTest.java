//package com.inmyhand.refrigerator.subscription.repository;
//
//import com.inmyhand.refrigerator.subscription.domain.dto.SubscriptionDetailDTO;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.*;
//@SpringBootTest
//class SubscriptionRepositoryTest {
//
//    @Autowired SubscriptionRepository subscriptionRepository;
//
//    @Test
//    void save() {
//        List<Object[]> subscriptionDetailsByMemberId = subscriptionRepository.findSubscriptionDetailsByMemberId(1L);
//        System.out.println("subscriptionDetailsByMemberId = " + subscriptionDetailsByMemberId.toString());
//    }
//
//}