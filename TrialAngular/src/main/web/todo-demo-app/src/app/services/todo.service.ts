import { Injectable } from '@angular/core';
import { AngularFirestore } from '@angular/fire/firestore';
import { Todo } from 'src/app/interfaces/todo';

@Injectable({
  providedIn: 'root'
})
export class TodoService {

  constructor(private afs: AngularFirestore) {}

  createTodo(name: string, uid: string) {
    /* PKとなるidを生成してから追加する */
    const id = this.afs.createId();
    const params: Todo = {
      name,
      uid,
      id,
      list: 'todo',
    };
    this.afs.collection<Todo>('todos').doc(id).set(params);  
  }

  putTodo(params: Todo) {
    this.afs.collection<Todo>('todos').doc(params.id).set(params);
  }

  getTodos() {
    return this.afs.collection<Todo>('todos').valueChanges();
  }

  deleteTodo(id: string) {
    this.afs.collection<Todo>('todos').doc(id).delete();
  }
}
