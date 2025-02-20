import { Injectable } from '@angular/core';
import { HttpClientAdapter } from "../http/http-client.adapter";
import { PageParameter } from "../model/page-parameter.model";
import HttpRequest from "../model/http-request.model";

@Injectable()
export class DocumentService {

    constructor(private httpClient: HttpClientAdapter) {
    }

    public findAll(page: PageParameter) {
        return this.httpClient.findAll(HttpRequest.ofResource('/api/documents').withPageParameter(page));
    }

    public upload(file: File) {

      const formData = new FormData();
      formData.append('file', file);

      return this.httpClient.post(HttpRequest.ofResourceAndBody('/api/documents', formData))
    }

    public answer(question: string) {
      return this.httpClient.post(HttpRequest.ofResourceAndBody('/api/documents/answer', question))
    }

}
