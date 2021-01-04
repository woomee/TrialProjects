import * as moment from 'moment';

export class Chat {
}

export class Session { // 追加
    login: boolean;
  
    constructor() {
      this.login = false;
    }
    reset(): Session { // 追加
        this.login = false;
        return this;
    }
}

export class User { // Userの定義を追加
    uid: number;
    name: string;
  
    constructor(uid: number, name: string) {
        this.uid = uid;
        this.name = name;
    }

    deserialize() { // 追加
        return Object.assign({}, this);
    }
}
  
export class Comment { // Commentの定義を変更
    user: User;
    initial: string;
    content: string;
    date: number;
    key?: string; // 追加
    editFlag?: boolean; // 追加
  
    constructor(user: User, content: string) {
      this.user = user;
      this.initial = user.name.slice(0, 1);
      this.content = content;
      this.date = +moment();
    }

    deserialize() { // 追加
        this.user = this.user.deserialize();
        return Object.assign({}, this);
    }

    // 追加時点の日付を反映し、更新フラグを付ける
    setData(date: number, key: string): Comment { // 追加
        this.date = date;
        this.key = key;
        this.editFlag = false;
        return this;
    }    
 }
