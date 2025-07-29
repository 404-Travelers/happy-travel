# Happy Travel

**Happy Travel** is a web application designed for travel enthusiasts to create, manage, and explore travel destinations. It offers user registration, destination CRUD operations, filtering, authentication via JWT, and role-based authorization.

## 🌍 Features

### User
- Registration & Login (with password validation)
- Admin panel to view and manage users

### Destinations
- Create, Read, Update, Delete (with image URLs)
- Search & Filter Destinations (by city/country/username)

### Security
- **JWT Authentication** and role-based access (USER / ADMIN)

### Documentation 
- **Swagger API Documentation**

### Exception Handling
- Global exception handling using @ControllerAdvice (GlobalExceptionHandler) --- CUSTOM EXCEPTIONS
- Returns structured and informative error messages

---

## 🛠️ Tech Stack

### Backend
- Java 17
- Spring Boot 3
- Spring Security (JWT)
- Hibernate & Spring Data JPA
- MySQL
- Swagger 
- Lombok
- IntelliJ IDEA CE

---

## 🔐 Authentication  ?????

All protected endpoints require a valid JWT token. To test with Swagger:

1. Register/login via `/register` or `/login`
2. Copy the JWT token from the login response
3. Click **Authorize** in Swagger and paste `Bearer <your-token>`

---

## 🚀 Getting Started

### Backend Setup

```bash
# Clone the repository
git clone https://github.com/404-Travelers/happy-travel
cd happy-travel

# Setup environment variables
cp .env.example .env
# Edit .env with your DB credentials and admin info

# Run the application
./mvnw spring-boot:run
```

### Required .env file

```
DB_URL=jdbc:mysql://localhost:3306/happy-travel?createDatabaseIfNotExist=true
SERVER_PORT=8080
DB_USER=root
DB_PASSWORD=root

ADMIN_EMAIL=admin12@example.com
ADMIN_USERNAME=admin12
ADMIN_PASSWORD=admin123
```

### Swagger UI

- [http://localhost:8080/swagger-ui/index.html#/](http://localhost:8080/swagger-ui/index.html#/)

### Frontend Setup

```bash
# Go to frontend folder (or clone repo)
cd happy-travel-frontend

# Install dependencies
npm install

# Run dev server
npm run dev
```

---

## 📦 Postman Collection

- Workspace: [Happy Travel Postman](https://travelers404.postman.co/)

---

## 📄 Endpoints Overview (also available in Swagger)  ???

### Auth
- `POST /register`
- `POST /login`
- `POST /admin/register` (ADMIN only)

### Destinations
- `GET /destinations` — all destinations
- `GET /destinations/auth` — destinations sorted with current user on top
- `GET /destinations/filter?city=X&country=Y`
- `GET /destinations/{id}`
- `GET /destinations/user?username=abc`
- `POST /destinations` — create (auth)
- `PUT /destinations/{id}` — update (auth)
- `DELETE /destinations/{id}` — delete (auth)

### Users
- `GET /users/me`
- `PUT /users/me`
- `DELETE /users/me`

### Admin
- `GET /admin/users`
- `GET /admin/users/{id}`
- `DELETE /admin/users/{id}`

---

## 📌 Notes

- You must manually insert your JWT token into Swagger UI to use protected endpoints
- Admin account is created on startup using values from `.env`
- Frontend currently supports image display via remote patterns in `next.config.js`

---

## 👥 Team & Credits

Developed as part of a backend bootcamp by:

- Maria Sycheva [@roz-mari](https://github.com/roz-mari)
- Iris
- Sofía Santos [@sofianutria](https://github.com/sofianutria)
- Saba [@sab-gif](https://github.com/sab-gif)

---
