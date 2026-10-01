import { Injectable, signal } from '@angular/core';
import { Client, Message } from '@stomp/stompjs';

@Injectable({
  providedIn: 'root'
})
export class NotificationService {

  private stompClient!: Client;

  notifications = signal<string[]>([]);


  connect(username: string) {

    this.stompClient = new Client({

      brokerURL: 'ws://localhost:8080/ws',

      reconnectDelay: 5000,

      onConnect: () => {

        console.log('WebSocket connected');


        // Trade Notifications
        this.stompClient.subscribe(
          `/topic/trades/${username}`,
          (message: Message) => {

            console.log(
              'Trade Notification:',
              message.body
            );

            this.notifications.update(current => [
              message.body,
              ...current
            ]);
          }
        );


        // Wallet Notifications
        this.stompClient.subscribe(
          `/topic/walletupdate/${username}`,
          (message: Message) => {

            console.log(
              'Wallet Notification:',
              message.body
            );

            this.notifications.update(current => [
              message.body,
              ...current
            ]);
          }
        );

      },

      onStompError: (frame) => {
        console.log(
          'STOMP Error:',
          frame.headers['message']
        );
      }

    });


    this.stompClient.activate();
  }
}