package com.bonitasoft.reactiveworkshop.api;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import com.bonitasoft.reactiveworkshop.dto.ArtistDTO;
import com.bonitasoft.reactiveworkshop.dto.CommentDTO;
import com.bonitasoft.reactiveworkshop.exception.NotFoundException;
import com.bonitasoft.reactiveworkshop.repository.ArtistRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class ArtistAPI {

    /**
     * Route from the external service API to get the last 10 comments on an artist
     */
    public static final String LAST_10_COMMENTS_ROUTE = "/comments/{artistId}/last10";

    private final ArtistRepository artistRepository;
    private final RestTemplate restTemplate;

    @GetMapping("/artist/{id}")
    public Artist findById(@PathVariable String id) throws NotFoundException {
        return artistRepository.findById(id).orElseThrow(NotFoundException::new);
    }

    @GetMapping("/artists")
    public List<Artist> findAll() {
        return artistRepository.findAll();
    }

    /**
     * Route to get the 10 last comments of a given artist
     *
     * @param artistId id of the artist to search
     * @return artist information and its 10 last comments
     * @throws NotFoundException if artist not found from database
     */
    @GetMapping("/artist/{artistId}/comments")
    public ArtistDTO getArtistComments(@PathVariable @NonNull String artistId) throws NotFoundException {
        log.info("Getting comments of artist [id={}]", artistId);
        // Get artist info and check if artist exists
        Artist artist = artistRepository.findById(artistId).orElseThrow(NotFoundException::new);
        // Get last 10 comments from external service API
        List<CommentDTO> comments = getLast10Comments(artistId);
        // Build response
        return ArtistDTO.builder()
                .artistId(artistId)
                .artistName(artist.getName())
                .genre(artist.getGenre())
                .comments(comments)
                .build();
    }

    /**
     * Get the last 10 comments of the given artist from the external service API
     *
     * @param artistId id of the artist to search
     * @return the last 10 comments of the given artist
     */
    private List<CommentDTO> getLast10Comments(@NonNull String artistId) {
        log.debug("Calling external service API to get last 10 comments of artist [id={}]", artistId);
        try {
            CommentDTO[] response = restTemplate.getForEntity(
                    LAST_10_COMMENTS_ROUTE,
                    CommentDTO[].class,
                    Collections.singletonMap("artistId", artistId)
            ).getBody();
            return response == null ? Collections.emptyList() : Arrays.asList(response);
        } catch (RestClientException e) {
            log.warn("Error from external service API when getting last 10 comments for artist [{}]", artistId, e);
            return Collections.emptyList();
        }
    }
}
