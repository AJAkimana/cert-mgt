import { Injectable } from '@angular/core';
import { MatSnackBar, MatSnackBarConfig } from '@angular/material/snack-bar';

export type NotificationType = 'success' | 'error' | 'info';

@Injectable({ providedIn: 'root' })
export class NotificationService {
  private readonly defaultConfig: MatSnackBarConfig = {
    duration: 3000,
    horizontalPosition: 'end',
    verticalPosition: 'top',
  };

  constructor(private readonly snackBar: MatSnackBar) {}

  show(message: string, type: NotificationType = 'info', config?: MatSnackBarConfig): void {
    this.snackBar.open(message, 'OK', {
      ...this.defaultConfig,
      panelClass: [`app-snackbar`, `app-snackbar-${type}`],
      ...config,
    });
  }

  success(message: string, config?: MatSnackBarConfig): void {
    this.show(message, 'success', config);
  }

  error(message: string, config?: MatSnackBarConfig): void {
    this.show(message, 'error', config);
  }

  info(message: string, config?: MatSnackBarConfig): void {
    this.show(message, 'info', config);
  }
}
