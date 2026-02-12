export interface Template {
  id: string;
  name: string;
  description?: string | null;
  rawTemplate: string;
  placeholders?: Record<string, unknown> | null;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
}

export interface CreateTemplateRequest {
  name: string;
  description?: string | null;
  rawTemplate: string;
  placeholders?: Record<string, unknown> | null;
  isActive?: boolean;
}
