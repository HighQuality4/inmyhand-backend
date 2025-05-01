package com.inmyhand.refrigerator.recipe.controller;

import static org.junit.jupiter.api.Assertions.*;

import com.inmyhand.refrigerator.recipe.service.RecipeViewService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecipeViewControllerTest {


    @Mock
    private RecipeViewService recipeViewService;

    @InjectMocks
    private RecipeViewController recipeViewController;

    private MockHttpServletRequest request;

    @BeforeEach
    public void setUp() {
        // HTTP 요청 환경 설정
        request = new MockHttpServletRequest();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
    }

    @Test
    public void testAddRecipeView_Success() {
        // 준비
        Long recipeId = 54L;
        Long memberId = 2L;

        // 서비스가 예외를 던지지 않도록 설정 (void 메서드)
        doNothing().when(recipeViewService).addRecipeView(eq(memberId), eq(recipeId));

        // 실행
        ResponseEntity<?> response = recipeViewController.addRecipeView(memberId, recipeId);

        // 검증
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody()); // 응답 본문이 비어있어야 함

        // 서비스 메서드가 올바른 파라미터로 호출되었는지 검증
        verify(recipeViewService).addRecipeView(eq(memberId), eq(recipeId));
    }

    @Test
    public void testAddRecipeView_ServiceThrowsException() {
        // 준비
        Long recipeId = 54L;
        Long memberId = 2L;

        // 서비스 호출 시 예외 발생 시뮬레이션
        doThrow(new RuntimeException("서비스 오류")).when(recipeViewService)
                .addRecipeView(eq(memberId), eq(recipeId));

        // 실행 및 검증
        assertThrows(RuntimeException.class, () ->
                recipeViewController.addRecipeView(memberId, recipeId));

        // 서비스 메서드가 올바른 파라미터로 호출되었는지 검증
        verify(recipeViewService).addRecipeView(eq(memberId), eq(recipeId));
    }

    @Test
    public void testAddRecipeView_WithInvalidParams() {
        // 준비 - 유효하지 않은 매개변수 (null)
        Long recipeId = null;
        Long memberId = 2L;

        // 서비스에서 null 값으로 호출 시 NullPointerException이 발생하도록 설정
        doThrow(NullPointerException.class).when(recipeViewService).addRecipeView(eq(memberId), eq(null));

        // 실행 및 검증
        assertThrows(NullPointerException.class, () ->
                recipeViewController.addRecipeView(memberId, recipeId));

        // 서비스 호출 확인
        verify(recipeViewService).addRecipeView(eq(memberId), eq(null));
    }

}