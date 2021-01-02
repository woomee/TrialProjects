export interface Todo {
    uid: string;
    name: string;
    list: 'todo' | 'doing' | 'done' | 'suspend';
    id: string;
}
