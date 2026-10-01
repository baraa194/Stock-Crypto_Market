import { Component,inject,OnInit } from '@angular/core';
import { ActivatedRoute, RouterLink, RouterLinkActive, RouterOutlet } from '@angular/router';
import { NotificationService } from './services/notification.service';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [
    RouterOutlet,
    RouterLink,
   RouterLinkActive
  ],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent  implements OnInit {
    notificationService = inject(NotificationService);
  title = 'stock_crypto_market';
  showNotifications = false;

toggleNotifications() {
  this.showNotifications = !this.showNotifications;
}

    ngOnInit(): void {
    const username = 'baraa';

   this.notificationService.connect(username);
  }
}
