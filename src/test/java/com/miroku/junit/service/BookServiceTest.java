package com.miroku.junit.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.miroku.junit.domain.Book;
import com.miroku.junit.domain.BookRepository;
import com.miroku.junit.util.MailSender;
import com.miroku.junit.web.dto.request.BookSaveReqDto;
import com.miroku.junit.web.dto.response.BookListRespDto;
import com.miroku.junit.web.dto.response.BookRespDto;

@ActiveProfiles("dev")
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
        BookListRespDto dtos = bookService.책목록보기();

        //then
        assertThat(dtos.getItems().get(0).getTitle()).isEqualTo("1.title");
        assertThat(dtos.getItems().get(0).getAuthor()).isEqualTo("1.author");
        assertThat(dtos.getItems().get(1).getTitle()).isEqualTo("2.title");
        assertThat(dtos.getItems().get(1).getAuthor()).isEqualTo("2.author");
    }

    @Test
    public void 책한건보기_테스트() {
        // given
        
        //stub
        Long id = 1L;
        Book book = new Book(1L, "title", "author");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto bookRespDto = bookService.책한건보기(id);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(book.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(book.getAuthor());

    }
    
    @Test
    public void 책수정하기_테스트() {
        // given
        Long id = 1L;
        BookSaveReqDto dto = new BookSaveReqDto();
        dto.setTitle("변경타이틀");
        dto.setAuthor("변경작성자");
        
        // stub
        Book book = new Book(1L, "원작타이틀", "원작작성자");
        Optional<Book> bookOP = Optional.of(book);
        when(bookRepository.findById(id)).thenReturn(bookOP);

        //when
        BookRespDto bookRespDto =  bookService.책수정하기(id, dto);

        //then
        assertThat(bookRespDto.getTitle()).isEqualTo(dto.getTitle());
        assertThat(bookRespDto.getAuthor()).isEqualTo(dto.getAuthor());

    }

}
