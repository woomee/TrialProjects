import * as moment from 'moment';

export class Chat {
}

export class User { // Userの定義を追加
    uid: number;
    name: string;
  
    constructor(uid: number, name: string) {
        this.uid = uid;
        this.name = name;
    }
}
  
export class Comment { // Commentの定義を変更
    user: User;
    initial: string;
    content: string;
    date: number;
  
    constructor(user: User, content: string) {
      this.user = user;
      this.initial = user.name.slice(0, 1);
      this.content = content;
      this.date = +moment();
    }
 }
