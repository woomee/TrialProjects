import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';

import { ChatRoutingModule } from './chat-routing.module';
import { SharedModule } from '../shared/shared.module'; // 追加
import { ChatComponent } from './chat.component';
import { StoreModule } from '@ngrx/store';
import * as fromChat from './store/chat.reducer';
import { EffectsModule } from '@ngrx/effects';
import { ChatEffects } from './store/chat.effects'; // 追加

@NgModule({
  declarations: [
    ChatComponent
  ],
  imports: [
    CommonModule,
    ChatRoutingModule,
    SharedModule,
    StoreModule.forFeature(fromChat.chatsFeatureKey, fromChat.reducer),
    EffectsModule.forFeature([ChatEffects])
  ]
})
export class ChatModule { }
