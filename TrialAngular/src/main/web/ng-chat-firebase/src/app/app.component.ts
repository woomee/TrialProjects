import { Component } from '@angular/core';
// import { Comment, User } from './class/chat';
// import { AngularFirestore } from '@angular/fire/firestore';
// import { Observable } from 'rxjs';
// import { map } from 'rxjs/operators'; // 追加

// const CURRENT_USER: User = new User(1, 'Tanaka Jiro'); // 自分のUser情報を追加
// const ANOTHER_USER: User = new User(2, 'Suzuki Taro'); // 相手のUser情報を追加

@Component({
  selector: 'app-root',
  templateUrl: './app.component.html',
  styleUrls: ['./app.component.scss'],
  // template: `<app-chat></app-chat>`
  // templateUrl: './chat/chat.component.html'
})

export class AppComponent {
  // public content = '';
  // public comments: Observable<Comment[]>;
  // public current_user = CURRENT_USER; // 追加

  // DI（依存性注入する機能を指定）
  constructor() {

    // this.comments = db
    //     .collection<Comment>('comments', ref => {
    //       // 日時の昇順で取得
    //       return ref.orderBy('date', 'asc');
    //     })
    //     .snapshotChanges()
    //     .pipe(
    //       map(actions => actions.map(action => {
    //         const data = action.payload.doc.data() as Comment;
    //         const key = action.payload.doc.id; // 追加
    //         const commentData = new Comment(data.user, data.content);
    //         commentData.setData(data.date, key); // 更新
    //         return commentData;
    //       }))
    //     )
  }

  // addComment(comment: string) {
  //   if (comment) {
  //     // this.comments.push(new Comment(this.current_user, comment));
  //     this.db.collection('comments')
  //            .add(new Comment(this.current_user, comment).deserialize());
  //     this.content = ''; 
  //   }
  // }

  // // 編集フィールドの切り替え
  // toggleEditComment(comment: Comment) { // 追加
  //   comment.editFlag = (!comment.editFlag);
  // }

  // // コメントを更新する
  // saveEditComment(comment: Comment) { // 追加
  //   this.db
  //     .collection('comments')
  //     .doc(comment.key)
  //     .update({
  //       content: comment.content,
  //       date: comment.date
  //     })
  //     .then(() => {
  //       alert('コメントを更新しました');
  //       comment.editFlag = false;
  //     });
  // }

  // // コメントをリセットする
  // resetEditComment(comment: Comment) { // 追加
  //   comment.content = '';
  // }

  // // コメントを削除する
  // deleteComment(comment: Comment) { // 追加
  //   this.db
  //     .collection('comments')
  //     .doc(comment.key)
  //     .delete()
  //     .then(() => {
  //       alert('コメントを削除しました');
  //     });
  // }

}
