import { Injectable } from '@angular/core';
import { AngularFireAuth } from '@angular/fire/auth';
// import { auth } from 'firebase/app';
// import * as firebase from 'firebase/app';
// import { auth } from '../../../node_modules/firebase';
import firebase from 'firebase/app';


@Injectable({
  providedIn: 'root'
})
export class AuthService {
  user$ = this.afAuth.user;

  constructor(private afAuth: AngularFireAuth) { }

  login() {
    this.afAuth
      .signInWithPopup(new firebase.auth.GoogleAuthProvider())
      .then((result) => {
        console.log('login:' + result);
      })
  }
  logout() {
    this.afAuth.signOut();
  }
}
