package com.miroku.junit.web;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.jdbc.Sql;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.miroku.junit.domain.Book;
import com.miroku.junit.domain.BookRepository;
import com.miroku.junit.web.dto.request.BookSaveReqDto;

import static org.assertj.core.api.Assertions.*;


@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {
    
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestRestTemplate rt;

    private static ObjectMapper mapper;
    private static HttpHeaders headers;

    @BeforeAll
    private static void init() {
        mapper = new ObjectMapper();
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

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
    public void saveBook_test() throws JsonProcessingException {
        //given
        BookSaveReqDto bookSaveReqDto = new BookSaveReqDto();
        bookSaveReqDto.setTitle("타이틀입니다.");
        bookSaveReqDto.setAuthor("작가입니다.");

        String body = mapper.writeValueAsString(bookSaveReqDto);
        
        //when 
        HttpEntity<String> request = new HttpEntity<String>(body, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.POST, request, String.class);
        
        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        String title = dc.read("$.body.title");
        String author = dc.read("$.body.author");

        assertThat(title).isEqualTo("타이틀입니다.");
        assertThat(author).isEqualTo("작가입니다.");

    }

    @Sql("classpath:db/tableInit.sql")  // id로 검증시 autoIncrement 로 인한 오류를 방지 하기 위해 테이블 재생성..
    @Test
    public void getBookList_test() {
        //given

        //when
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book", HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.items[0].title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit5");

    }

    @Sql("classpath:db/tableInit.sql")  // id로 검증시 autoIncrement 로 인한 오류를 방지 하기 위해 테이블 재생성..
    @Test
    public void getBookOne_test() {
        //given 
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.GET, request, String.class);

        //then
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        String title = dc.read("$.body.title");

        assertThat(code).isEqualTo(1);
        assertThat(title).isEqualTo("junit5");
    }

    @Sql("classpath:db/tableInit.sql")  // id로 검증시 autoIncrement 로 인한 오류를 방지 하기 위해 테이블 재생성..
    @Test
    public void deleteBook_test() {
        //given
        Integer id = 1;

        //when
        HttpEntity<String> request = new HttpEntity<String>(null, headers);
        ResponseEntity<String> response = rt.exchange("/api/v1/book/" + id, HttpMethod.DELETE, request, String.class);

        //then
        int resultStatus = response.getStatusCode().value();
        DocumentContext dc = JsonPath.parse(response.getBody());
        Integer code = dc.read("$.code");
        assertThat(code).isEqualTo(1);
        assertThat(resultStatus).isEqualTo(200);

    }
}
