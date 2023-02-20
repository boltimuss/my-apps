import { Injectable } from '@angular/core';

export interface Subscriber {
  onMessageReceived(message:Object): void
}

@Injectable({
  providedIn: 'root'
})
export class MessageBusService {

  private static subscribers:Map<String, Subscriber[]> = new Map();

  private constructor() { }

  public static sendMessage(topic:String, message:Object)
  {
    MessageBusService.subscribers.get(topic)?.forEach(function (value) {
      value.onMessageReceived(message);
    });
  }

  public static subscribe(topic:string, subscriber:Subscriber)
  {

    if (MessageBusService.subscribers.get(topic) == null)
    {
      MessageBusService.subscribers.set(topic, []);
    }

    MessageBusService.subscribers.get(topic)?.push(subscriber);
  }
}
