//package com.inmyhand.refrigerator.recipe.controller;
//
//import com.inmyhand.refrigerator.recipe.service.RecipeLikeService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.ArgumentMatchers.eq;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//
//@ExtendWith(MockitoExtension.class)
//@Transactional
//class RecipeLikeControllerTest {
//
//    @Mock
//    private RecipeLikeService recipeLikeService;
//
//    @InjectMocks
//    private RecipeLikeController recipeLikeController;
//
//    private MockHttpServletRequest request;
//
//    @BeforeEach
//    public void setUp() {
//        // HTTP 요청 환경 설정
//        request = new MockHttpServletRequest();
//        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
//    }
//
//    @Test
//    public void testToggleLike_LikeAdded() {
//        // 준비
//        Long recipeId = 36L;
//        Long memberId = 2L;
//
//        // 좋아요 추가 시나리오 설정 (서비스가 true 반환)
//        when(recipeLikeService.toggleRecipeLike(eq(memberId), eq(recipeId))).thenReturn(true);
//
//        // 실행
//        ResponseEntity<Map<String, Object>> response =
//                recipeLikeController.toggleLike(recipeId, memberId);
//
//        // 검증
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//
//        // 응답 구조 검증
//        Map<String, Object> responseMap = response.getBody();
//        assertTrue(responseMap.containsKey("message"));
//
//        // 메시지 내용 검증
//        @SuppressWarnings("unchecked")
//        Map<String, Object> messageMap = (Map<String, Object>) responseMap.get("message");
//        assertTrue((Boolean) messageMap.get("liked"));
//        assertEquals("레시피를 좋아요 했습니다.", messageMap.get("message"));
//
//        // 서비스 호출 검증
//        verify(recipeLikeService).toggleRecipeLike(eq(memberId), eq(recipeId));
//    }
//
//    @Test
//    public void testToggleLike_LikeRemoved() {
//        // 준비
//        Long recipeId = 36L;
//        Long memberId = 2L;
//
//        // 좋아요 취소 시나리오 설정 (서비스가 false 반환)
//        when(recipeLikeService.toggleRecipeLike(eq(memberId), eq(recipeId))).thenReturn(false);
//
//        // 실행
//        ResponseEntity<Map<String, Object>> response =
//                recipeLikeController.toggleLike(recipeId, memberId);
//
//        // 검증
//        assertEquals(200, response.getStatusCodeValue());
//        assertNotNull(response.getBody());
//
//        // 응답 구조 검증
//        Map<String, Object> responseMap = response.getBody();
//        assertTrue(responseMap.containsKey("message"));
//
//        // 메시지 내용 검증
//        @SuppressWarnings("unchecked")
//        Map<String, Object> messageMap = (Map<String, Object>) responseMap.get("message");
//        assertFalse((Boolean) messageMap.get("liked"));
//        assertEquals("레시피 좋아요를 취소했습니다.", messageMap.get("message"));
//
//        // 서비스 호출 검증
//        verify(recipeLikeService).toggleRecipeLike(eq(memberId), eq(recipeId));
//    }
//
//    @Test
//    public void testToggleLike_ServiceThrowsException() {
//        // 준비
//        Long recipeId = 36L;
//        Long memberId = 2L;
//
//        // 서비스 호출 시 예외 발생 시뮬레이션
//        when(recipeLikeService.toggleRecipeLike(eq(memberId), eq(recipeId)))
//                .thenThrow(new RuntimeException("서비스 오류"));
//
//        // 실행 및 검증
//        assertThrows(RuntimeException.class, () ->
//                recipeLikeController.toggleLike(recipeId, memberId));
//
//        // 서비스 호출 검증
//        verify(recipeLikeService).toggleRecipeLike(eq(memberId), eq(recipeId));
//    }
//
//}