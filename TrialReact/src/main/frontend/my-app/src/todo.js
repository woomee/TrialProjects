import React, { Component } from 'react';

export default class Todo extends Component {

  constructor(props) {
    super(props);
    this.state = {
      todos: [],
      name: ''
    };
  }

  onInput = (e) => {
    console.log("onInput: " + e.target.value);
    this.setState({
      name: e.target.value
    });
  }

  addTodo = () => {
    console.log("addTodo: " + this.state);
    const { todos, name } = this.state;
    // this.setState({
    //   // todos: [...todos, name]
    //   // todos: [todos.push(name)]    NG
    // });
    todos.push(name);
    this.setState({todos: todos});
  }

  removeTodo = (index) => {
    const { todos, name } = this.state;
    // this.setState({
    //   todos: [...todos.slice(0, index), ...todos.slice(index + 1)]
    // });
    todos.splice(index, 1);
    this.setState({todos: todos});
  }

  render() {
    const { todos } = this.state;
    return (<div>
      <input type="text" onInput={this.onInput} />
      <button onClick={this.addTodo}>登録</button>
      <ul>
        {
            todos.map((todo, index) =>
                <li key={index}>
                    {todo}
                    <button onClick={() => { this.removeTodo(index) }}>削除</button>
                </li>)
        }
      </ul>
    </div>);
  }
}