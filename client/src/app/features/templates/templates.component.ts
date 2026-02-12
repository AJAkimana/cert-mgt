import { Component, DestroyRef, inject, OnInit, signal } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';
import { takeUntilDestroyed } from '@angular/core/rxjs-interop';

import { TemplateService } from '../../core/services/template.service';
import { NotificationService } from '../../core/services/notification.service';
import { Template } from '../../core/models/template.models';

@Component({
  selector: 'app-templates',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './templates.component.html',
})
export class TemplatesComponent implements OnInit {
  private readonly templateService = inject(TemplateService);
  private readonly notifier = inject(NotificationService);
  private readonly destroyRef = inject(DestroyRef);

  protected readonly templates = signal<Template[]>([]);
  protected readonly isLoading = signal(false);

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
}
