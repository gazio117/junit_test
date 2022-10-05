package com.miroku.junit.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.miroku.junit.service.BookService;
import com.miroku.junit.web.dto.request.BookSaveReqDto;
import com.miroku.junit.web.dto.response.BookListRespDto;
import com.miroku.junit.web.dto.response.BookRespDto;
import com.miroku.junit.web.dto.response.CMRespDto;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RestController
public class BookApiController {

    private final BookService bookService;
    
    // 1. 책등록
    @PostMapping("/api/v1/book")
    public ResponseEntity<?> saveBook(@RequestBody @Valid BookSaveReqDto bookSaveReqDto, BindingResult bindingResult) {
        
        if (bindingResult.hasErrors()) {
            Map<String, String> errorMap = new HashMap<>();
            for (FieldError fe : bindingResult.getFieldErrors()) {
                errorMap.put(fe.getField(), fe.getDefaultMessage());
            }
            throw new RuntimeException(errorMap.toString());
        }
        BookRespDto bookRepsDto = bookService.책등록하기(bookSaveReqDto);
        CMRespDto<?> cmRespDto = CMRespDto.builder().code(1).msg("글 저장 성공").body(bookRepsDto).build();
        return new ResponseEntity<>(cmRespDto, HttpStatus.CREATED);
    }

    // 2. 책목록보기
    @GetMapping("/api/v1/book")
    public ResponseEntity<?> getBookList() {
        BookListRespDto bookListRespDto = bookService.책목록보기();
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 목록보기 성공").body(bookListRespDto).build(), HttpStatus.OK);
    }
    
    // 3. 책한건보기
    @GetMapping("api/v1/book/{id}")
    public ResponseEntity<?> getBookOne(@PathVariable Long id) {
        BookRespDto bookRespDto = bookService.책한건보기(id);
        return new ResponseEntity<>(CMRespDto.builder().code(1).msg("글 한건보기 성공").body(bookRespDto).build(), HttpStatus.OK);
    }
    
    // 4. 책삭제
    public ResponseEntity<?> deleteBook() {
        return null;
    }

    // 5. 책수정
    public ResponseEntity<?> updateBook() {
        return null;
    }
}
