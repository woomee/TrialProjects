import { Component, OnInit } from '@angular/core';
import { Session } from '../class/chat';
import { SessionService } from '../service/session.service';
import { Store } from '@ngrx/store'; // 追加
import * as fromSession from '../app-store/reducers'; // 追加
import { Observable } from 'rxjs';
import * as fromChat from '../chat/store/chat.reducer';

@Component({
  selector: 'app-header',
  templateUrl: './header.component.html',
  styleUrls: ['./header.component.scss']
})
export class HeaderComponent implements OnInit {

  // public login = false; // htmlで利用するため変数化
  public session$: Observable<Session>; // 追加
  public loadingSession$: Observable<boolean>; // 追加
  public loadingChat$: Observable<boolean>; // 追加

  constructor (
    public sessionService: SessionService,
    private store: Store<fromSession.State>,
    private chat: Store<fromChat.State>
  ) { 
    // this.login = this.sessionService.session.login;
    this.session$ = this.store.select(fromSession.getSession);
    this.loadingSession$ = this.store.select(fromSession.getLoading);
    this.loadingChat$ = this.chat.select(fromChat.getChatLoading); // 追加
  }

  ngOnInit(): void {

    // Redux化
    // this.sessionService.sessionState.subscribe((session: Session) => {
    //   if (session) {
    //     this.login = session.login;
    //   }
    // });
    // console.log('header-component-login:' + this.login); // 追加
  }

  logout(): void {  // 追加
    this.sessionService.logout();
  }
}
