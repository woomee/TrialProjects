import { Pipe, PipeTransform } from '@angular/core';
import * as moment from 'moment'; // 追加

@Pipe({
  name: 'chatDate'   // -> html内に渡す名前
})
export class ChatDatePipe implements PipeTransform {

  transform(date: number): string {
    moment.locale('ja');
    return moment(date).format('LLLL');
  }

}
