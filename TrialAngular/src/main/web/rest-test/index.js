var express = require('express');
var bodyParser = require('body-parser');
var app = express();
app.listen(8888);
app.use(bodyParser.json());
app.use(function(req, res, next) {
  res.header("Access-Control-Allow-Origin", "*");     // セキュリティリスク有り
  res.header("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept");
  next();
});

let users = [
  { id: 1, name: "Yamada", email: "yamada@example.com" },
  { id: 2, name: "Tanaka", email: "tanaka@example.com" },
  { id: 3, name: "Suzuki", email: "suzuki@example.com" }
];

app.get('/users', function(req, res) {
  res.send(JSON.stringify(users));
});

app.post('/users', function(req, res) {
  users.push(req.body);
  res.end();
});

app.get('/users/:id', function(req, res) {
  for (let i = 0; i < users.length; i++) {
    if (users[i].id == req.params.id) {
      res.send(JSON.stringify(users[i]));
    }
  }
});

app.post('/users/:id', function(req, res) {
  for (let i = 0; i < users.length; i++) {
    if (users[i].id == req.params.id) {
      users[i] = req.body;
    }
  }
  res.end();
});

app.delete('/users/:id', function(req, res) {
  for (let i = 0; i < users.length; i++) {
    if (users[i].id == req.params.id) {
      users.splice(i, 1);
    }
  }
  res.end();
});