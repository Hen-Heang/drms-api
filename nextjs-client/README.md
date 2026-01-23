# DRMS Next.js Client

A Next.js frontend application for the DRMS (Distributor Retailer Management System) API.

## Overview

This Next.js application provides a modern, responsive web interface for the DRMS API. It includes authentication, user registration, OTP verification, and role-based dashboards for Partners (Distributors) and Merchants (Retailers).

## Features

- **Authentication System**
  - User registration with role selection (Partner/Merchant)
  - Email/password login
  - JWT token-based authentication
  - Automatic token refresh handling
  - Protected routes

- **OTP Verification**
  - Email-based OTP generation
  - OTP verification flow
  
- **Role-Based Dashboards**
  - Partner (Distributor) dashboard
  - Merchant (Retailer) dashboard
  - User profile information display

- **Modern UI**
  - Responsive design with Tailwind CSS
  - Clean and intuitive interface
  - Loading states and error handling

## Prerequisites

- Node.js 18.x or higher
- npm or yarn
- DRMS API backend running (default: http://localhost:8080)

## Installation

1. Navigate to the Next.js client directory:
```bash
cd nextjs-client
```

2. Install dependencies:
```bash
npm install
```

3. Create environment configuration:
```bash
cp .env.local.example .env.local
```

4. Edit `.env.local` and set your API URL:
```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

## Running the Application

### Development Mode

```bash
npm run dev
```

The application will be available at http://localhost:3000

### Production Build

```bash
npm run build
npm start
```

## Project Structure

```
nextjs-client/
├── src/
│   ├── components/       # Reusable React components
│   │   └── ProtectedRoute.tsx
│   ├── contexts/         # React contexts
│   │   └── AuthContext.tsx
│   ├── lib/             # Utility libraries
│   │   └── api-client.ts
│   ├── pages/           # Next.js pages
│   │   ├── _app.tsx
│   │   ├── _document.tsx
│   │   ├── index.tsx
│   │   ├── login.tsx
│   │   ├── register.tsx
│   │   ├── dashboard.tsx
│   │   └── verify-otp.tsx
│   ├── styles/          # CSS styles
│   │   └── globals.css
│   └── types/           # TypeScript types
│       └── api.ts
├── public/              # Static assets
├── .env.local.example   # Environment variables template
├── next.config.js       # Next.js configuration
├── package.json         # Dependencies
├── tailwind.config.js   # Tailwind CSS configuration
└── tsconfig.json        # TypeScript configuration
```

## Available Pages

- `/` - Home page (redirects to login or dashboard)
- `/login` - User login page
- `/register` - User registration page
- `/dashboard` - User dashboard (protected route)
- `/verify-otp` - OTP verification page

## API Integration

The application integrates with the following DRMS API endpoints:

- `POST /auth/register` - User registration
- `POST /auth/login` - User login
- `POST /api/otp/generate` - Generate OTP
- `POST /api/otp/verify` - Verify OTP

## Authentication Flow

1. User registers or logs in
2. Backend returns JWT token and user data
3. Token is stored in localStorage
4. Token is automatically included in all API requests
5. Protected routes check for valid token
6. Expired tokens trigger automatic logout and redirect to login

## User Roles

- **PARTNER** (Distributor): Access to partner-specific features
- **MERCHANT** (Retailer): Access to merchant-specific features

## Development

### Tech Stack

- **Framework**: Next.js 14
- **Language**: TypeScript
- **Styling**: Tailwind CSS
- **HTTP Client**: Axios
- **State Management**: React Context API

### Code Style

- Follow TypeScript best practices
- Use functional components with hooks
- Maintain consistent naming conventions
- Add proper error handling

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| NEXT_PUBLIC_API_URL | DRMS API base URL | http://localhost:8080 |

## Troubleshooting

### API Connection Issues

1. Ensure the DRMS API backend is running
2. Check the API URL in `.env.local`
3. Verify CORS is enabled in the backend

### Build Errors

1. Clear the Next.js cache:
```bash
rm -rf .next
```

2. Reinstall dependencies:
```bash
rm -rf node_modules package-lock.json
npm install
```

## Contributing

1. Create a feature branch
2. Make your changes
3. Test thoroughly
4. Submit a pull request

## License

This project is part of the DRMS system.

## Support

For issues or questions, please contact the development team.
