import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
// import { FormsModule } from '@angular/forms';
import { environment } from 'src/environments/environment'; // 追加
import { AngularFireModule } from '@angular/fire'; // 追加
import { AngularFirestoreModule } from '@angular/fire/firestore'; // 追加
import { AngularFireAuthModule } from '@angular/fire/auth'; // 追加


import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './app.component';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
// import { ChatDatePipe } from './shared/pipe/chat-date.pipe';
import { ChatComponent } from './chat/chat.component';
import { SharedModule } from './shared/shared.module';
import { HeaderComponent } from './header/header.component';


@NgModule({
  declarations: [
    AppComponent,
    ChatComponent,
    HeaderComponent
  ],
  imports: [
    BrowserModule,
    AppRoutingModule,
    SharedModule,
    NgbModule,
    AngularFireModule.initializeApp(environment.firebase),
    AngularFirestoreModule,  // 追加
    AngularFireAuthModule  // 追加
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
