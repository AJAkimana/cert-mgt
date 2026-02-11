import { HttpErrorResponse, HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';
import { catchError, throwError } from 'rxjs';
import { NotificationService } from '../services/notification.service';

const getErrorMessage = (error: HttpErrorResponse): string => {
  if (error.status === 0) {
    return 'Network error. Please check your connection.';
  }

  const payload = error.error as { message?: string; error?: string } | string | null;

  if (payload && typeof payload === 'object') {
    return payload.message || payload.error || 'Request failed.';
  }

  if (typeof payload === 'string' && payload.trim().length > 0) {
    return payload;
  }

  return error.message || 'Request failed.';
};

export const errorInterceptor: HttpInterceptorFn = (req, next) => {
  const notifier = inject(NotificationService);

  return next(req).pipe(
    catchError((error: HttpErrorResponse) => {
      notifier.error(getErrorMessage(error));
      return throwError(() => error);
    }),
  );
};
