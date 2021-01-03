import { Component } from '@angular/core';
import { Comment, User } from './class/chat';
import { AngularFirestore } from '@angular/fire/firestore';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators'; // 追加

const CURRENT_USER: User = new User(1, 'Tanaka Jiro'); // 自分のUser情報を追加
const ANOTHER_USER: User = new User(2, 'Suzuki Taro'); // 相手のUser情報を追加



@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss']
})

export class AppComponent {
  title = 'ng-chat-firebase';

  public content = '';
  public comments: Observable<Comment[]>;
  public current_user = CURRENT_USER; // 追加

  // DI（依存性注入する機能を指定）
  constructor(private db: AngularFirestore) {

    this.comments = db
        .collection<Comment>('comments', ref => {
          // 日時の昇順で取得
          return ref.orderBy('date', 'asc');
        })
        .snapshotChanges()
        .pipe(
          map(actions => actions.map(action => {
            const data = action.payload.doc.data() as Comment;
            const commentData = new Comment(data.user, data.content);
            commentData.setData(data.date);
            return commentData;
          }))
        )
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
