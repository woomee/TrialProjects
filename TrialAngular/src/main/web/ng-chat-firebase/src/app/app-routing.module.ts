import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChatComponent } from './chat/chat.component';
import { PageNotFoundComponent } from './error/page-not-found/page-not-found.component';
import { AccountModule } from './account/account.module';
import { AuthGuard } from './guard/auth.guard';
import { LoginGuard } from './guard/login.guard';

const routes: Routes = [
  { path: 'test', redirectTo: '', pathMatch: 'full'}, // 追加
  { path: '', 
    // component: ChatComponent,
    loadChildren: () => import('./chat/chat.module').then(m => m.ChatModule),
    canActivate: [AuthGuard] },
  { path: 'account',
    loadChildren: () => import ('./account/account.module').then(m => m.AccountModule),
    canActivate: [LoginGuard] },
  //
  { path: '**', component: PageNotFoundComponent }
 ];

@NgModule({
  imports: [RouterModule.forRoot(routes), AccountModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
