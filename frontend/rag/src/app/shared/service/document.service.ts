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

}
