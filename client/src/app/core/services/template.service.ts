import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { environment } from '../../../environments/environment';
import { ApiResponse } from '../models/api-response.models';
import { CreateTemplateRequest, Template } from '../models/template.models';

@Injectable({ providedIn: 'root' })
export class TemplateService {
  private readonly baseUrl = `${environment.apiBaseUrl}/api/templates`;

  constructor(private readonly http: HttpClient) {}

  createTemplate(payload: CreateTemplateRequest): Observable<ApiResponse<Template>> {
    return this.http.post<ApiResponse<Template>>(this.baseUrl, payload);
  }

  getTemplates(): Observable<ApiResponse<Template[]>> {
    return this.http.get<ApiResponse<Template[]>>(this.baseUrl);
  }
}
