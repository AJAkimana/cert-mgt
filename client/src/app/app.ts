import { Component, computed, inject, signal } from '@angular/core';
import { Router, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NgIf } from '@angular/common';
import { MatListModule } from '@angular/material/list';
import { MatSidenavModule } from '@angular/material/sidenav';
import { TopbarComponent } from './shared/components/topbar.component';
import { AuthService } from './core/services/auth.service';
import { TokenService } from './core/services/token.service';

@Component({
  selector: 'app-root',
  imports: [
    NgIf,
    MatListModule,
    MatSidenavModule,
    RouterLink,
    RouterLinkActive,
    RouterOutlet,
    TopbarComponent,
  ],
  templateUrl: './app.html',
  styleUrl: './app.scss',
})
export class App {
  private readonly auth = inject(AuthService);
  private readonly router = inject(Router);
  private readonly tokenService = inject(TokenService);

  protected readonly isLoggedIn = computed(() => this.tokenService.hasToken());
  protected readonly title = signal('client');

  logout(): void {
    this.auth.logout();
    this.router.navigate(['/auth/login']);
  }
}
