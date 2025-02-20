import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import HttpRequest from "../model/http-request.model";

@Injectable()
export class HttpClientAdapter {
  constructor(private readonly _httpClient: HttpClient) {}

  public findAll(request: HttpRequest): Observable<any> {
    return this._httpClient.get(`${request.resource}?${request.pageQuery}`);
  }

  public get(request: HttpRequest): Observable<any> {
    return this._httpClient.get(request.resource);
  }

  public post(request: HttpRequest): Observable<any> {
    return this._httpClient.post(request.resource, request.body);
  }

  public patch(request: HttpRequest): Observable<any> {
    return this._httpClient.patch(request.resource, request.body);
  }

  public delete(request: HttpRequest): Observable<any> {
    return this._httpClient.delete(request.resource);
  }
}
