import { Injectable } from '@angular/core';
import { User } from './user';

@Injectable({
  providedIn: 'root'
})
export class UserService {
  users: User[];

  constructor() {
    this.users = [
      { id: 1, name: 'Yamada', email: 'yamada@example.com' },
      { id: 2, name: 'Suzuki', email: 'suzuki@example.com' },
      { id: 3, name: 'Tanaka', email: 'tanaka@example.com' },
    ];    
  }

  getUsers(): User[] {
    return this.users;
  }

  getUser(id: number): User {
    return this.users.find(user => user.id === id);
  }

  setUser(user: User): void {
    for (let index = 0; index < this.users.length; index++) {
      if (this.users[index].id === user.id) {
        this.users[index] = user;
      }
    }
  }
}
