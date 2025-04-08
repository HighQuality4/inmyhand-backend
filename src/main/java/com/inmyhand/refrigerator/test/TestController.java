package com.inmyhand.refrigerator.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
public class TestController {
	
	@GetMapping("/test")
	public String test1() {
		return "/app/test/first-test";
	}
	
	@PostMapping("/api/board")
	public ResponseEntity<?> indexBoard(){
		
		Map<String, Object> setDate = new HashMap();
		// 테스트
		
		
		Map<String, String> map = new HashMap();
		map.put("title", "제목입니다");
		map.put("content","게시글입니다.");
		setDate.put("dm2", map);
		
		List<Map<String, String>> ds1 = new ArrayList();
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
		log.info("map={}",setDate.toString());
		return ResponseEntity.ok(setDate);
	}
	
	
	@RequestMapping("/test/list.do")
	@ResponseBody
	public ResponseEntity<Map<String, Object>> list(){
	    // 필요한 경우 request에서 파라미터 직접 추출
	    // 예: String paramValue = request.getParameter("paramName");
	    
	    List<Map<String, Object>> ds1 = new ArrayList<>();
	    Map<String, Object> row = new HashMap<>();

	    row.put("column1", "column44");
	    row.put("column2", "column55");
	    row.put("column3", "column66");
	    ds1.add(row);
	    
	    row = new HashMap<>();
	    row.put("column1", "column77");
	    row.put("column2", "column88");
	    row.put("column3", "column99");
	    ds1.add(row);
	    
	    Map<String, String> dm1 = new HashMap<>();
	    dm1.put("column1", "tt" + "column998877");
	    
	    // 응답 데이터를 하나의 Map에 담기
	    Map<String, Object> responseData = new HashMap<>();
	    responseData.put("ds1", ds1);
	    responseData.put("dm1", dm1);
	    
	    // HTTP 상태코드 200(OK)과 함께 응답 데이터 반환
	    return ResponseEntity.ok(responseData);
	}
	
	
	
	@PostMapping("/api/index")
	public ResponseEntity<?> test2(DataRequest dataRequest) {
		log.info("data={}", dataRequest);
		TestDTO singleClass = ConverterClassUtil.getSingleClass(dataRequest, "dm1",TestDTO.class);
		log.info("data={}",singleClass);
		return ResponseEntity.ok(singleClass);
	}



}
