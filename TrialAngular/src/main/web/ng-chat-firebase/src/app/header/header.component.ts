import { Component, OnInit } from '@angular/core';
import { Session } from '../class/chat';
import { SessionService } from '../service/session.service';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  public login = false; // htmlで利用するため変数化
  
  constructor(public sessionService: SessionService) { 
    this.login = this.sessionService.session.login;
  }

  ngOnInit(): void {
    this.sessionService.sessionState.subscribe((session: Session) => {
      if (session) {
        this.login = session.login;
      }
    });
    console.log('header-component-login:' + this.login); // 追加
  }

  logout(): void {  // 追加
    this.sessionService.logout();
  }
}
