import { Component, OnInit } from '@angular/core';
import { Password } from 'src/app/class/chat';
import { SessionService } from 'src/app/service/session.service';

@Component({
  selector: 'app-sign-up',
  templateUrl: './sign-up.component.html',
  styleUrls: ['./sign-up.component.scss']
})
export class SignUpComponent implements OnInit {

  constructor(private session: SessionService) { }

  public account = new Password();

  ngOnInit(): void {
  }

  // アカウント作成
  submitSignUp(e: Event): void { // 追加
    e.preventDefault();
    // パスワード確認
    if (this.account.password !== this.account.passwordConfirmation) {
      alert('パスワードが異なります。');
      return;
    }
    this.session.signup(this.account);
    this.account.reset();
  }
  
}
