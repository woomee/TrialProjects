import { Injectable } from '@angular/core';
import { CanActivate, ActivatedRouteSnapshot, RouterStateSnapshot, UrlTree, Router } from '@angular/router';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { SessionService } from '../service/session.service';

@Injectable({
  providedIn: 'root'
})
export class AuthGuard implements CanActivate {

  constructor(
    private session: SessionService,
    private router: Router
  ) {  }
  
  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    // trueならば遷移, falseならば遷移しない
    // return true;
    return this.session
      .checkLoginState()
      .pipe(
        map(session => {
          if (!session.login) { 
            this.router.navigate([ '/account/login' ]);
          }
          return session.login;
        })
      );
  }
  
}
