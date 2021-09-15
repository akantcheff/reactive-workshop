package com.bonitasoft.reactiveworkshop.api;

import com.bonitasoft.reactiveworkshop.dto.CommentStreamDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import reactor.test.StepVerifier;

import java.util.Collections;
import java.util.function.Predicate;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
public class GenreApiTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void testGetStreamOfCommentsByGenreNominal() {
        Predicate<CommentStreamDTO> predicate = (CommentStreamDTO commentStreamDTO) ->
                commentStreamDTO != null
                        && commentStreamDTO.getArtistId() != null
                        && commentStreamDTO.getArtistName() != null
                        && commentStreamDTO.getUserName() != null
                        && commentStreamDTO.getComment() != null;
        StepVerifier.create(
                        webTestClient
                                .get()
                                .uri("/genre/{genre}/comments/stream", Collections.singletonMap("genre", "Pop"))
                                .accept(MediaType.APPLICATION_NDJSON)
                                .exchange()
                                .expectStatus().isOk()
                                .returnResult(CommentStreamDTO.class)
                                .getResponseBody()
                                .take(2))
                .expectNextMatches(predicate)
                .expectNextMatches(predicate)
                .verifyComplete();
    }

    @Test
    public void testGetStreamOfCommentsByGenreNoData() {
        StepVerifier.create(
                        webTestClient
                                .get()
                                .uri("/genre/{genre}/comments/stream", Collections.singletonMap("genre", "foobar"))
                                .accept(MediaType.APPLICATION_NDJSON)
                                .exchange()
                                .expectStatus().isOk()
                                .returnResult(CommentStreamDTO.class)
                                .getResponseBody())
                .expectNextCount(0)
                .verifyComplete();
    }

}
