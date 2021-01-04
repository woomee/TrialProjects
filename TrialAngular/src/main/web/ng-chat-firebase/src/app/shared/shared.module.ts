import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from "@angular/forms"; // 追加
import { ChatDatePipe } from "./pipe/chat-date.pipe"; // 追加


@NgModule({
  declarations: [
    ChatDatePipe
  ],
  imports: [
    CommonModule,
    FormsModule
  ],
  exports: [  // 追加
    CommonModule,
    FormsModule,
    ChatDatePipe
  ]
})
export class SharedModule { }
