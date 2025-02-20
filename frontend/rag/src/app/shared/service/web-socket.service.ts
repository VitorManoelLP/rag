import { Injectable } from '@angular/core';
import { RxStomp } from '@stomp/rx-stomp';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class WebSocketService {
  private rxStomp: RxStomp;

  constructor() {
    this.rxStomp = new RxStomp();
    this.initializeWebSocketConnection();
  }

  private initializeWebSocketConnection() {
    const config = {
      brokerURL: 'ws://localhost:8080/ws',
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000,
      debug: (msg: string) => {
        console.log(msg);
      }
    };

    this.rxStomp.configure(config);
    this.rxStomp.activate();
  }

  public fromEvent(topic: string): Observable<any> {
    return this.rxStomp
      .watch(`/topic/${topic}`)
      .pipe(
        map(message => {
          console.log(message);
          if (message.body) {
            return JSON.parse(message.body);
          }
          return null;
        })
      );
  }

  public async  disconnect() {
    await this.rxStomp.deactivate();
  }

}
