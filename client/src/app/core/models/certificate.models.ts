import { Template } from './template.models';

export interface Certificate {
  id: string;
  recipientName: string;
  recipientEmail?: string | null;
  status: string;
  issuedAt?: string | null;
  createdAt: string;
  updatedAt: string;
  template?: Template;
}
