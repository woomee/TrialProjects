import { Component, OnInit } from '@angular/core';
import { Observable, Subject } from 'rxjs';
import { Password } from 'src/app/class/chat';
import { SessionService } from 'src/app/service/session.service';

@Component({
  selector: 'app-login',
  templateUrl: './login.component.html',
  styleUrls: ['./login.component.scss']
})
export class LoginComponent implements OnInit {

  public loginObservable: Observable<string> = new Observable((observer => {  // 追加
    observer.next('test1');
    setTimeout(() => {
      observer.next('test2');
    }, 3000);
  }));

  public loginSubject: Subject<number> = new Subject();  // 変更
  public loginState = this.loginSubject.asObservable();  // 追加
  public count = 0;  // 追加
  public account = new Password();


  constructor(private sessionService: SessionService) { }

  ngOnInit(): void {
    this.loginObservable.subscribe((data: string) => { // 追加
      console.log(data);
    });

    this.loginState.subscribe((data: number) => { // 変更
      console.log(data);
    });
  }

  submitLogin() { // 追加
    // e.preventDefault();
    if (this.account.email != null && this.account.email.length > 0) {
      this.sessionService.login(this.account);
    }
    else {
      console.log("email is not set.")
    }
  }    


  clickNext() { // 追加
    this.count++;
    this.loginSubject.next(this.count);
  }  
}
