# 🌡️ Thermos Bottle Management System

Hệ thống quản lý bình giữ nhiệt được xây dựng bằng Spring Boot với Firebase và Google Authentication.

## 📋 Tính Năng

- ✅ **Đăng nhập bằng Google OAuth2** - Xác thực an toàn và tiện lợi
- ✅ **Hiển thị danh sách** - Xem tất cả bình giữ nhiệt từ Firebase
- ✅ **Thêm mới** - Tạo mới bình giữ nhiệt với validation đầy đủ
- ✅ **Cập nhật** - Chỉnh sửa thông tin bình giữ nhiệt
- ✅ **Xóa** - Xóa bình giữ nhiệt với xác nhận
- 🔍 **Tìm kiếm** - Tìm kiếm theo tên hoặc thương hiệu
- 📊 **Thống kê** - Hiển thị thống kê tổng quan

## 🛠️ Công Nghệ Sử Dụng

- **Backend**: Spring Boot 3.2.0, Spring Security, Spring Web
- **Database**: Firebase Firestore
- **Authentication**: Google OAuth2
- **Frontend**: Thymeleaf, Bootstrap 5, Font Awesome
- **Build Tool**: Maven
- **Java Version**: 17+

## 🚀 Cài Đặt và Chạy

### 1. Yêu Cầu Hệ Thống

- Java 17 hoặc cao hơn
- Maven 3.6+
- Tài khoản Google Cloud Console
- Tài khoản Firebase

### 2. Cấu Hình Google OAuth2

1. Truy cập [Google Cloud Console](https://console.cloud.google.com/)
2. Tạo project mới hoặc chọn project có sẵn
3. Kích hoạt Google+ API
4. Tạo OAuth 2.0 Client ID:
   - Application type: Web application
   - Authorized redirect URIs: `http://localhost:8080/login/oauth2/code/google`
5. Lưu Client ID và Client Secret

### 3. Cấu Hình Firebase

1. Truy cập [Firebase Console](https://console.firebase.google.com/)
2. Tạo project mới
3. Kích hoạt Firestore Database
4. Tạo Service Account Key:
   - Project Settings → Service Accounts
   - Generate new private key
   - Tải file JSON về

### 4. Cấu Hình Ứng Dụng

1. **Cập nhật `src/main/resources/application.properties`:**
   ```properties
   spring.security.oauth2.client.registration.google.client-id=YOUR_GOOGLE_CLIENT_ID
   spring.security.oauth2.client.registration.google.client-secret=YOUR_GOOGLE_CLIENT_SECRET
   firebase.database-url=https://YOUR_PROJECT_ID-default-rtdb.firebaseio.com
   ```

2. **Thêm Firebase Service Account Key:**
   - Đổi tên file JSON thành `firebase-service-account-key.json`
   - Đặt vào thư mục `src/main/resources/`

### 5. Chạy Ứng Dụng

```bash
# Clone repository
git clone <repository-url>
cd thermos-bottle-management

# Build và chạy
mvn clean install
mvn spring-boot:run
```

Truy cập: http://localhost:8080

## 📱 Hướng Dẫn Sử Dụng

### Đăng Nhập
1. Truy cập trang chủ
2. Nhấn "Đăng Nhập với Google"
3. Chọn tài khoản Google và xác thực

### Quản Lý Bình Giữ Nhiệt

**Thêm Mới:**
1. Nhấn "Thêm Mới" từ danh sách
2. Điền thông tin bắt buộc: Tên, Thương hiệu, Dung tích, Màu sắc, Giá
3. Có thể thêm thông tin tùy chọn: Chất liệu, Mô tả, URL hình ảnh, etc.
4. Nhấn "Thêm Mới"

**Xem Chi Tiết:**
1. Nhấn biểu tượng mắt (👁️) trong danh sách
2. Xem đầy đủ thông tin và hình ảnh

**Chỉnh Sửa:**
1. Nhấn biểu tượng bút (✏️) trong danh sách hoặc từ trang chi tiết
2. Cập nhật thông tin
3. Nhấn "Cập Nhật"

**Xóa:**
1. Nhấn biểu tượng thùng rác (🗑️)
2. Xác nhận trong modal popup

**Tìm Kiếm:**
- Sử dụng thanh tìm kiếm ở đầu danh sách
- Tìm theo tên hoặc thương hiệu

## 📊 Cấu Trúc Database (Firebase)

### Collection: `thermos_bottles`

```json
{
  "id": "auto-generated-id",
  "name": "Bình giữ nhiệt Stanley Classic",
  "brand": "Stanley",
  "capacity": 750.0,
  "color": "Đen",
  "price": 1200000.0,
  "material": "Thép không gỉ",
  "description": "Bình giữ nhiệt chất lượng cao...",
  "keepHotHours": 12,
  "keepColdHours": 15,
  "imageUrl": "https://example.com/image.jpg",
  "createdAt": "2024-01-01T10:00:00",
  "updatedAt": "2024-01-01T10:00:00"
}
```

## 🎨 Giao Diện

- **Responsive Design**: Tương thích mọi thiết bị
- **Bootstrap 5**: UI hiện đại và đẹp mắt
- **Font Awesome**: Icons phong phú
- **Gradient Colors**: Màu sắc gradient đẹp
- **Animations**: Hiệu ứng mượt mà

## 🔒 Bảo Mật

- **OAuth2 Authentication**: Xác thực qua Google
- **CSRF Protection**: Bảo vệ chống tấn công CSRF
- **Input Validation**: Kiểm tra dữ liệu đầu vào
- **Error Handling**: Xử lý lỗi an toàn

## 📁 Cấu Trúc Project

```
src/
├── main/
│   ├── java/com/example/thermos/
│   │   ├── ThermosBottleApplication.java
│   │   ├── config/
│   │   │   ├── FirebaseConfig.java
│   │   │   └── SecurityConfig.java
│   │   ├── controller/
│   │   │   ├── HomeController.java
│   │   │   └── ThermosBottleController.java
│   │   ├── model/
│   │   │   └── ThermosBottle.java
│   │   └── service/
│   │       └── ThermosBottleService.java
│   └── resources/
│       ├── templates/
│       │   ├── index.html
│       │   ├── login.html
│       │   └── thermos/
│       │       ├── list.html
│       │       ├── form.html
│       │       └── view.html
│       ├── application.properties
│       └── firebase-service-account-key.json
└── test/
```

## 🐛 Troubleshooting

### Lỗi Firebase
- Kiểm tra file `firebase-service-account-key.json` có đúng vị trí
- Đảm bảo Firebase project ID đúng trong URL

### Lỗi Google OAuth2
- Kiểm tra Client ID và Client Secret
- Đảm bảo redirect URI đúng trong Google Console

### Lỗi Port
- Thay đổi port trong `application.properties` nếu 8080 bị chiếm dụng

## 📝 TODO

- [ ] Thêm phân trang cho danh sách
- [ ] Upload hình ảnh trực tiếp
- [ ] Xuất báo cáo Excel/PDF
- [ ] API REST endpoints
- [ ] Unit tests
- [ ] Docker containerization

## 🤝 Đóng Góp

1. Fork project
2. Tạo branch mới (`git checkout -b feature/AmazingFeature`)
3. Commit changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to branch (`git push origin feature/AmazingFeature`)
5. Tạo Pull Request

## 📄 License

Distributed under the MIT License. See `LICENSE` for more information.

## 📞 Liên Hệ

- Email: your-email@example.com
- GitHub: [@your-username](https://github.com/your-username)

---

⭐ **Nếu project này hữu ích, hãy cho một star!** ⭐