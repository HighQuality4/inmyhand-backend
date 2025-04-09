package com.inmyhand.refrigerator.test;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cleopatra.spring.CommonsMultipartFile;
import com.inmyhand.refrigerator.fridge.domain.dto.ReceiptDTO;
import com.inmyhand.refrigerator.fridge.service.ReceiptExtraction;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.cleopatra.protocol.data.DataRequest;
import com.inmyhand.refrigerator.util.ConverterClassUtil;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class TestController {

	private final ReceiptExtraction receiptExtraction;
	
	@GetMapping("/test")
	public String test1() {
		return "/app/test/first-test";
	}


	@GetMapping("/test/datarequest")
	@ResponseBody
	public ResponseEntity<?> test2() {

		return ResponseEntity.ok(Map.of("as", "asd"));

	}
	
	@PostMapping("/api/board")
	public ResponseEntity<?> indexBoard(){
		
		Map<String, Object> setDate = new HashMap<>();
		// 테스트2

		Map<String, String> map = new HashMap<>();
		map.put("title", "제목입니다");
		map.put("content","게시글입니다.");
		setDate.put("dm2", map);
		
		List<Map<String, String>> ds1 = new ArrayList<>();
		map = new HashMap<>();
		map.put("s1", "111");
		map.put("s2", "222");
		map.put("s3", "333");
		ds1.add(map);
		map = new HashMap<>();
		map.put("s1", "444");
		map.put("s2", "555");
		map.put("s3", "666");
		ds1.add(map);
		setDate.put("ds1", ds1);
		log.info("map={}",setDate);
		return ResponseEntity.ok(setDate);
	}
	

//	@GetMapping("/api/aitest")
//	@ResponseBody
//	public ResponseEntity<?> test3() throws Exception {
//		        File image = new File("/Users/yeongbee/Final-Project/영수증/images1.jpeg");
//		CommonsMultipartFile commonsMultipartFile = receiptExtraction.convertFileToMultipart(image);
//		ReceiptDTO receiptDTO = receiptExtraction.extractReceiptData(commonsMultipartFile);
//
//        log.info("receiptDTO: {}", receiptDTO);
//        System.out.println("MultipartFile 이름: " + receiptDTO.toString());
//
//		return ResponseEntity.ok(receiptDTO);
//	}

	
	
	
	@PostMapping("/api/index")
	public ResponseEntity<?> test2(DataRequest dataRequest) {
		log.info("data={}", dataRequest);
		TestDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "dm1",TestDTO.class);
		log.info("data={}",singleClass);
		return ResponseEntity.ok(singleClass);
	}



}
