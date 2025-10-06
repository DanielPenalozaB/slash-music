# Slash Music API - Authentication Endpoints

**Base URL:** `http://ec2-54-167-13-29.compute-1.amazonaws.com:7777`

**API Version:** `v1`

**Swagger Documentation:** [http://ec2-54-167-13-29.compute-1.amazonaws.com:7777/swagger-ui/index.html](http://ec2-54-167-13-29.compute-1.amazonaws.com:7777/swagger-ui/index.html)

---

## Table of Contents
- [Authentication Overview](#authentication-overview)
- [Endpoints](#endpoints)
  - [1. Register](#1-register)
  - [2. Login](#2-login)
  - [3. Refresh Token](#3-refresh-token)
  - [4. Get Current User](#4-get-current-user)
  - [5. Change Password](#5-change-password)
  - [6. Logout](#6-logout)
- [Models](#models)
- [Error Handling](#error-handling)
- [Flutter Integration Example](#flutter-integration-example)

---

## Authentication Overview

This API uses **JWT (JSON Web Tokens)** for authentication. After successful login or registration, you will receive:
- **Access Token**: Short-lived token (24 hours) used to authenticate API requests
- **Refresh Token**: Long-lived token (7 days) used to obtain new access tokens

### How to use JWT in requests

Include the access token in the `Authorization` header:

```
Authorization: Bearer {accessToken}
```

---

## Endpoints

### 1. Register

**Create a new user account**

- **URL:** `/api/v1/auth/register`
- **Method:** `POST`
- **Auth Required:** No

#### Request Body

```json
{
  "email": "user@example.com",
  "name": "John Doe",
  "password": "password123",
  "confirmPassword": "password123"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| email | String | Yes | Valid email format, max 100 characters |
| name | String | Yes | Max 50 characters |
| password | String | Yes | Min 6 characters |
| confirmPassword | String | Yes | Must match password |

#### Success Response (201 Created)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "role": "FREE_USER",
    "status": "ACTIVE",
    "profileImageUrl": null,
    "createdAt": "2025-10-06T03:00:00",
    "updatedAt": "2025-10-06T03:00:00"
  }
}
```

#### Error Responses

| Code | Description |
|------|-------------|
| 400 | Invalid input data (validation errors) |
| 409 | Email or username already exists |

---

### 2. Login

**Authenticate user and get JWT tokens**

- **URL:** `/api/v1/auth/login`
- **Method:** `POST`
- **Auth Required:** No

#### Request Body

```json
{
  "email": "user@example.com",
  "password": "password123"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| email | String | Yes | Valid email format |
| password | String | Yes | Required |

#### Success Response (200 OK)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "role": "FREE_USER",
    "status": "ACTIVE",
    "profileImageUrl": null,
    "createdAt": "2025-10-06T03:00:00",
    "updatedAt": "2025-10-06T03:00:00"
  }
}
```

#### Error Responses

| Code | Description |
|------|-------------|
| 401 | Invalid credentials (wrong email or password) |

---

### 3. Refresh Token

**Generate new access and refresh tokens**

- **URL:** `/api/v1/auth/refresh`
- **Method:** `POST`
- **Auth Required:** No (but requires valid refresh token)

#### Request Body

```json
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| refreshToken | String | Yes | Valid refresh token |

#### Success Response (200 OK)

```json
{
  "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": 1,
    "email": "user@example.com",
    "name": "John Doe",
    "role": "FREE_USER",
    "status": "ACTIVE",
    "profileImageUrl": null,
    "createdAt": "2025-10-06T03:00:00",
    "updatedAt": "2025-10-06T03:00:00"
  }
}
```

#### Error Responses

| Code | Description |
|------|-------------|
| 401 | Invalid or expired refresh token |

---

### 4. Get Current User

**Get authenticated user information**

- **URL:** `/api/v1/auth/me`
- **Method:** `GET`
- **Auth Required:** Yes

#### Headers

```
Authorization: Bearer {accessToken}
```

#### Success Response (200 OK)

```json
{
  "id": 1,
  "email": "user@example.com",
  "name": "John Doe",
  "role": "FREE_USER",
  "status": "ACTIVE",
  "profileImageUrl": null,
  "createdAt": "2025-10-06T03:00:00",
  "updatedAt": "2025-10-06T03:00:00"
}
```

#### Error Responses

| Code | Description |
|------|-------------|
| 401 | User not authenticated or invalid token |

---

### 5. Change Password

**Change current user's password**

- **URL:** `/api/v1/auth/change-password`
- **Method:** `POST`
- **Auth Required:** Yes

#### Headers

```
Authorization: Bearer {accessToken}
```

#### Request Body

```json
{
  "currentPassword": "oldPassword123",
  "newPassword": "newPassword456",
  "confirmPassword": "newPassword456"
}
```

| Field | Type | Required | Validation |
|-------|------|----------|------------|
| currentPassword | String | Yes | Required |
| newPassword | String | Yes | Min 6 characters |
| confirmPassword | String | Yes | Must match newPassword |

#### Success Response (200 OK)

```
(Empty body)
```

#### Error Responses

| Code | Description |
|------|-------------|
| 400 | Invalid password data (validation errors) |
| 401 | User not authenticated or current password incorrect |

---

### 6. Logout

**Logout current user (client-side token invalidation)**

- **URL:** `/api/v1/auth/logout`
- **Method:** `POST`
- **Auth Required:** Yes

#### Headers

```
Authorization: Bearer {accessToken}
```

#### Success Response (200 OK)

```
(Empty body)
```

**Note:** This is a stateless JWT implementation. The client should discard the access and refresh tokens locally.

---

## Models

### AuthResponse

```dart
class AuthResponse {
  final String accessToken;
  final String refreshToken;
  final User user;
}
```

### User (UserResponseDTO)

```dart
class User {
  final int id;
  final String email;
  final String name;
  final String role;        // "FREE_USER" | "PREMIUM_USER" | "ADMIN"
  final String status;      // "ACTIVE" | "INACTIVE" | "SUSPENDED"
  final String? profileImageUrl;
  final DateTime createdAt;
  final DateTime updatedAt;
}
```

### UserRole Enum

```dart
enum UserRole {
  FREE_USER,
  PREMIUM_USER,
  ADMIN
}
```

### UserStatus Enum

```dart
enum UserStatus {
  ACTIVE,
  INACTIVE,
  SUSPENDED
}
```

---

## Error Handling

All errors follow this format:

```json
{
  "timestamp": "2025-10-06T03:00:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Validation failed",
  "path": "/api/v1/auth/register"
}
```

### Common HTTP Status Codes

| Code | Meaning |
|------|---------|
| 200 | Success |
| 201 | Created (successful registration) |
| 400 | Bad Request (validation errors) |
| 401 | Unauthorized (authentication failed) |
| 403 | Forbidden (insufficient permissions) |
| 409 | Conflict (duplicate email/username) |
| 500 | Internal Server Error |

---

## Flutter Integration Example

### 1. Setup Dependencies

Add to `pubspec.yaml`:

```yaml
dependencies:
  http: ^1.1.0
  shared_preferences: ^2.2.2
```

### 2. API Service

```dart
import 'dart:convert';
import 'package:http/http.dart' as http;
import 'package:shared_preferences/shared_preferences.dart';

class AuthService {
  static const String baseUrl = 'http://ec2-54-167-13-29.compute-1.amazonaws.com:7777';
  static const String apiPrefix = '/api/v1/auth';

  // Register
  Future<AuthResponse> register({
    required String email,
    required String name,
    required String password,
    required String confirmPassword,
  }) async {
    final response = await http.post(
      Uri.parse('$baseUrl$apiPrefix/register'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'email': email,
        'name': name,
        'password': password,
        'confirmPassword': confirmPassword,
      }),
    );

    if (response.statusCode == 201) {
      final authResponse = AuthResponse.fromJson(jsonDecode(response.body));
      await _saveTokens(authResponse.accessToken, authResponse.refreshToken);
      return authResponse;
    } else {
      throw Exception('Registration failed: ${response.body}');
    }
  }

  // Login
  Future<AuthResponse> login({
    required String email,
    required String password,
  }) async {
    final response = await http.post(
      Uri.parse('$baseUrl$apiPrefix/login'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({
        'email': email,
        'password': password,
      }),
    );

    if (response.statusCode == 200) {
      final authResponse = AuthResponse.fromJson(jsonDecode(response.body));
      await _saveTokens(authResponse.accessToken, authResponse.refreshToken);
      return authResponse;
    } else {
      throw Exception('Login failed: ${response.body}');
    }
  }

  // Get Current User
  Future<User> getCurrentUser() async {
    final accessToken = await _getAccessToken();
    
    final response = await http.get(
      Uri.parse('$baseUrl$apiPrefix/me'),
      headers: {
        'Content-Type': 'application/json',
        'Authorization': 'Bearer $accessToken',
      },
    );

    if (response.statusCode == 200) {
      return User.fromJson(jsonDecode(response.body));
    } else if (response.statusCode == 401) {
      // Try to refresh token
      await refreshToken();
      return getCurrentUser(); // Retry
    } else {
      throw Exception('Failed to get current user: ${response.body}');
    }
  }

  // Refresh Token
  Future<AuthResponse> refreshToken() async {
    final refreshToken = await _getRefreshToken();
    
    final response = await http.post(
      Uri.parse('$baseUrl$apiPrefix/refresh'),
      headers: {'Content-Type': 'application/json'},
      body: jsonEncode({'refreshToken': refreshToken}),
    );

    if (response.statusCode == 200) {
      final authResponse = AuthResponse.fromJson(jsonDecode(response.body));
      await _saveTokens(authResponse.accessToken, authResponse.refreshToken);
      return authResponse;
    } else {
      await logout(); // Clear invalid tokens
      throw Exception('Token refresh failed: ${response.body}');
    }
  }

  // Logout
  Future<void> logout() async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.remove('accessToken');
    await prefs.remove('refreshToken');
  }

  // Private methods for token management
  Future<void> _saveTokens(String accessToken, String refreshToken) async {
    final prefs = await SharedPreferences.getInstance();
    await prefs.setString('accessToken', accessToken);
    await prefs.setString('refreshToken', refreshToken);
  }

  Future<String> _getAccessToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('accessToken') ?? '';
  }

  Future<String> _getRefreshToken() async {
    final prefs = await SharedPreferences.getInstance();
    return prefs.getString('refreshToken') ?? '';
  }
}
```

### 3. Models

```dart
class AuthResponse {
  final String accessToken;
  final String refreshToken;
  final User user;

  AuthResponse({
    required this.accessToken,
    required this.refreshToken,
    required this.user,
  });

  factory AuthResponse.fromJson(Map<String, dynamic> json) {
    return AuthResponse(
      accessToken: json['accessToken'],
      refreshToken: json['refreshToken'],
      user: User.fromJson(json['user']),
    );
  }
}

class User {
  final int id;
  final String email;
  final String name;
  final String role;
  final String status;
  final String? profileImageUrl;
  final DateTime createdAt;
  final DateTime updatedAt;

  User({
    required this.id,
    required this.email,
    required this.name,
    required this.role,
    required this.status,
    this.profileImageUrl,
    required this.createdAt,
    required this.updatedAt,
  });

  factory User.fromJson(Map<String, dynamic> json) {
    return User(
      id: json['id'],
      email: json['email'],
      name: json['name'],
      role: json['role'],
      status: json['status'],
      profileImageUrl: json['profileImageUrl'],
      createdAt: DateTime.parse(json['createdAt']),
      updatedAt: DateTime.parse(json['updatedAt']),
    );
  }
}
```

### 4. Usage Example

```dart
final authService = AuthService();

// Login
try {
  final authResponse = await authService.login(
    email: 'user@example.com',
    password: 'password123',
  );
  print('Logged in: ${authResponse.user.name}');
} catch (e) {
  print('Login error: $e');
}

// Get current user
try {
  final user = await authService.getCurrentUser();
  print('Current user: ${user.name}');
} catch (e) {
  print('Error: $e');
}

// Logout
await authService.logout();
```

---

## Testing with cURL

```bash
# Register
curl -X POST http://ec2-54-167-13-29.compute-1.amazonaws.com:7777/api/v1/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "name": "Test User",
    "password": "password123",
    "confirmPassword": "password123"
  }'

# Login
curl -X POST http://ec2-54-167-13-29.compute-1.amazonaws.com:7777/api/v1/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "email": "test@example.com",
    "password": "password123"
  }'

# Get current user
curl -X GET http://ec2-54-167-13-29.compute-1.amazonaws.com:7777/api/v1/auth/me \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## Security Best Practices

1. **Store tokens securely** using `flutter_secure_storage` instead of `shared_preferences` for production
2. **Always use HTTPS** in production (update base URL)
3. **Implement token refresh logic** automatically when receiving 401 errors
4. **Clear tokens on logout** to prevent unauthorized access
5. **Validate token expiration** on the client side
6. **Handle network errors** gracefully with retry logic

---

**Last Updated:** October 6, 2025

**Contact:** Backend Team - Slash Music

