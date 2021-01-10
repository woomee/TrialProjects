import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
import { AngularFirestore } from '@angular/fire/firestore';
import { Router } from '@angular/router';
import { Observable, of, Subject } from 'rxjs';
import { map, switchMap, take } from 'rxjs/operators';
import { Session, Password, User } from '../class/chat';

@Injectable({
  providedIn: 'root'
})
export class SessionService {

  public session = new Session();
  public sessionSubject = new Subject<Session>(); // 追加
  public sessionState = this.sessionSubject.asObservable(); // 追加

  constructor(
    private router: Router,
    private afAuth: AngularFireAuth,
    private afs: AngularFirestore ) { 

      console.log('session service constructor session.login=' + this.session.login);
  }

  /* login, logout時にnext()でストリームを流す */
  login(account: Password): void { // 追加
    // this.session.login = true;
    // this.sessionSubject.next(this.session);
    // this.router.navigate(['/']);

    this.afAuth
      .signInWithEmailAndPassword(account.email, account.password)
      .then(auth => {
        if (!auth.user?.emailVerified) {
          this.afAuth.signOut();
          return Promise.reject('メールアドレスが確認できていません');
        }
        this.session.login = true;
        console.log('session service session.login=' + this.session.login);
        this.sessionSubject.next(this.session);
        return this.router.navigate(['/']);
      })
      // .then(() => alert('ログインしました。'))
      .catch(err => {
        console.log(err);
        alert('ログインに失敗しました\n' + err);
      });
  }

  logout(): void { // 追加
    // this.sessionSubject.next(this.session.reset());
    // this.router.navigate(['/account/login']);
    this.afAuth
      .signOut()
      .then(() => {
        this.sessionSubject.next(this.session.reset());
        return this.router.navigate(['/account/login']);
      })
      .then(() => {
        this.sessionSubject.next(this.session.reset());
        alert('ログアウトしました。')
      })
      .catch( err => {
        console.log(err);
        alert('ログアウトに失敗しました\n' + err);
      });
  }

  signup(account: Password): void {
    // this.afAuth
    //   .createUserWithEmailAndPassword(account.email, account.password)
    //   .then(auth => auth.user?.sendEmailVerification())
    //   .then(() => alert('メールアドレス確認メールを送信しました'))
    //   .catch(err => {
    //     console.log(err);
    //     alert('アカウント作成に失敗しました。\n' + err);
    //   })
    let auth: firebase.default.auth.UserCredential;
    this.afAuth
      .createUserWithEmailAndPassword(account.email, account.password) // アカウント作成
      .then((TEMP_AUTH: firebase.default.auth.UserCredential) => {
        auth = TEMP_AUTH;
        return auth.user?.sendEmailVerification(); // メールアドレス確認
      })
      .then(() => { // 追加
        return this.createUser(new User(auth.user?.uid, account.name));
      })
      .then(() => this.afAuth.signOut()) // 追加
      .then(() => {
        account.reset(); // 追加
        alert('メールアドレス確認メールを送信しました。');
      })
      .catch(err => {
        console.log(err);
        alert('アカウントの作成に失敗しました。\n' + err);
      });
  }

  // ログイン状況確認
  checkLogin(): void { // 追加
    this.afAuth
      .authState
      .pipe(
        switchMap(auth => {
          if (!auth) {
            return of(null);
          }
          else {
            return this.getUser(auth.uid);
          }
        })
      )
      .subscribe((user: User)  => {
        // ログイン状態を返り値の有無で判断
        this.session.login = (!!user);
        this.session.user = (user) ? user : new User();
        this.sessionSubject.next(this.session);
        console.log("checkLogin() session.login=" + this.session.login);
        
      });
  }
  // ログイン状況確認(State)
  checkLoginState(): Observable<Session> { // 追加
    return this.afAuth
      .authState
      .pipe(
        map(auth => {
          // ログイン状態を返り値の有無で判断
          this.session.login = (!!auth);
          console.log("checkLoginState() session.login=" + this.session.login);
          return this.session;
        })
      )
  }

 // ユーザーを作成
 private createUser(user: User): Promise<void> { // 追加
  return this.afs
    .collection('users')
    .doc(user.uid)
    .set(user.deserialize());
}

// ユーザーを取得
private getUser(uid: string): Observable<any> { // 追加
  console.log("getUser() arg uid=" + uid);
  return this.afs
    .collection<User>('users')
    .doc(uid)
    .valueChanges()
    .pipe(
      switchMap((user?: User) => {
        console.log("getUser() user=" + user);
        if (user) {
          return of(new User(uid, user.name));
        } else {
          return of(null);
        }
      })
    );
}  
}