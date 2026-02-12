import { Component, DestroyRef, inject, signal, PLATFORM_ID } from '@angular/core';
import { CommonModule, isPlatformBrowser } from '@angular/common';
import { FormArray, FormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatFormFieldModule } from '@angular/material/form-field';
import { MatInputModule } from '@angular/material/input';
import { MatSlideToggleModule } from '@angular/material/slide-toggle';
import { QuillModule } from 'ngx-quill';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { TemplateService } from '../../core/services/template.service';
import { NotificationService } from '../../core/services/notification.service';
import { CreateTemplateRequest } from '../../core/models/template.models';

@Component({
  selector: 'app-template-create',
  standalone: true,
  imports: [
    CommonModule,
    ReactiveFormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatSlideToggleModule,
    QuillModule,
  ],
  templateUrl: './template-create.component.html',
})
export class TemplateCreateComponent {
  private readonly templateService = inject(TemplateService);
  private readonly notifier = inject(NotificationService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly platformId = inject(PLATFORM_ID);
  private readonly fb = inject(FormBuilder);

  protected readonly isBrowser = isPlatformBrowser(this.platformId);
  protected readonly isSaving = signal(false);
  protected readonly placeholderTypes = ['text', 'date', 'number', 'image'];

  protected readonly form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(150)]],
    description: ['', [Validators.maxLength(1000)]],
    rawTemplate: ['', [Validators.required]],
    placeholders: this.fb.array([this.createPlaceholderGroup()]),
    isActive: [true],
  });

  get placeholdersArray(): FormArray {
    return this.form.get('placeholders') as FormArray;
  }

  addPlaceholder(): void {
    this.placeholdersArray.push(this.createPlaceholderGroup());
  }

  removePlaceholder(index: number): void {
    if (this.placeholdersArray.length <= 1) {
      return;
    }
    this.placeholdersArray.removeAt(index);
  }

  insertToken(index: number): void {
    const group = this.placeholdersArray.at(index);
    const key = group.get('key')?.value?.trim();
    if (!key) {
      this.notifier.error('Add a placeholder key first');
      return;
    }
    const raw = this.form.value.rawTemplate ?? '';
    const token = ` {${key}}`;
    this.form.patchValue({ rawTemplate: raw + token });
  }

  submit(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    const rawTemplate = this.form.value.rawTemplate ?? '';
    const placeholders = this.buildPlaceholders(rawTemplate);
    if (!placeholders) {
      return;
    }

    const payload: CreateTemplateRequest = {
      name: this.form.value.name ?? '',
      description: this.form.value.description || null,
      rawTemplate: this.form.value.rawTemplate ?? '',
      placeholders,
      isActive: !!this.form.value.isActive,
    };

    this.isSaving.set(true);
    this.templateService
      .createTemplate(payload)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.notifier.success(res.message || 'Template created');
          this.form.reset({
            name: '',
            description: '',
            rawTemplate: '',
            isActive: true,
          });
          this.placeholdersArray.clear();
          this.placeholdersArray.push(this.createPlaceholderGroup());
          this.isSaving.set(false);
        },
        error: () => {
          this.isSaving.set(false);
        },
      });
  }

  private createPlaceholderGroup() {
    return this.fb.group({
      key: ['', [Validators.required, Validators.maxLength(60)]],
      type: ['text', [Validators.required]],
      format: [''],
    });
  }

  private buildPlaceholders(rawTemplate: string): Record<string, unknown> | null {
    const placeholders: Record<string, { type: string; format?: string }> = {};
    const missingTokens: string[] = [];

    for (const control of this.placeholdersArray.controls) {
      const key = control.get('key')?.value?.trim();
      const type = control.get('type')?.value?.trim();
      const format = control.get('format')?.value?.trim();

      if (!key || !type) {
        this.notifier.error('Placeholder name and type are required');
        return null;
      }

      if (placeholders[key]) {
        this.notifier.error(`Duplicate placeholder: ${key}`);
        return null;
      }

      placeholders[key] = format ? { type, format } : { type };

      if (!rawTemplate.includes(`{${key}}`)) {
        missingTokens.push(key);
      }
    }

    if (missingTokens.length > 0) {
      this.notifier.error(
        `Insert tokens into the template for: ${missingTokens.map((key) => `{${key}}`).join(', ')}`,
      );
      return null;
    }

    return placeholders;
  }
}
