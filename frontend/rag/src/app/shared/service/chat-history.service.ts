import { Injectable } from '@angular/core';
import { HttpClientAdapter } from '../http/http-client.adapter';
import HttpRequest from '../model/http-request.model';

@Injectable()
export class ChatHistoryService {

  constructor(private httpClient: HttpClientAdapter) {
  }

  public findAll() {
    return this.httpClient.findAll(HttpRequest.ofResource('/api/chat-history'));
  }

}
