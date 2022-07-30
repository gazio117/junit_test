package com.miroku.junit.domain;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@DataJpaTest    // DB 와 관련된 컴포넌트로만 메모리에 로딩
public class BookRepositoryTest {
    
    @Autowired
    private BookRepository bookRepository;

    @Test
    public void 책등록_test() {
        // given (데이터 준비)
        String title = "junit5";
        String author = "테스트";

        Book book  = Book.builder()
                                .title(title)
                                .author(author)
                                .build();

        // when (테스트 실행)
        Book bookPs = bookRepository.save(book);
        // then (검증)
        assertEquals(title, bookPs.getTitle());
        assertEquals(author, bookPs.getAuthor());

    }
    
}
