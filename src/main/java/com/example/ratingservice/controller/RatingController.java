package com.example.ratingservice.controller;

import com.example.ratingservice.dto.RatingAverageResponse;
import com.example.ratingservice.dto.RatingRequest;
import com.example.ratingservice.dto.RatingResponse;
import com.example.ratingservice.entity.User;
import com.example.ratingservice.service.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RatingController {

    @Autowired
    private RatingService ratingService;

    /**
     * 1. POST /api/items/{itemId}/ratings – Đánh giá sản phẩm
     * Người dùng đã đăng nhập gửi đánh giá (1–5 sao) cho sản phẩm.
     * Nếu đã đánh giá trước đó → cập nhật (update).
     */
    @PostMapping("/items/{itemId}/ratings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<RatingResponse> createOrUpdateRating(
            @PathVariable Long itemId,
            @Valid @RequestBody RatingRequest request) {
        
        String username = getCurrentUsername();
        RatingResponse response = ratingService.createOrUpdateRating(itemId, request, username);
        return ResponseEntity.ok(response);
    }

    /**
     * 2. GET /api/items/{itemId}/ratings – Xem tất cả đánh giá của sản phẩm
     * Trả về danh sách các đánh giá, gồm tên người đánh giá, sao, nhận xét, ngày đánh giá.
     * Admin và user đều xem được.
     */
    @GetMapping("/items/{itemId}/ratings")
    public ResponseEntity<List<RatingResponse>> getAllRatingsForItem(@PathVariable Long itemId) {
        List<RatingResponse> ratings = ratingService.getAllRatingsForItem(itemId);
        return ResponseEntity.ok(ratings);
    }

    /**
     * 3. GET /api/items/{itemId}/ratings/average – Lấy điểm trung bình
     * Trả về điểm trung bình (số sao) của sản phẩm, ví dụ: 4.2
     */
    @GetMapping("/items/{itemId}/ratings/average")
    public ResponseEntity<RatingAverageResponse> getAverageRatingForItem(@PathVariable Long itemId) {
        RatingAverageResponse averageResponse = ratingService.getAverageRatingForItem(itemId);
        return ResponseEntity.ok(averageResponse);
    }

    /**
     * 4. GET /api/users/me/ratings – Xem các đánh giá do chính mình gửi
     * Trả về danh sách sản phẩm mà user đã từng đánh giá.
     */
    @GetMapping("/users/me/ratings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<RatingResponse>> getCurrentUserRatings() {
        String username = getCurrentUsername();
        List<RatingResponse> ratings = ratingService.getUserRatings(username);
        return ResponseEntity.ok(ratings);
    }

    /**
     * 5. DELETE /api/items/{itemId}/ratings – Xoá đánh giá của chính mình
     * Người dùng chỉ được xoá đánh giá của chính họ.
     * Admin được xoá thay người khác.
     */
    @DeleteMapping("/items/{itemId}/ratings")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteOwnRating(@PathVariable Long itemId) {
        String username = getCurrentUsername();
        boolean isAdmin = isCurrentUserAdmin();
        
        ratingService.deleteRating(itemId, username, isAdmin);
        
        return ResponseEntity.ok(Map.of("message", "Rating deleted successfully"));
    }

    /**
     * Admin endpoint to delete any user's rating
     * DELETE /api/items/{itemId}/ratings/user/{userId}
     */
    @DeleteMapping("/items/{itemId}/ratings/user/{userId}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Map<String, String>> deleteUserRatingAsAdmin(
            @PathVariable Long itemId, 
            @PathVariable Long userId) {
        
        ratingService.deleteRatingAsAdmin(itemId, userId);
        
        return ResponseEntity.ok(Map.of("message", "Rating deleted successfully"));
    }

    /**
     * Exception handlers
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<Map<String, String>> handleRuntimeException(RuntimeException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(Map.of("error", e.getMessage()));
    }

    @ExceptionHandler(org.springframework.security.access.AccessDeniedException.class)
    public ResponseEntity<Map<String, String>> handleAccessDeniedException(
            org.springframework.security.access.AccessDeniedException e) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(Map.of("error", e.getMessage()));
    }

    /**
     * Helper methods
     */
    private String getCurrentUsername() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getName();
    }

    private boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals("ROLE_ADMIN"));
    }
}