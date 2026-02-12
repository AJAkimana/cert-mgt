import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.models';
import { CertificateSummary } from '../models/certificate.models';

@Injectable({ providedIn: 'root' })
export class CertificateService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/certificates`;

  constructor(private readonly http: HttpClient) {}

  getCertificates(): Observable<ApiResponse<CertificateSummary[]>> {
    return this.http.get<ApiResponse<CertificateSummary[]>>(this.baseUrl);
  }

  getDownloadUrl(certificateId: string): string {
    return `${this.baseUrl}/${certificateId}/download`;
  }
}
