import { Component, OnInit } from '@angular/core';
import { tap } from 'rxjs/operators';
import { TodoService } from 'src/app/services/todo.service';
import { AuthService } from 'src/app/services/auth.service';
import { FormBuilder, Validators } from '@angular/forms';
import { Todo } from 'src/app/interfaces/todo';
import { CdkDragDrop } from '@angular/cdk/drag-drop';

@Component({
  selector: 'app-todo',
  templateUrl: './todo.component.html',
  styleUrls: ['./todo.component.scss']
})
export class TodoComponent implements OnInit {
  constructor(
    private todo: TodoService,
    private auth: AuthService,
    private fb: FormBuilder
  ) {}

  todos$ = this.todo.getTodos().pipe(tap((data) => console.log(data)));
  user$ = this.auth.user$;
  lists = ['todo', 'doing', 'done', 'suspend'];
  form = this.fb.group({
    name: ['', [Validators.required, Validators.maxLength(10)]],
  });


  ngOnInit(): void {
  }

  /** Form画面から呼ばれるメソッド */
  createTodo(uid: string) {
    const name = this.form.controls.name.value;
    this.todo.createTodo(name, uid);
    this.form.reset();
    this.form.controls.name.setErrors({ required: null });
  }

  deleteTodo(id: string) {
    this.todo.deleteTodo(id);
  }

  filterTodo(filterList: string, todos: Todo[]): Todo[] {
    if (!todos) {
      return [];
    }
    return todos.filter((todo) => todo.list === filterList);
  }

  drop(event: CdkDragDrop<Todo[]>) {
    console.log(event);
    if (event.previousContainer !== event.container) {
      const list = event.container.id;
      const todo = event.item.data;
      todo.list = list;
      this.todo.putTodo(todo);
    }
  }


}
