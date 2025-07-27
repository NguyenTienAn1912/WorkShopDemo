# Rating Service API

Hệ thống đánh giá sản phẩm (Rating Service) được xây dựng bằng Spring Boot, cung cấp các tính năng đánh giá và quản lý đánh giá sản phẩm.

## Tính năng chính

1. **POST /api/items/{itemId}/ratings** – Đánh giá sản phẩm
2. **GET /api/items/{itemId}/ratings** – Xem tất cả đánh giá của sản phẩm  
3. **GET /api/items/{itemId}/ratings/average** – Lấy điểm trung bình
4. **GET /api/users/me/ratings** – Xem các đánh giá do chính mình gửi
5. **DELETE /api/items/{itemId}/ratings** – Xoá đánh giá của chính mình

## Yêu cầu hệ thống

- Java 17+
- Maven 3.6+

## Cài đặt và chạy

```bash
# Clone repository
git clone <repository-url>
cd rating-service

# Build project
mvn clean compile

# Run application
mvn spring-boot:run
```

Ứng dụng sẽ chạy tại: http://localhost:8080

## Database

Sử dụng H2 in-memory database cho demo. Truy cập H2 Console tại: http://localhost:8080/h2-console

**Thông tin kết nối:**
- JDBC URL: `jdbc:h2:mem:testdb`
- Username: `sa`
- Password: `password`

## Dữ liệu mẫu

Hệ thống được khởi tạo với dữ liệu mẫu:

**Users:**
- `user1` / `password` (USER role)
- `user2` / `password` (USER role)  
- `admin` / `admin` (ADMIN role)

**Items:**
- iPhone 15 (ID: 1)
- Samsung Galaxy S24 (ID: 2)
- MacBook Pro (ID: 3)
- Dell XPS 13 (ID: 4)
- AirPods Pro (ID: 5)

## API Documentation

### Authentication

#### Đăng nhập
```http
POST /api/auth/login
Content-Type: application/json

{
  "username": "user1",
  "password": "password"
}
```

**Response:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "type": "Bearer",
  "id": 1,
  "username": "user1",
  "email": "user1@example.com",
  "fullName": "John Doe",
  "role": "USER"
}
```

### Rating APIs

Tất cả các API yêu cầu xác thực (trừ GET ratings và average) cần thêm header:
```
Authorization: Bearer <token>
```

#### 1. Đánh giá sản phẩm
```http
POST /api/items/{itemId}/ratings
Content-Type: application/json
Authorization: Bearer <token>

{
  "rating": 5,
  "comment": "Excellent product!"
}
```

**Response:**
```json
{
  "id": 1,
  "userName": "John Doe",
  "itemId": 1,
  "itemName": "iPhone 15",
  "rating": 5,
  "comment": "Excellent product!",
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}
```

#### 2. Xem tất cả đánh giá của sản phẩm
```http
GET /api/items/{itemId}/ratings
```

**Response:**
```json
[
  {
    "id": 1,
    "userName": "John Doe",
    "itemId": 1,
    "itemName": "iPhone 15",
    "rating": 5,
    "comment": "Excellent product!",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

#### 3. Lấy điểm trung bình
```http
GET /api/items/{itemId}/ratings/average
```

**Response:**
```json
{
  "itemId": 1,
  "itemName": "iPhone 15",
  "averageRating": 4.2,
  "totalRatings": 5
}
```

#### 4. Xem đánh giá của chính mình
```http
GET /api/users/me/ratings
Authorization: Bearer <token>
```

**Response:**
```json
[
  {
    "id": 1,
    "userName": "John Doe",
    "itemId": 1,
    "itemName": "iPhone 15",
    "rating": 5,
    "comment": "Excellent product!",
    "createdAt": "2024-01-15T10:30:00",
    "updatedAt": "2024-01-15T10:30:00"
  }
]
```

#### 5. Xoá đánh giá của chính mình
```http
DELETE /api/items/{itemId}/ratings
Authorization: Bearer <token>
```

**Response:**
```json
{
  "message": "Rating deleted successfully"
}
```

#### 6. Admin xoá đánh giá của user khác
```http
DELETE /api/items/{itemId}/ratings/user/{userId}
Authorization: Bearer <admin-token>
```

**Response:**
```json
{
  "message": "Rating deleted successfully"
}
```

## Ví dụ sử dụng với cURL

### 1. Đăng nhập
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{"username":"user1","password":"password"}'
```

### 2. Đánh giá sản phẩm
```bash
curl -X POST http://localhost:8080/api/items/1/ratings \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_TOKEN" \
  -d '{"rating":5,"comment":"Great product!"}'
```

### 3. Xem đánh giá sản phẩm
```bash
curl -X GET http://localhost:8080/api/items/1/ratings
```

### 4. Xem điểm trung bình
```bash
curl -X GET http://localhost:8080/api/items/1/ratings/average
```

### 5. Xem đánh giá của mình
```bash
curl -X GET http://localhost:8080/api/users/me/ratings \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### 6. Xoá đánh giá
```bash
curl -X DELETE http://localhost:8080/api/items/1/ratings \
  -H "Authorization: Bearer YOUR_TOKEN"
```

## Validation Rules

- **Rating**: Bắt buộc, từ 1-5 sao
- **Comment**: Tùy chọn, tối đa 1000 ký tự
- **Authentication**: Bắt buộc cho POST, DELETE và GET user ratings
- **Authorization**: User chỉ có thể xoá đánh giá của mình, Admin có thể xoá mọi đánh giá

## Error Handling

API trả về các mã lỗi HTTP chuẩn:

- **400 Bad Request**: Dữ liệu không hợp lệ
- **401 Unauthorized**: Chưa đăng nhập
- **403 Forbidden**: Không có quyền truy cập
- **404 Not Found**: Không tìm thấy resource

**Ví dụ error response:**
```json
{
  "error": "Item not found"
}
```

## Technology Stack

- **Spring Boot 3.1.0**
- **Spring Security** với JWT authentication
- **Spring Data JPA** 
- **H2 Database** (in-memory)
- **Maven** build tool
- **Java 17**

## Architecture

```
Controller Layer (REST API)
    ↓
Service Layer (Business Logic)  
    ↓
Repository Layer (Data Access)
    ↓
Database (H2)
```

## Security Features

- JWT-based authentication
- Role-based access control (USER/ADMIN)
- Password encryption with BCrypt
- Method-level security annotations
- CORS support

## Future Enhancements

- Pagination cho danh sách đánh giá
- Search và filter đánh giá
- Upload hình ảnh cho đánh giá
- Email notifications
- Rate limiting
- Caching với Redis
- MySQL/PostgreSQL database
- Docker containerization