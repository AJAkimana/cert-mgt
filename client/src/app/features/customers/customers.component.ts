import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { MatTableModule } from '@angular/material/table';
import { MatButtonModule } from '@angular/material/button';
import { MatDialog, MatDialogModule } from '@angular/material/dialog';
import { MatIconModule } from '@angular/material/icon';
import { MatCardModule } from '@angular/material/card';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { CustomerService } from '../../core/services/customer.service';
import { NotificationService } from '../../core/services/notification.service';
import { Customer } from '../../core/models/customer.models';
import {
  AddCustomerDialogComponent,
  AddCustomerFormValue,
} from './dialogs/add-customer-dialog.component';

@Component({
  selector: 'app-customers',
  standalone: true,
  imports: [
    CommonModule,
    MatTableModule,
    MatButtonModule,
    MatDialogModule,
    MatIconModule,
    MatCardModule,
  ],
  templateUrl: './customers.component.html',
  styleUrl: './customers.component.scss',
})
export class CustomersComponent implements OnInit {
  private readonly customerService = inject(CustomerService);
  private readonly notifier = inject(NotificationService);
  private readonly dialog = inject(MatDialog);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly customers = signal<Customer[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly displayedColumns = ['name', 'email', 'status', 'createdAt'];

  ngOnInit(): void {
    this.loadCustomers();
  }

  openCreateDialog(): void {
    const dialogRef = this.dialog.open(AddCustomerDialogComponent, {
      width: '420px',
    });

    dialogRef
      .afterClosed()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe((value: AddCustomerFormValue | undefined) => {
        if (!value) {
          return;
        }
        this.createCustomer(value);
      });
  }

  private loadCustomers(): void {
    this.isLoading.set(true);
    this.customerService
      .getCustomers()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.isLoading.set(false);
          this.customers.set(res.data ?? []);
        },
        error: () => {
          this.customers.set([]);
          this.isLoading.set(false);
        },
        complete: () => {
          this.isLoading.set(false);
        },
      });
  }

  private createCustomer(payload: AddCustomerFormValue): void {
    this.customerService
      .createCustomer(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.notifier.success(res.message || 'Customer created');
          this.customers.update((current) => [...current, res.data]);
        },
        error: () => undefined,
      });
  }
}
