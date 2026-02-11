import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Auth, LoginRequest, RegisterRequest } from '../models/auth.models';
import { Observable, tap } from 'rxjs';
import { TokenService } from './token.service';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.models';

@Injectable({ providedIn: 'root' })
export class AuthService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/auth`;

  constructor(
    private readonly http: HttpClient,
    private readonly tokenService: TokenService,
  ) {}

  login(payload: LoginRequest): Observable<ApiResponse<Auth>> {
    return this.http.post<ApiResponse<Auth>>(`${this.baseUrl}/login`, payload).pipe(
      tap((res) => {
        console.log(res);

        return this.tokenService.setToken(res.data.token);
      }),
    );
  }

  register(payload: RegisterRequest): Observable<void> {
    return this.http.post<void>(`${this.baseUrl}/register`, payload);
  }

  logout(): void {
    this.tokenService.clear();
  }
}
