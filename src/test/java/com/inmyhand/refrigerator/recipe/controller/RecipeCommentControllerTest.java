//package com.inmyhand.refrigerator.recipe.controller;
//
//import com.inmyhand.refrigerator.recipe.domain.dto.RecipeCommentEntityDto;
//import com.inmyhand.refrigerator.recipe.service.RecipeCommentService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.mock.web.MockHttpServletRequest;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//
//import java.util.Map;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//@ExtendWith(MockitoExtension.class)
//@Transactional
//class RecipeCommentControllerTest {
//
//
//    @Mock
//    private RecipeCommentService recipeCommentService;
//
//    @InjectMocks
//    private RecipeCommentController recipeCommentController;
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
//    public void testAddComment() {
//        // 준비
//        Long recipeId = 10L;
//        Long memberId = 2L;
//        RecipeCommentEntityDto commentDto = new RecipeCommentEntityDto();
//        commentDto.setCommentContents("테스트 댓글 내용");
//
//        // 실행
//        ResponseEntity<Map<String, String>> response =
//                recipeCommentController.addComment(recipeId, commentDto, memberId);
//
//        // 검증
//        assertEquals(HttpStatus.CREATED, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("댓글이 성공적으로 등록되었습니다.", response.getBody().get("message"));
//
//        // 서비스 메소드가 올바른 파라미터로 호출되었는지 검증
//        verify(recipeCommentService).addComment(
//                eq("테스트 댓글 내용"),
//                eq(memberId),
//                eq(recipeId)
//        );
//    }
//
//    @Test
//    public void testDeleteComment() {
//        // 준비
//        Long commentId = 3L;
//
//        // 실행
//        ResponseEntity<Map<String, String>> response =
//                recipeCommentController.deleteComment(commentId);
//
//        // 검증
//        assertEquals(HttpStatus.OK, response.getStatusCode());
//        assertNotNull(response.getBody());
//        assertEquals("댓글이 삭제되었습니다.", response.getBody().get("message"));
//
//        // 서비스 메소드가 올바른 파라미터로 호출되었는지 검증
//        verify(recipeCommentService).deleteComment(eq(commentId));
//    }
//
//    @Test
//    public void testAddComment_ServiceThrowsException() {
//        // 준비
//        Long recipeId = 10L;
//        Long memberId = 2L;
//        RecipeCommentEntityDto commentDto = new RecipeCommentEntityDto();
//        commentDto.setCommentContents("테스트 댓글 내용");
//
//        // 서비스 호출 시 예외 발생 시뮬레이션
//        doThrow(new RuntimeException("서비스 오류")).when(recipeCommentService)
//                .addComment(anyString(), anyLong(), anyLong());
//
//        // 실행 및 검증
//        assertThrows(RuntimeException.class, () ->
//                recipeCommentController.addComment(recipeId, commentDto, memberId));
//    }
//
//    @Test
//    public void testDeleteComment_ServiceThrowsException() {
//        // 준비
//        Long commentId = 3L;
//
//        // 서비스 호출 시 예외 발생 시뮬레이션
//        doThrow(new RuntimeException("서비스 오류")).when(recipeCommentService)
//                .deleteComment(anyLong());
//
//        // 실행 및 검증
//        assertThrows(RuntimeException.class, () ->
//                recipeCommentController.deleteComment(commentId));
//    }
//}