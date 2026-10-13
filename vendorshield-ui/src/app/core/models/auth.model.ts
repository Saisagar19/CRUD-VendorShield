export interface LoginRequest {
  username: string;
  password: string;
}

export interface AuthResponse {
  accessToken: string;
  tokenType: string;
  username: string;
  role: string;
}

export interface AuthUser {
  username: string;
  role: string;
  accessToken: string;
}

export type RoleType = 'ROLE_ADMIN' | 'ROLE_MANAGER' | 'ROLE_VIEWER' | string;
