export interface LoginRequest {
  identifier: string; // email or username
  password: string;
}

export interface AuthResponse {
  token: string;
}

export interface RegisterRequest {
  customerName: string;
  customerEmail?: string;
  username: string;
  email: string;
  password: string;
}
