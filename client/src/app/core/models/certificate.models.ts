import { Template } from './template.models';

export interface CertificateSummary {
  id: string;
  templateId: string;
  templateName: string;
  recipientName: string;
  recipientEmail?: string | null;
  status: string;
  issuedAt?: string | null;
  createdAt: string;
  updatedAt: string;
  template?: Template;
}
