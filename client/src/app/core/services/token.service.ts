import { Injectable, inject, signal } from '@angular/core';
import { PLATFORM_ID } from '@angular/core';
import { isPlatformBrowser } from '@angular/common';

const TOKEN_KEY = 'auth_token';

@Injectable({ providedIn: 'root' })
export class TokenService {
  private readonly platformId = inject(PLATFORM_ID);
  private readonly token = signal<string | null>(this.readToken());

  private readToken(): string | null {
    if (!isPlatformBrowser(this.platformId)) {
      return null;
    }
    return localStorage.getItem(TOKEN_KEY);
  }

  private writeToken(token: string | null): void {
    if (!isPlatformBrowser(this.platformId)) {
      return;
    }
    if (token === null) {
      localStorage.removeItem(TOKEN_KEY);
      return;
    }
    localStorage.setItem(TOKEN_KEY, token);
  }

  setToken(token: string): void {
    this.writeToken(token);
    this.token.set(token);
  }

  getToken(): string | null {
    return this.token();
  }

  hasToken(): boolean {
    return !!this.token();
  }

  clear(): void {
    this.writeToken(null);
    this.token.set(null);
  }
}
