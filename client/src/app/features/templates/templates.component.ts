import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { TemplateService } from '../../core/services/template.service';
import { NotificationService } from '../../core/services/notification.service';
import { Template } from '../../core/models/template.models';

@Component({
  selector: 'app-templates',
  standalone: true,
  imports: [CommonModule, RouterLink, ReactiveFormsModule],
  templateUrl: './templates.component.html',
})
export class TemplatesComponent implements OnInit {
  private readonly templateService = inject(TemplateService);
  private readonly notifier = inject(NotificationService);
  private readonly destroyRef = inject(DestroyRef);
  private readonly fb = inject(FormBuilder);

  protected readonly templates = signal<Template[]>([]);
  protected readonly isLoading = signal(false);
  protected readonly isGenerating = signal(false);
  protected readonly isModalOpen = signal(false);
  protected readonly selectedTemplate = signal<Template | null>(null);
  protected readonly generateForm = signal<FormGroup>(this.fb.group({}));

  ngOnInit(): void {
    this.loadTemplates();
  }

  private loadTemplates(): void {
    this.isLoading.set(true);
    this.templateService
      .getTemplates()
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (res) => {
          this.templates.set(res.data ?? []);
          this.isLoading.set(false);
        },
        error: () => {
          this.templates.set([]);
          this.isLoading.set(false);
          this.notifier.error('Failed to load templates');
        },
      });
  }

  openGenerateModal(template: Template): void {
    this.selectedTemplate.set(template);
    this.generateForm.set(this.buildGenerateForm(template));
    this.isModalOpen.set(true);
  }

  closeGenerateModal(): void {
    this.isModalOpen.set(false);
    this.selectedTemplate.set(null);
  }

  submitGenerate(): void {
    const template = this.selectedTemplate();
    if (!template) {
      return;
    }

    const form = this.generateForm();
    if (form.invalid) {
      form.markAllAsTouched();
      return;
    }

    this.isGenerating.set(true);
    this.templateService
      .generateCertificate(template.id, form.value)
      .pipe(takeUntilDestroyed(this.destroyRef))
      .subscribe({
        next: (blob) => {
          this.downloadBlob(blob, template.name);
          this.isGenerating.set(false);
          this.closeGenerateModal();
        },
        error: () => {
          this.isGenerating.set(false);
          this.notifier.error('Failed to generate certificate');
        },
      });
  }

  getPlaceholderEntries(template: Template): Array<{ key: string; type: string; format?: string }> {
    const placeholders = template.placeholders ?? {};
    if (typeof placeholders !== 'object') {
      return [];
    }

    return Object.entries(placeholders).map(([key, value]) => {
      if (value && typeof value === 'object') {
        const typed = value as { type?: string; format?: string };
        return { key, type: typed.type ?? 'text', format: typed.format };
      }
      return { key, type: 'text' };
    });
  }

  inputType(type: string): string {
    switch (type) {
      case 'date':
        return 'date';
      case 'number':
        return 'number';
      case 'image':
        return 'url';
      default:
        return 'text';
    }
  }

  private buildGenerateForm(template: Template): FormGroup {
    const controls: Record<string, unknown> = {};
    for (const entry of this.getPlaceholderEntries(template)) {
      controls[entry.key] = this.fb.control('', Validators.required);
    }
    return this.fb.group(controls);
  }

  private downloadBlob(blob: Blob, name: string): void {
    const url = URL.createObjectURL(blob);
    const anchor = document.createElement('a');
    anchor.href = url;
    anchor.download = `${name || 'certificate'}.pdf`;
    anchor.click();
    URL.revokeObjectURL(url);
  }
}
