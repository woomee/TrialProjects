import { Component, OnInit } from '@angular/core';
import { Comment, User } from '../class/chat';
// import { AngularFirestore } from '@angular/fire/firestore';
// import { Observable } from 'rxjs';
// import { map } from 'rxjs/operators'; // 追加
import { SessionService } from '../service/session.service';
import { Store } from '@ngrx/store'; // 追加
import * as fromSession from '../app-store/reducers'; // 追加
import * as fromChat from './store/chat.reducer'; // 追加
import * as ChatActions from './store/chat.actions'; // 追加

const CURRENT_USER: User = new User('1', 'Tanaka Jiro'); // 自分のUser情報を追加
const ANOTHER_USER: User = new User('2', 'Suzuki Taro'); // 相手のUser情報を追加

@Component({
  selector: 'app-chat',
  templateUrl: './chat.component.html',
  styleUrls: ['./chat.component.scss']
})


export class ChatComponent implements OnInit {
  public content = '';
  // Redux化
  // public comments: Observable<Comment[]>;
  public comments: Comment[] = [];
  public current_user = CURRENT_USER;

  constructor(
    // private db: AngularFirestore,
    // private session: SessionService
    private chat: Store<fromChat.State>, // Redux化
    private store: Store<fromSession.State>
  ) {

    // Redux化
    // this.session
    //   .sessionState
    //   .subscribe(data => {
    //     this.current_user = data.user;
    //   })
    this.store.select(fromSession.getSession)
      .subscribe(data => {
        this.current_user = data.user;
      });
    this.chat.select(fromChat.selectAllChats)
      .subscribe((comments: Comment[]) => {
        this.comments = [];
        comments.forEach((comment: Comment) => {
          let comId:string = comment.id == undefined ? '' : comment.id;
          this.comments.push(
            new Comment(comment.user, comment.content)
              .setData(comment.date, comId)
          );
      });
    }); // 追加



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

    //         // return action.payload.doc.data();
    //       }))
    //     )
   }

  ngOnInit(): void {
    console.log("chat.component onInit");
    this.store.dispatch(ChatActions.loadChats({ chats: [] }));
  }

  addComment(comment: string) {
    // if (comment) {
    //   // this.comments.push(new Comment(this.current_user, comment));
    //   this.db.collection<Comment>('comments')
    //          .add(new Comment(this.current_user, comment).deserialize());
    //   this.content = ''; 
    // }
    if (comment) {
      const tmpComment = new Comment(this.current_user, comment).deserialize();
      this.chat.dispatch(ChatActions.addChat({ chat: tmpComment }));
      this.content = '';
    }


  }

  // 編集フィールドの切り替え
  toggleEditComment(comment: Comment) { 
    comment.editFlag = (!comment.editFlag);
  }

  // コメントを更新する
  saveEditComment(comment: Comment) { // 追加
    // this.db
    //   .collection('comments')
    //   .doc(comment.id)
    //   .update({
    //     content: comment.content,
    //     date: comment.date
    //   })
    //   .then(() => {
    //     alert('コメントを更新しました');
    //     comment.editFlag = false;
    //   });
    comment.editFlag = false;
    let comId:string = comment.id == undefined ? '' : comment.id;
    this.chat.dispatch(ChatActions.updateChat({ chat: { id: comId, changes: comment } }));
  }

  // コメントをリセットする
  resetEditComment(comment: Comment) { // 追加
    comment.content = '';
  }

  // コメント編集をやめる
  cancelEditComment(comment: Comment) { // 追加
    this.toggleEditComment(comment)
  }

  // コメントを削除する
  deleteComment(comment: Comment) { // 追加
    // this.db
    //   .collection('comments')
    //   .doc(comment.id)
    //   .delete()
    //   .then(() => {
    //     alert('コメントを削除しました');
    //   });
    let comId:string = comment.id == undefined ? '' : comment.id;
    this.chat.dispatch(ChatActions.deleteChat({ id: comId }));
  }
}
