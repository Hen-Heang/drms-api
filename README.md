# DRMS - Distributor Retailer Management System

A comprehensive system for managing relationships and transactions between distributors and retailers, consisting of a Spring Boot REST API backend and a Next.js frontend.

## 🚀 Quick Start

### Backend (Spring Boot API)

The backend API is built with Spring Boot 4.0, PostgreSQL, and JWT authentication.

**Prerequisites:**
- Java 17 or higher
- PostgreSQL database
- Maven

**Setup:**

1. Create PostgreSQL database:
```sql
CREATE DATABASE drmsdb;
```

2. Create the auth_users table:
```sql
create table auth_users(
id 	BIGSERIAL primary key,
email Varchar(150) not null,
password varchar(255) not null,
full_name varchar(100) not null,
phone varchar(50),
role varchar(30) not null ,-- 'PARTNER' or 'MERCHANT'
status varchar(40) not null default 'ACTIVE',
created_at timestamp  not null default NOW()
);
```

3. Configure database connection in `src/main/resources/application.properties`

4. Build and run the API:
```bash
./mvnw spring-boot:run
```

The API will be available at http://localhost:8080

**API Documentation:**
- Swagger UI: http://localhost:8080/swagger-ui.html

**Available Endpoints:**
- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `POST /api/otp/generate` - Generate OTP
- `POST /api/otp/verify` - Verify OTP

### Frontend (Next.js Client)

The frontend is a modern Next.js application with TypeScript and Tailwind CSS.

**Prerequisites:**
- Node.js 18.x or higher
- npm or yarn

**Setup:**

1. Navigate to the client directory:
```bash
cd nextjs-client
```

2. Install dependencies:
```bash
npm install
```

3. Create environment file:
```bash
cp .env.local.example .env.local
```

4. Edit `.env.local` and configure the API URL:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

5. Run the development server:
```bash
npm run dev
```

The frontend will be available at http://localhost:3000

## 📁 Project Structure

```
drms-api/
├── src/                          # Spring Boot backend source
│   ├── main/
│   │   ├── java/                # Java source files
│   │   └── resources/           # Configuration files
│   └── test/                    # Backend tests
├── nextjs-client/               # Next.js frontend
│   ├── src/
│   │   ├── pages/              # Next.js pages
│   │   ├── components/         # React components
│   │   ├── lib/                # Utilities
│   │   ├── contexts/           # React contexts
│   │   └── types/              # TypeScript types
│   └── public/                 # Static assets
├── pom.xml                     # Maven configuration
└── README.md                   # This file
```

## 🔒 Authentication

The system uses JWT (JSON Web Token) authentication:
- Tokens expire after 24 hours
- Tokens are stored in localStorage on the frontend
- All protected API endpoints require Bearer token authentication

## 👥 User Roles

- **PARTNER** (Distributor): Manages inventory and supplies to merchants
- **MERCHANT** (Retailer): Places orders and manages retail operations

## 🛠️ Technology Stack

### Backend
- Spring Boot 4.0
- PostgreSQL
- MyBatis
- Spring Security
- JWT (JJWT)
- Swagger/OpenAPI

### Frontend
- Next.js 14
- TypeScript
- Tailwind CSS
- Axios
- React Context API

## 📝 Development Notes

### Backend Implementation Order
1. JwtTokenUtil
2. JwtRequestFilter
3. JwtAuthenticationEntryPoint
4. BeanConfig
5. CorsFilterConfiguration
6. SecurityConfig ← LAST

### Frontend Features
- User registration and login
- OTP verification
- Protected routes
- Role-based dashboards
- Responsive design
- Error handling

## 🚧 Roadmap

- [ ] Complete Partner management features
- [ ] Complete Merchant management features
- [ ] Add product catalog
- [ ] Add order management
- [ ] Add inventory tracking
- [ ] Add reporting and analytics

## 📖 Documentation

- Backend README: See root directory
- Frontend README: See `nextjs-client/README.md`
- API Documentation: http://localhost:8080/swagger-ui.html (when running)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Test thoroughly
5. Submit a pull request

## 📄 License

This project is part of the DRMS system.

## 📧 Support

For issues or questions, please contact the development team.


