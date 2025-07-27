package com.example.ratingservice.service;

import com.example.ratingservice.dto.RatingAverageResponse;
import com.example.ratingservice.dto.RatingRequest;
import com.example.ratingservice.dto.RatingResponse;
import com.example.ratingservice.entity.Item;
import com.example.ratingservice.entity.Rating;
import com.example.ratingservice.entity.User;
import com.example.ratingservice.repository.ItemRepository;
import com.example.ratingservice.repository.RatingRepository;
import com.example.ratingservice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserRepository userRepository;

    public RatingResponse createOrUpdateRating(Long itemId, RatingRequest request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Optional<Rating> existingRating = ratingRepository.findByUserAndItem(user, item);

        Rating rating;
        if (existingRating.isPresent()) {
            // Update existing rating
            rating = existingRating.get();
            rating.setRating(request.getRating());
            rating.setComment(request.getComment());
            rating.preUpdate(); // Update timestamp
        } else {
            // Create new rating
            rating = new Rating(user, item, request.getRating(), request.getComment());
        }

        rating = ratingRepository.save(rating);
        return new RatingResponse(rating);
    }

    @Transactional(readOnly = true)
    public List<RatingResponse> getAllRatingsForItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        List<Rating> ratings = ratingRepository.findByItemOrderByCreatedAtDesc(item);
        return ratings.stream()
                .map(RatingResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public RatingAverageResponse getAverageRatingForItem(Long itemId) {
        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Double averageRating = ratingRepository.findAverageRatingByItem(item);
        Long totalRatings = ratingRepository.countRatingsByItem(item);

        return new RatingAverageResponse(
                item.getId(),
                item.getName(),
                averageRating,
                totalRatings
        );
    }

    @Transactional(readOnly = true)
    public List<RatingResponse> getUserRatings(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Rating> ratings = ratingRepository.findByUserOrderByCreatedAtDesc(user);
        return ratings.stream()
                .map(RatingResponse::new)
                .collect(Collectors.toList());
    }

    public void deleteRating(Long itemId, String username, boolean isAdmin) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Rating rating = ratingRepository.findByUserAndItem(user, item)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        // Check if user owns the rating or is admin
        if (!rating.getUser().getUsername().equals(username) && !isAdmin) {
            throw new AccessDeniedException("You can only delete your own ratings");
        }

        ratingRepository.delete(rating);
    }

    public void deleteRatingAsAdmin(Long itemId, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("Item not found"));

        Rating rating = ratingRepository.findByUserAndItem(user, item)
                .orElseThrow(() -> new RuntimeException("Rating not found"));

        ratingRepository.delete(rating);
    }
}