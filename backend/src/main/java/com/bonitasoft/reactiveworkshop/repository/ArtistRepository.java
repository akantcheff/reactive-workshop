package com.bonitasoft.reactiveworkshop.repository;

import com.bonitasoft.reactiveworkshop.domain.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, String> {//extends MongoRepository<Artist, String> {
    List<Artist> findAllByGenre(String genre);
}
