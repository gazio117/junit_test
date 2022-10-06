package com.miroku.junit.web;

import static org.assertj.core.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;
import com.miroku.junit.web.dto.request.BookSaveReqDto;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class BookApiControllerTest {
    

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
}
