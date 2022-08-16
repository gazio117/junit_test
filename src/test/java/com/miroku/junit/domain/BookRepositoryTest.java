package com.miroku.junit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;


@DataJpaTest    // DB 와 관련된 컴포넌트로만 메모리에 로딩
public class BookRepositoryTest {
    
    @Autowired
    private BookRepository bookRepository;

    //@BeforeAll  // 테스트 시작전에 한번만 실행
    @BeforeEach // 각 테스트 시작전에 한번씩 실행
    public void 데이터준비() {
        String title = "junit5";
        String author = "테스트";

        Book book  = Book.builder()
                                .title(title)
                                .author(author)
                                .build();
        bookRepository.save(book);
    }

    @Test
    public void 책등록_test() {
        // given (데이터 준비)
        String title = "등록";
        String author = "합니다";

        Book book  = Book.builder()
                                .title(title)
                                .author(author)
                                .build();

        // when (테스트 실행)
        Book bookPS = bookRepository.save(book);
        // then (검증)
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }

    @Test
    public void 책목록보기_test() {
        // given
        String title = "junit5";
        String author = "테스트";

        // when 
        List<Book> booksPS = bookRepository.findAll();

        // then 
        assertEquals(title, booksPS.get(0).getTitle());
        assertEquals(author, booksPS.get(0).getAuthor());
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책한권보기_test() {
        // given
        String title = "junit5";
        String author = "테스트";
        System.out.println("========================================");
        //when
        Book bookPS = bookRepository.findById(1L).get();

        System.out.println("bookPS : " + bookPS.toString());

        //then
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());
    }
    
    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책삭제_test() {
        // given
        Long id = 1L;
        // when
        bookRepository.deleteById(id);
        //then
        assertFalse(bookRepository.findById(id).isPresent());
    }

    @Sql("classpath:db/tableInit.sql")
    @Test
    public void 책수정_test() {
        // given
        Long id = 1L;
        String title = "junit5";
        String author = "miroku";

        Book book = new Book(id, title, author);

        // when
        Book bookPS = bookRepository.save(book);
        
        //then 
        assertEquals(id, bookPS.getId());
        assertEquals(title, bookPS.getTitle());
        assertEquals(author, bookPS.getAuthor());

    }

}
