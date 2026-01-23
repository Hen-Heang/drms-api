import axios, { AxiosInstance, AxiosError } from 'axios';
import { 
  LoginRequest, 
  RegisterRequest, 
  AuthResponse, 
  OtpGenerateRequest, 
  OtpVerifyRequest,
  OtpResponse,
  ApiResponse 
} from '@/types/api';

const API_URL = process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8080';

class ApiClient {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: API_URL,
      headers: {
        'Content-Type': 'application/json',
      },
    });

    // Add request interceptor to include JWT token
    this.client.interceptors.request.use(
      (config) => {
        const token = this.getToken();
        if (token) {
          config.headers.Authorization = `Bearer ${token}`;
        }
        return config;
      },
      (error) => Promise.reject(error)
    );

    // Add response interceptor to handle errors
    this.client.interceptors.response.use(
      (response) => response,
      (error: AxiosError) => {
        if (error.response?.status === 401) {
          // Token expired or invalid
          this.removeToken();
          if (typeof window !== 'undefined') {
            window.location.href = '/login';
          }
        }
        return Promise.reject(error);
      }
    );
  }

  // Token management
  getToken(): string | null {
    if (typeof window !== 'undefined') {
      return localStorage.getItem('token');
    }
    return null;
  }

  setToken(token: string): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem('token', token);
    }
  }

  removeToken(): void {
    if (typeof window !== 'undefined') {
      localStorage.removeItem('token');
      localStorage.removeItem('user');
    }
  }

  // User management
  setUser(user: any): void {
    if (typeof window !== 'undefined') {
      localStorage.setItem('user', JSON.stringify(user));
    }
  }

  getUser(): any | null {
    if (typeof window !== 'undefined') {
      const user = localStorage.getItem('user');
      return user ? JSON.parse(user) : null;
    }
    return null;
  }

  // Auth API
  async login(data: LoginRequest): Promise<AuthResponse> {
    const response = await this.client.post<ApiResponse<AuthResponse>>('/auth/login', data);
    if (response.data.data) {
      this.setToken(response.data.data.token);
      this.setUser(response.data.data.user);
      return response.data.data;
    }
    throw new Error(response.data.message || 'Login failed');
  }

  async register(data: RegisterRequest): Promise<AuthResponse> {
    const response = await this.client.post<ApiResponse<AuthResponse>>('/auth/register', data);
    if (response.data.data) {
      this.setToken(response.data.data.token);
      this.setUser(response.data.data.user);
      return response.data.data;
    }
    throw new Error(response.data.message || 'Registration failed');
  }

  async logout(): Promise<void> {
    this.removeToken();
  }

  // OTP API
  async generateOtp(data: OtpGenerateRequest): Promise<string> {
    const response = await this.client.post<ApiResponse<string>>('/api/otp/generate', data);
    return response.data.message || 'OTP sent';
  }

  async verifyOtp(data: OtpVerifyRequest): Promise<OtpResponse> {
    const response = await this.client.post<ApiResponse<OtpResponse>>('/api/otp/verify', data);
    if (response.data.data) {
      return response.data.data;
    }
    throw new Error(response.data.message || 'OTP verification failed');
  }
}

export const apiClient = new ApiClient();
