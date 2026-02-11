import { CanActivateFn, Router } from '@angular/router';
import { inject } from '@angular/core';
import { TokenService } from '../services/token.service';

export const guestGuard: CanActivateFn = () => {
  const token = inject(TokenService).getToken();
  const router = inject(Router);

  if (token) {
    router.navigate(['/dashboard']);
    return false;
  }
  return true;
};
