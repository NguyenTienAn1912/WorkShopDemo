package com.example.ratingservice.repository;

import com.example.ratingservice.entity.Rating;
import com.example.ratingservice.entity.User;
import com.example.ratingservice.entity.Item;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
    
    Optional<Rating> findByUserAndItem(User user, Item item);
    
    List<Rating> findByItemOrderByCreatedAtDesc(Item item);
    
    List<Rating> findByUserOrderByCreatedAtDesc(User user);
    
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.item = :item")
    Double findAverageRatingByItem(@Param("item") Item item);
    
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.item = :item")
    Long countRatingsByItem(@Param("item") Item item);
    
    void deleteByUserAndItem(User user, Item item);
}