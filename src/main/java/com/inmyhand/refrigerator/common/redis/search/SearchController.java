package com.inmyhand.refrigerator.common.redis.search;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/recipe/search")
@RequiredArgsConstructor
@Slf4j
public class SearchController {
    
    private final PopularSearchService popularSearchService;
    
    /**
     * 검색 요청 처리 및 검색어 등록
     */
    @GetMapping("/init")
    public ResponseEntity<?> search(@RequestParam("keyword") String keyword) {
        // 검색어 등록
        popularSearchService.registerSearchKeyword(keyword);
        
        // 여기에 실제 검색 로직 추가...
        
        return ResponseEntity.ok().build();
    }
    
    /**
     * 인기 검색어 목록 조회
     */
    @GetMapping("/popular")
    public ResponseEntity<?> getPopularSearches() {
        List<PopularSearchDto> popularSearches = popularSearchService.getPopularSearches();
//        log.info("Popular searches: {}", Map.of("PopularSearch", popularSearches.toString()));
        return ResponseEntity.ok(Map.of("popularSearch", popularSearches));
    }
    
    /**
     * 특정 날짜의 인기 검색어 목록 조회
     */
    @GetMapping("/popular/date")
    public ResponseEntity<List<PopularSearchDto>> getPopularSearchesByDate(
            @RequestParam int year,
            @RequestParam int month,
            @RequestParam int day) {
        
        LocalDateTime dateTime = LocalDateTime.of(year, month, day, 0, 0);
        List<PopularSearchDto> popularSearches = popularSearchService.getPopularSearchesByDate(dateTime);
        
        return ResponseEntity.ok(popularSearches);
    }
}
