package com.example.thermos.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import java.time.LocalDateTime;

public class ThermosBottle {
    private String id;
    
    @NotBlank(message = "Tên bình giữ nhiệt không được để trống")
    private String name;
    
    @NotBlank(message = "Thương hiệu không được để trống")
    private String brand;
    
    @NotNull(message = "Dung tích không được để trống")
    @Positive(message = "Dung tích phải lớn hơn 0")
    private Double capacity; // Đơn vị: ml
    
    @NotBlank(message = "Màu sắc không được để trống")
    private String color;
    
    @NotNull(message = "Giá không được để trống")
    @Positive(message = "Giá phải lớn hơn 0")
    private Double price; // Đơn vị: VND
    
    private String material; // Chất liệu
    private String description; // Mô tả
    private Integer keepHotHours; // Số giờ giữ nhiệt
    private Integer keepColdHours; // Số giờ giữ lạnh
    private String imageUrl; // URL hình ảnh
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    // Constructors
    public ThermosBottle() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }
    
    public ThermosBottle(String name, String brand, Double capacity, String color, Double price) {
        this();
        this.name = name;
        this.brand = brand;
        this.capacity = capacity;
        this.color = color;
        this.price = price;
    }
    
    // Getters and Setters
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getBrand() {
        return brand;
    }
    
    public void setBrand(String brand) {
        this.brand = brand;
    }
    
    public Double getCapacity() {
        return capacity;
    }
    
    public void setCapacity(Double capacity) {
        this.capacity = capacity;
    }
    
    public String getColor() {
        return color;
    }
    
    public void setColor(String color) {
        this.color = color;
    }
    
    public Double getPrice() {
        return price;
    }
    
    public void setPrice(Double price) {
        this.price = price;
    }
    
    public String getMaterial() {
        return material;
    }
    
    public void setMaterial(String material) {
        this.material = material;
    }
    
    public String getDescription() {
        return description;
    }
    
    public void setDescription(String description) {
        this.description = description;
    }
    
    public Integer getKeepHotHours() {
        return keepHotHours;
    }
    
    public void setKeepHotHours(Integer keepHotHours) {
        this.keepHotHours = keepHotHours;
    }
    
    public Integer getKeepColdHours() {
        return keepColdHours;
    }
    
    public void setKeepColdHours(Integer keepColdHours) {
        this.keepColdHours = keepColdHours;
    }
    
    public String getImageUrl() {
        return imageUrl;
    }
    
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }
    
    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
    
    @Override
    public String toString() {
        return "ThermosBottle{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", brand='" + brand + '\'' +
                ", capacity=" + capacity +
                ", color='" + color + '\'' +
                ", price=" + price +
                ", material='" + material + '\'' +
                ", description='" + description + '\'' +
                ", keepHotHours=" + keepHotHours +
                ", keepColdHours=" + keepColdHours +
                ", imageUrl='" + imageUrl + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}