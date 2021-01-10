import * as moment from 'moment';

export class Chat {
}

export class User { // Userの定義を追加
    uid?: string;
    name?: string;
  
    constructor(uid?: string, name?: string) {
        this.uid = uid;
        this.name = name;
    }

    deserialize() { // 追加
        return Object.assign({}, this);
    }
}

export class Session { // 追加
  login: boolean;
  user: User;

  constructor(init?: User) {
    this.login = (!!init);
    this.user = (init) ? new User(init.uid, init.name) : new User();
  }
  reset(): Session { // 追加
      this.login = false;
      return this;
  }
}

export class Comment { // Commentの定義を変更
    user: User;
    initial?: string;
    content: string;
    date: number;
    // Redux化でidに変更
    // key?: string;
    id?: string; 
    editFlag?: boolean;
  
    constructor(user: User, content: string) {
      this.user = user;
      this.initial = user.name?.slice(0, 1);
      this.content = content;
      this.date = +moment();
    }

    deserialize() { // 追加
        this.user = this.user.deserialize();
        return Object.assign({}, this);
    }

    // 追加時点の日付を反映し、更新フラグを付ける
    setData(date: number, key: string): Comment {
        this.date = date;
        this.id = key;
        this.editFlag = false;
        return this;
    }    
 }

 export class Password { // 追加
    name: string;
    email: string;
    password: string;
    passwordConfirmation: string;
  
    constructor() {
      this.name = ''
      this.email = '';
      this.password = '';
      this.passwordConfirmation = '';
    }
  
    reset(): void {
      this.name = '';
      this.email = '';
      this.password = '';
      this.passwordConfirmation = '';    
    }
}
