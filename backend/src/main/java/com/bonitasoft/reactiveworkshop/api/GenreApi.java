package com.bonitasoft.reactiveworkshop.api;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.dto.CommentDTO;
import com.bonitasoft.reactiveworkshop.dto.CommentStreamDTO;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class GenreApi {

    /**
     * Route from the external service API to get a stream of comments on an artist
     */
    public static final String STREAM_OF_COMMENTS_ROUTE = "/comments/{artistId}/stream";

    private final ArtistRepository artistRepository;
    private final WebClient webClient;

    @GetMapping("/genres")
    public List<String> findAll() {
        return artistRepository.findAll().stream()
                .map(Artist::getGenre)
                .filter(g -> !g.isEmpty())
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Route to get a stream of comments on artists filtered by a given genre
     *
     * @param genre genre name to filter on
     * @return a steam of comments with artists information
     */
    @GetMapping(path = "/genre/{genre}/comments/stream", produces = MediaType.APPLICATION_NDJSON_VALUE)
    public Flux<CommentStreamDTO> getStreamOfCommentsByGenre(@PathVariable @NonNull String genre) {
        log.info("Getting stream of comments on artists with genre [{}]", genre);
        return Flux.fromStream(artistRepository
                        .findAllByGenre(genre)
                        .stream())
                .flatMap(this::getStreamOfCommentsByArtist);
    }

    /**
     * Get a stream of comments on the given artist from the external service API
     *
     * @param artist artist to search comments on
     * @return a steam of comments with the artist information
     */
    private Flux<CommentStreamDTO> getStreamOfCommentsByArtist(@NonNull Artist artist) {
        log.debug("Calling external service API to get stream of comments on artist [{}]", artist);
        return this.webClient
                .get()
                .uri(STREAM_OF_COMMENTS_ROUTE, Collections.singletonMap("artistId", artist.getId()))
                .accept(MediaType.APPLICATION_NDJSON)
                .retrieve()
                .bodyToFlux(CommentDTO.class)
                .map(commentDTO -> CommentStreamDTO.builder()
                        .artistId(artist.getId())
                        .artistName(artist.getName())
                        .userName(commentDTO.getUserName())
                        .comment((commentDTO.getComment()))
                        .build());
    }
}
