import { Component } from '@angular/core';
import { SessionService } from './service/session.service';

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  // template: `<app-chat></app-chat>`
})

export class AppComponent {

  constructor(private session: SessionService) {
    this.session.checkLogin();
  }
}
