package com.inmyhand.refrigerator.payment.controller;

import com.inmyhand.refrigerator.payment.domain.dto.PaymentConfirmRequest;
import com.inmyhand.refrigerator.payment.domain.dto.PaymentConfirmResponse;
import com.inmyhand.refrigerator.payment.service.PaymentServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;
import org.springframework.web.servlet.ModelAndView;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;

@Controller
@Slf4j
@RequiredArgsConstructor
@RequestMapping("/payment")
public class TossController {

    @Value("${toss.payments.secret-key}")
    private String widgetSecretKey;

    @Value("${toss.payments.confirm-url}")
    private String confirmUrl;


    private final PaymentServiceImpl paymentService;

    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmRequest request){
//        log.info("결제 승인 요청: {}", request);

        String authorizations = "Basic " + Base64.getEncoder().encodeToString(
                (widgetSecretKey + ":").getBytes(StandardCharsets.UTF_8));

        try {
            PaymentConfirmResponse response = RestClient.create()
                    .post()
                    .uri(confirmUrl)
                    .header("Authorization", authorizations)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(request)
                    .retrieve()
                    .onStatus(status -> !status.is2xxSuccessful(), (req, res) -> {
                        // 에러 응답 처리
                        log.error("결제 승인 에러: {}", res.getStatusCode());
                        log.error("결제 승인 에러: {}", res.getStatusText());
                    })
                    .body(PaymentConfirmResponse.class);

            log.info("결제 승인 응답: {}", response);

            //TODO, 2번째 파라미터 MemberId 가 들어감 - @최성관
            paymentService.savePaymentFromConfirmation(response, 2L);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("결제 승인 처리 중 오류 발생", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    /**
     * 인증성공처리
     */
    @GetMapping("/success")
    public ModelAndView paymentSuccess( @ModelAttribute PaymentConfirmRequest paymentConfirmRequest) {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/view/app/payment/success.html");
        return modelAndView;
    }

    /**
     * 메인 페이지
     */
    @GetMapping("/toss")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("forward:/view/app/payment/checkout.html");
        return modelAndView;
    }

    /**
     * 인증실패처리
     */
    @GetMapping("/fail")
    public ModelAndView failPayment(HttpServletRequest request) {
        String failCode = request.getParameter("code");
        String failMessage = request.getParameter("message");

        ModelAndView modelAndView = new ModelAndView();
        modelAndView.addObject("code", failCode);
        modelAndView.addObject("message", failMessage);
        modelAndView.setViewName("forward:/view/app/payment/fail.html");
        return modelAndView;
    }

    @GetMapping("/findall")
    @ResponseBody
    public ResponseEntity<?> findAll() {
        return ResponseEntity.ok(Map.of("findall", paymentService.getAllSubscriptions()));
    }

    @GetMapping("/finduser")
    @ResponseBody
    public ResponseEntity<?> findSubUserId() {
        //TODO Member Id를 @PathVariable or @RequestParam, DataRequest 을 사용해서  MemberId를 받으세요~~~
        //TODO 그리고 아래에 2L 대신 넣으세요~~~
        //TODO 대답.
        return ResponseEntity.ok(Map.of("finduser", paymentService.getSubscriptionsByMemberId(2L)));
    }


}
