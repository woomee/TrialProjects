import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { ChatComponent } from './chat/chat.component';
import { PageNotFoundComponent } from './error/page-not-found/page-not-found.component';
import { AccountModule } from './account/account.module';

const routes: Routes = [
  { path: 'test', redirectTo: '', pathMatch: 'full'}, // 追加
  { path: '', component: ChatComponent },
  { path: 'account',
    loadChildren: () => import ('./account/account.module').then(m => m.AccountModule) },
  //
  { path: '**', component: PageNotFoundComponent }
 ];

@NgModule({
  imports: [RouterModule.forRoot(routes), AccountModule],
  exports: [RouterModule]
})
export class AppRoutingModule { }
