import { Injectable } from '@angular/core';
import { Router } from '@angular/router';
import { Subject } from 'rxjs';
import { Session } from '../class/chat';
@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public session = new Session();
  public sessionSubject = new Subject<Session>(); // 追加
  public sessionState = this.sessionSubject.asObservable(); // 追加

  constructor(private router: Router) { }

  /* login, logout時にnext()でストリームを流す */
  login(): void { // 追加
    this.session.login = true;
    this.sessionSubject.next(this.session);
    this.router.navigate(['/']);
  }

  logout(): void { // 追加
    this.sessionSubject.next(this.session.reset());
    this.router.navigate(['/account/login']);
  }

}
