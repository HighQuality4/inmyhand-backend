//package com.inmyhand.refrigerator;
//
//import com.google.cloud.vertexai.VertexAI;
//import com.google.cloud.vertexai.api.GenerateContentResponse;
//import com.google.cloud.vertexai.generativeai.GenerativeModel;
//import com.google.cloud.vertexai.generativeai.ResponseHandler;
//import com.inmyhand.refrigerator.fridge.service.ocr.ReceiptExtraction;
//import org.junit.jupiter.api.Test;
//import org.springframework.ai.chat.model.ChatResponse;
//import org.springframework.ai.chat.prompt.Prompt;
//import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.mock.web.MockMultipartFile;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.*;
//import java.util.ArrayList;
//import java.util.List;
//
//@SpringBootTest
//public class sardTest {
//
//
//    @Autowired
//    VertexAiGeminiChatModel chatModel;
//
//    @Autowired
//    ReceiptExtraction receiptExtraction;
//
//    @Test
//    void asd() throws IOException {
//
//
////        File file = new File("/Users/yeongbee/Final-Project/영수증/images1.jpeg");
//        File file2 = new File("/Users/yeongbee/Final-Project/영수증/images2.jpeg");
//
//        // 파일을 읽어 InputStream을 생성합니다.
////        InputStream inputStream = new FileInputStream(file);
////        MultipartFile multipartFile = new MockMultipartFile(
////                "file",
////                file.getName(),
////                "image/jpeg",
////                inputStream
////        );
//        InputStream inputStream = new FileInputStream(file2);
//
//        inputStream = new FileInputStream(file2);
//        MultipartFile multipartFile2 = new MockMultipartFile(
//                "file2",
//                file2.getName(),
//                "image/jpeg",
//                inputStream
//        );
//
//        List<MultipartFile> lists = new ArrayList<>();
////        lists.add(multipartFile);
//        lists.add(multipartFile2);
//
//
////        System.out.println("MultipartFile name: " + multipartFile.getName());
////        System.out.println("MultipartFile original filename: " + multipartFile.getOriginalFilename());
////        List<ReceiptDTO> receiptDTOS = receiptExtraction.extractReceiptData(multipartFile);
//
//        System.out.println("receiptExtraction.extractReceiptData(lists) = " + receiptExtraction.extractReceiptData(lists));
////       receiptExtraction.extractReceiptData(lists).forEach(receipt -> {
////           System.out.println("receipt = " + receipt);
////       });
//
////        System.out.println("receiptDTO = " + receiptDTOS.toString());
//    }
//
//
//    @Test
//    public void sendPrompt() {
//
//        String textPrompt = "안녕";
//        String projectId = "myapp2-452808";
//        String location = "us-central1";
//        String modelName = "gemini-2.0-flash";
//
//        try (VertexAI vertexAI = new VertexAI(projectId, location)) {
//            GenerativeModel model = new GenerativeModel(modelName, vertexAI);
//            GenerateContentResponse response = model.generateContent(textPrompt);
//            System.out.println("response = " + ResponseHandler.getText(response));
//        } catch (IOException e) {
//            String errorMessage = "Vertex AI 호출 중 오류 발생: " + e.getMessage();
//            throw new RuntimeException(errorMessage);
//        }
//    }
//
//    @Test
//    public void sendPrompt222() {
//        String textPrompt = "자기 소개를 해주세요";
//        try {
//            ChatResponse response = chatModel.call(new Prompt(textPrompt));
//            String text = response.getResult().getOutput().getText();
//            System.out.println("getText = " + text);
//        } catch (Exception e) {
//            String errorMessage = "Vertex AI 호출 중 오류 발생: " + e.getMessage();
//            throw new RuntimeException(errorMessage, e);
//        }
//    }
//}
