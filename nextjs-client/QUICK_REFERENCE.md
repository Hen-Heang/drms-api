# DRMS Next.js Quick Reference

## 🚀 Quick Start Commands

```bash
# Navigate to the Next.js client
cd nextjs-client

# Install dependencies
npm install

# Create environment file
cp .env.local.example .env.local

# Start development server
npm run dev
# Access at: http://localhost:3000
```

## 📱 Available Pages

### Public Pages (No Login Required)

#### 1. Login Page - `/login`
- Email and password authentication
- Link to registration page
- Error handling for invalid credentials
- Auto-redirect to dashboard on success

#### 2. Registration Page - `/register`
- Full name, email, phone, password fields
- Role selection (PARTNER or MERCHANT)
- Link to login page
- Auto-redirect to dashboard on success

#### 3. OTP Verification - `/verify-otp`
- Two-step process: email → OTP
- Email-based OTP delivery
- Resend OTP functionality
- Verification with success/error feedback

### Protected Pages (Login Required)

#### 4. Dashboard - `/dashboard`
- Shows user profile information
- Role-based content (PARTNER/MERCHANT)
- User details display (email, phone, status)
- Logout button
- Auto-redirect to login if not authenticated

## 🔑 User Roles

### PARTNER (Distributor)
- Manages inventory and supplies
- Future: Product catalog, merchant management

### MERCHANT (Retailer)
- Places orders
- Future: Order history, product browsing

## 🛠️ Tech Stack

| Component | Technology |
|-----------|-----------|
| Framework | Next.js 14 |
| Language | TypeScript |
| Styling | Tailwind CSS |
| HTTP Client | Axios |
| Auth | JWT (Bearer Token) |
| State | React Context API |

## 📂 Key Files

```
nextjs-client/
├── src/
│   ├── lib/
│   │   └── api-client.ts          # API integration (login, register, OTP)
│   ├── contexts/
│   │   └── AuthContext.tsx        # Global auth state
│   ├── components/
│   │   └── ProtectedRoute.tsx     # Route protection HOC
│   ├── types/
│   │   └── api.ts                 # TypeScript types
│   └── pages/
│       ├── login.tsx              # Login page
│       ├── register.tsx           # Registration page
│       ├── dashboard.tsx          # User dashboard
│       └── verify-otp.tsx         # OTP verification
├── .env.local                     # Environment config (create this!)
└── package.json                   # Dependencies
```

## 🔗 API Endpoints Used

| Endpoint | Method | Purpose |
|----------|--------|---------|
| `/auth/login` | POST | User authentication |
| `/auth/register` | POST | User registration |
| `/api/otp/generate` | POST | Send OTP to email |
| `/api/otp/verify` | POST | Verify OTP code |

## 💾 Local Storage

The app stores:
- `token`: JWT authentication token
- `user`: User object (id, email, fullName, role, etc.)

## 🎨 UI Components

### Form Inputs
- Text inputs with labels
- Email validation
- Password fields
- Select dropdowns (role selection)
- Form validation (HTML5 + required fields)

### Buttons
- Primary action buttons (blue)
- Logout button (red)
- Loading states (disabled when processing)

### Feedback
- Error messages (red background)
- Success messages (green background)
- Loading indicators

### Layout
- Centered forms on public pages
- Navigation bar on dashboard
- Responsive design (mobile-friendly)

## 🔒 Security Features

✅ JWT token-based authentication
✅ Automatic token expiration handling
✅ Protected routes redirect to login
✅ XSS protection via React
✅ HTTPS ready for production

## 🐛 Common Issues & Solutions

### "Cannot connect to API"
**Solution**: Ensure backend is running on port 8080 and NEXT_PUBLIC_API_URL is correct in .env.local

### "Token expired"
**Solution**: Login again (tokens expire after 24 hours)

### Build errors
**Solution**: 
```bash
rm -rf .next node_modules
npm install
npm run build
```

## 📊 Application Flow

```
User Visit
    ↓
Not Logged In?
    ↓
Login/Register Page
    ↓
Submit Form
    ↓
API Request (with credentials)
    ↓
Receive JWT Token
    ↓
Store in localStorage
    ↓
Redirect to Dashboard
    ↓
Dashboard (Protected)
    ↓
All API Requests Include Token
    ↓
Token Expires? → Redirect to Login
```

## 🎯 Testing Checklist

- [ ] Run backend API on port 8080
- [ ] Install Next.js dependencies
- [ ] Configure .env.local
- [ ] Start dev server
- [ ] Test registration with PARTNER role
- [ ] Test registration with MERCHANT role
- [ ] Test login with created accounts
- [ ] Verify dashboard shows correct role
- [ ] Test logout functionality
- [ ] Test OTP generation
- [ ] Test OTP verification
- [ ] Verify protected routes redirect when logged out

## 🚀 Production Deployment

### Environment Variables
Set in production:
```
NEXT_PUBLIC_API_URL=https://your-api-domain.com
```

### Build Commands
```bash
npm run build
npm start
```

### Deployment Platforms
- **Vercel**: Automatic Next.js deployment
- **Netlify**: Static site hosting
- **AWS/GCP/Azure**: Docker container deployment

## 📞 Support Resources

- [Next.js Documentation](https://nextjs.org/docs)
- [Tailwind CSS Documentation](https://tailwindcss.com/docs)
- [React Documentation](https://react.dev)
- Project README: `nextjs-client/README.md`
- Getting Started Guide: `nextjs-client/GETTING_STARTED.md`

## 📝 Notes

- The backend API must be running for the frontend to work
- Default API URL is http://localhost:8080
- Frontend runs on http://localhost:3000
- CORS must be enabled in the backend
- Partner and Merchant specific features are placeholders (backend endpoints not yet implemented)
