import { Component, inject } from '@angular/core';
import { FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatDialogModule, MatDialogRef } from '@angular/material/dialog';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatButtonModule } from '@angular/material/button';

export interface AddCustomerFormValue {
  name: string;
  email?: string | null;
}

@Component({
  selector: 'app-add-customer-dialog',
  standalone: true,
  imports: [
    ReactiveFormsModule,
    MatDialogModule,
    MatFormFieldModule,
    MatInputModule,
    MatButtonModule,
  ],
  templateUrl: './add-customer-dialog.component.html',
})
export class AddCustomerDialogComponent {
  private readonly dialogRef = inject(MatDialogRef<AddCustomerDialogComponent>);
  private readonly fb = inject(FormBuilder);

  protected readonly form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(150)]],
    email: ['', [Validators.email, Validators.maxLength(150)]],
  });

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const value = this.form.value;
    this.dialogRef.close({
      name: value.name?.trim() ?? '',
      email: value.email?.trim() || null,
    } satisfies AddCustomerFormValue);
  }

  close(): void {
    this.dialogRef.close();
  }
}
