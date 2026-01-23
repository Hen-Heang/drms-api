# Getting Started with DRMS Next.js Client

This guide will help you set up and run the Next.js client application that integrates with the DRMS API.

## Prerequisites

Before you begin, ensure you have the following installed:
- Node.js 18.x or higher
- npm or yarn
- The DRMS API backend should be running on port 8080

## Setup Instructions

### Step 1: Navigate to the Client Directory

```bash
cd nextjs-client
```

### Step 2: Install Dependencies

```bash
npm install
```

This will install all necessary packages including:
- Next.js
- React
- TypeScript
- Tailwind CSS
- Axios

### Step 3: Configure Environment Variables

Create a `.env.local` file in the `nextjs-client` directory:

```bash
cp .env.local.example .env.local
```

Edit the `.env.local` file and set your API URL:

```
NEXT_PUBLIC_API_URL=http://localhost:8080
```

### Step 4: Start the Development Server

```bash
npm run dev
```

The application will be available at http://localhost:3000

## Using the Application

### 1. Register a New User

1. Navigate to http://localhost:3000
2. Click "Register here" or go directly to http://localhost:3000/register
3. Fill in the registration form:
   - Full Name
   - Email
   - Phone (optional)
   - Password
   - Role (MERCHANT or PARTNER)
4. Click "Register"
5. Upon successful registration, you'll be automatically logged in and redirected to the dashboard

### 2. Login

1. Go to http://localhost:3000/login
2. Enter your email and password
3. Click "Login"
4. You'll be redirected to the dashboard

### 3. OTP Verification (Optional)

If you want to test the OTP functionality:

1. Go to http://localhost:3000/verify-otp
2. Enter your email address
3. Click "Send OTP"
4. Check your email for the OTP code
5. Enter the OTP and click "Verify OTP"

### 4. Dashboard

After logging in, you'll see:
- Your profile information
- Role-specific features (coming soon)
- Logout button

## Available Scripts

In the `nextjs-client` directory, you can run:

### `npm run dev`
Runs the app in development mode on http://localhost:3000

### `npm run build`
Builds the app for production to the `.next` folder

### `npm start`
Runs the production build (must run `npm run build` first)

### `npm run lint`
Runs the ESLint linter to check for code issues

## Project Structure

```
nextjs-client/
├── src/
│   ├── components/       # Reusable React components
│   │   └── ProtectedRoute.tsx  # HOC for route protection
│   ├── contexts/         # React context providers
│   │   └── AuthContext.tsx     # Authentication context
│   ├── lib/             # Utility libraries
│   │   └── api-client.ts       # API client with axios
│   ├── pages/           # Next.js pages (routes)
│   │   ├── _app.tsx            # App wrapper
│   │   ├── _document.tsx       # HTML document wrapper
│   │   ├── index.tsx           # Home page
│   │   ├── login.tsx           # Login page
│   │   ├── register.tsx        # Registration page
│   │   ├── dashboard.tsx       # User dashboard
│   │   └── verify-otp.tsx      # OTP verification
│   ├── styles/          # Global styles
│   │   └── globals.css         # Tailwind CSS imports
│   └── types/           # TypeScript type definitions
│       └── api.ts              # API response types
├── public/              # Static assets
├── .env.local          # Environment variables (not committed)
├── .env.local.example  # Example environment file
├── next.config.js      # Next.js configuration
├── package.json        # Dependencies and scripts
├── tailwind.config.js  # Tailwind CSS configuration
└── tsconfig.json       # TypeScript configuration
```

## Features

### Authentication
- **Registration**: Create a new account with role selection
- **Login**: Authenticate with email and password
- **Logout**: Clear session and return to login page
- **JWT Token Management**: Automatic token storage and inclusion in API requests
- **Auto-logout**: Redirects to login on token expiration

### Protected Routes
- Dashboard and other protected pages automatically redirect to login if not authenticated
- Token validation on every page load

### OTP Verification
- Generate OTP codes via email
- Verify OTP codes
- Resend OTP functionality

### User Roles
- **PARTNER** (Distributor): Access to partner-specific features
- **MERCHANT** (Retailer): Access to merchant-specific features

## API Integration

The application communicates with the following DRMS API endpoints:

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/auth/register` | POST | User registration |
| `/auth/login` | POST | User authentication |
| `/api/otp/generate` | POST | Generate OTP code |
| `/api/otp/verify` | POST | Verify OTP code |

## Troubleshooting

### Cannot Connect to API

**Problem**: Errors like "Network Error" or "ECONNREFUSED"

**Solution**:
1. Ensure the DRMS API backend is running on port 8080
2. Check that `NEXT_PUBLIC_API_URL` in `.env.local` is correct
3. Verify CORS is enabled in the backend

### Build Errors

**Problem**: Build fails with errors

**Solution**:
```bash
# Clear Next.js cache
rm -rf .next

# Clear node_modules and reinstall
rm -rf node_modules package-lock.json
npm install
```

### Token Expired Error

**Problem**: Getting 401 Unauthorized errors

**Solution**:
- JWT tokens expire after 24 hours
- Simply logout and login again
- The app should automatically redirect to login on token expiration

### Page Not Found

**Problem**: 404 errors when refreshing pages

**Solution**:
- This is expected in development mode for some pages
- Run `npm run build` and `npm start` to test production mode

## Development Tips

### Hot Reload
The development server supports hot reload. Changes to React components will be reflected immediately without a full page refresh.

### TypeScript
All code is written in TypeScript for type safety. The compiler will catch type errors during development.

### Styling
The app uses Tailwind CSS for styling. You can customize the theme in `tailwind.config.js`.

## Next Steps

### For Developers

1. **Add More Features**: Extend the dashboard with partner and merchant specific features
2. **Add Forms**: Create forms for product management, orders, etc.
3. **Add Tables**: Display data in tables with sorting and pagination
4. **Add Charts**: Visualize data with charts and graphs

### For Users

1. **Register an Account**: Create your account with the appropriate role
2. **Explore the Dashboard**: Familiarize yourself with the interface
3. **Provide Feedback**: Report bugs or request features

## Support

For help or questions:
1. Check the main README.md in the root directory
2. Review the API documentation at http://localhost:8080/swagger-ui.html
3. Contact the development team

## License

This project is part of the DRMS system.
