// API Response Types
export interface ApiResponse<T> {
  status: string;
  message: string;
  data?: T;
}

// User Types
export interface User {
  id: number;
  email: string;
  fullName: string;
  phone?: string;
  role: 'PARTNER' | 'MERCHANT';
  status: string;
  createdAt: string;
}

// Auth Types
export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  email: string;
  password: string;
  fullName: string;
  phone?: string;
  role: 'PARTNER' | 'MERCHANT';
}

export interface AuthResponse {
  token: string;
  user: User;
}

// OTP Types
export interface OtpGenerateRequest {
  email: string;
}

export interface OtpVerifyRequest {
  email: string;
  otp: string;
}

export interface OtpResponse {
  verified: boolean;
  message: string;
}
