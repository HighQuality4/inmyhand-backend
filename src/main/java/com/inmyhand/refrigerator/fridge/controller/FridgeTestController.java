package com.inmyhand.refrigerator.fridge.controller;

import com.cleopatra.protocol.data.DataRequest;
import com.cleopatra.protocol.data.ParameterGroup;
import com.inmyhand.refrigerator.fridge.domain.dto.food.FridgeFoodDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


//TODO
//@RestController
public class FridgeTestController {


    // 응답 데이터 예시
    // 서버 -> 클라이언트 : 응답데이터 확인 (고객에게 답장, 응답)
    @GetMapping
    public ResponseEntity<Void> sendListData(DataRequest  dataRequest){
        //        데이터 셋
        List<Map<String, Object>> data1 = new ArrayList<Map<String, Object>>();

        Map<String, Object> mapData1 = new HashMap<String, Object>();
        mapData1.put("column1", "test1");
        mapData1.put("column2", "test2");

        Map<String, Object> mapData2 = new HashMap<String, Object>();
        mapData2.put("column1", "test3");
        mapData2.put("column2", "test4");

        data1.add(mapData1);
        data1.add(mapData2);
        // 응답데이터 등록
        dataRequest.setResponse("ds1", data1);

        //----------------------------------------------------------------------
        // 데이터 맵
        Map<String, Object> data2 = new HashMap<String, Object>();
        data2.put("column1", "test5");
        data2.put("column2", "test6");

        // 응답데이터 등록
        dataRequest.setResponse("dm1", data2);
        return   ResponseEntity.status(HttpStatus.CREATED).build();
    }


    // 클라이언트 -> 서버 (고객이 요청)
    @PostMapping
    public ResponseEntity<Void> getListData(DataRequest  dataRequest) {
        // 요청 데이터
        ParameterGroup dsList = dataRequest.getParameterGroup("ds1");
        ParameterGroup dm1 = dataRequest.getParameterGroup("dm1");

        return   ResponseEntity.status(HttpStatus.CREATED).build();
    }


    @PostMapping("/api/board")
    public ResponseEntity<?> indexBoard(){

        Map<String, Object> setDate = new HashMap<>();
        // 테스트2

        Map<String, String> map = new HashMap<>();


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

        return ResponseEntity.ok(setDate);
    }

}
