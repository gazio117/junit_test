package com.miroku.junit.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.miroku.junit.domain.Book;
import com.miroku.junit.domain.BookRepository;
import com.miroku.junit.web.dto.request.BookSaveReqDto;
import com.miroku.junit.web.dto.response.BookRespDto;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BookService {
    
    private final BookRepository bookRepository;
    
    // 1. 책등록
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto 책등록하기(BookSaveReqDto dto) {
        Book bookPS = bookRepository.save(dto.toEntity());
        return bookPS.toDto();
    }

    // 2. 책목록보기
    public List<BookRespDto> 책목록보기() {
        return bookRepository.findAll().stream()
                                                    // .map((bookPS) -> new BookRespDto().toDto(bookPS))
                                                    .map(Book::toDto)
                                                    .collect(Collectors.toList());
    }

    // 3. 책한건보기
    public BookRespDto 책한건보기(Long id) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if (bookOP.isPresent()) {
            Book bookPS = bookOP.get();
            return bookPS.toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }

    // 4. 책삭제
    @Transactional(rollbackFor = RuntimeException.class)
    public void 책삭제하기(Long id) {
        bookRepository.deleteById(id);
    }

    // 5. 책수정
    @Transactional(rollbackFor = RuntimeException.class)
    public BookRespDto 책수정하기(Long id, BookSaveReqDto dto) {
        Optional<Book> bookOP = bookRepository.findById(id);
        if (bookOP.isPresent()) {
            Book bookPS = bookOP.get();
            bookPS.update(dto.getTitle(), dto.getAuthor());
            return bookPS.toDto();
        } else {
            throw new RuntimeException("해당 아이디를 찾을 수 없습니다.");
        }
    }
}
