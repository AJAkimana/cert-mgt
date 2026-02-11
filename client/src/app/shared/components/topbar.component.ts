import { Component } from '@angular/core';
import { MatToolbarModule } from '@angular/material/toolbar';

@Component({
  selector: 'app-topbar',
  standalone: true,
  imports: [MatToolbarModule],
  template: `<mat-toolbar color="primary">Sec CERTIFICATE</mat-toolbar>`,
})
export class TopbarComponent {}
