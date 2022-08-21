package com.miroku.junit.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miroku.junit.domain.Book;
import com.miroku.junit.domain.BookRepository;
import com.miroku.junit.util.MailSender;
import com.miroku.junit.web.dto.BookRespDto;
import com.miroku.junit.web.dto.BookSaveReqDto;

@ExtendWith(MockitoExtension.class)
public class BookServiceTest {
    
    @InjectMocks
    private BookService bookService;

    @Mock
    private BookRepository bookRepository;

    @Mock
    private MailSender mailSender;

    @Test
    public void 책등록하기_테스트() {
        // given
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("title");
        dto.setAuthor("author");

        // stub (가설)
        when(bookRepository.save(any())).thenReturn(dto.toEntity());
        // when(mailSender.send()).thenReturn(true);

        // when
        BookRespDto bookRespDto = bookService.책등록하기(dto);

        // then
        // assertEquals(dto.getAuthor(), bookRespDto.getAuthor());
        // assertEquals(dto.getTitle(), bookRespDto.getTitle());
        assertThat(dto.getTitle()).isEqualTo(bookRespDto.getTitle());
        assertThat(dto.getAuthor()).isEqualTo(bookRespDto.getAuthor());
    }

    @Test
    public void 책목록보기_테스트() {
        //given
        List<Book> books = new ArrayList<>();
        books.add(new Book(1L, "1.title", "1.author"));
        books.add(new Book(2L, "2.title", "2.author"));

        //stub
        when(bookRepository.findAll()).thenReturn(books);

        //when
        List<BookRespDto> dtos = bookService.책목록보기();

        //then
        assertThat(dtos.get(0).getTitle()).isEqualTo("1.title");
        assertThat(dtos.get(0).getAuthor()).isEqualTo("1.author");
        assertThat(dtos.get(1).getTitle()).isEqualTo("2.title");
        assertThat(dtos.get(1).getAuthor()).isEqualTo("2.author");
    }

    @Test
    public void 책한건보기_테스트() {
        // given
        Long id = 1L;
        Book book = new Book(1L, "title", "author");
        Optional<Book> bookOP = Optional.of(book);

        //stub
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto bookRespDto = bookService.책한건보기(id);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());

    }
}
