import { Component } from '@angular/core';
import { Comment, User } from './class/chat';
import { AngularFirestore } from '@angular/fire/firestore';
import { Observable } from 'rxjs';

const CURRENT_USER: User = new User(1, 'Tanaka Jiro'); // 自分のUser情報を追加
const ANOTHER_USER: User = new User(2, 'Suzuki Taro'); // 相手のUser情報を追加

const COMMENTS: Comment[] =[
  new Comment(ANOTHER_USER, 'Suzukiの１つ目のコメントです。'),
  new Comment(ANOTHER_USER, 'Suzukiの2つ目のコメントです。'),
  new Comment(CURRENT_USER, 'Tanakaの１つ目のコメントです。'),
  new Comment(ANOTHER_USER, 'Suzukiの3つ目のコメントです。'),
  new Comment(CURRENT_USER, 'Tanakaの2つ目のコメントです。')
];

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent {
  title = 'ng-chat-firebase';

  item: Observable<Comment | undefined>; // 追加
  public content = '';
  public comments = COMMENTS;
  public current_user = CURRENT_USER; // 追加

  // DI（依存性注入する機能を指定）
  constructor(private db: AngularFirestore) {
    // NG
    // this.item = db
    //   .collection('comments')
    //   .doc<Comment>('item')
    //   .valueChanges();

    this.item = db
      .collection<Comment>('comments')
      .doc('item')
      .valueChanges();
  }

  addComment(comment: string) {
    if (comment) {
      // this.comments.push(new Comment(this.current_user, comment));
      this.db.collection('comments')
             .add(new Comment(this.current_user, comment).deserialize());
      this.content = ''; 
    }
  }
}
