//package org.glazweq.demo.repos;
//
//
//import jakarta.transaction.Transactional;
//import org.glazweq.demo.domain.MovieCard;
//import org.springframework.data.domain.Page;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//import org.springframework.data.jpa.repository.Query;
//import org.springframework.stereotype.Repository;
//
//
//@Repository
//@Transactional
//public interface PageRepo extends JpaRepository<MovieCard, Integer> {
//
//    Page<MovieCard> findByTitleContainingIgnoreCase(String keyword, Pageable pageable);
//
//    // ...
//}