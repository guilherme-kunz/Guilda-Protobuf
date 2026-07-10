'use strict';

const express = require('express');
const protobuf = require('protobufjs');
const path = require('path');

const app = express();
const PORT = 3000;

let nextId = 5;

const MOCK_USERS = [
  { id: '1', name: 'Alice Silva',   email: 'alice@guilda.com',  roles: [1, 3] },
  { id: '2', name: 'Bob Santos',    email: 'bob@guilda.com',    roles: [2, 3] },
  { id: '3', name: 'Carol Martins', email: 'carol@guilda.com',  roles: [1, 2, 3] },
  { id: '4', name: 'Diego Ramos',   email: 'diego@guilda.com',  roles: [3] },
];

async function bootstrap() {
  const root = await protobuf.load(path.join(__dirname, '../proto/user.proto'));

  const UserList          = root.lookupType('guilda.UserList');
  const User              = root.lookupType('guilda.User');
  const CreateUserRequest = root.lookupType('guilda.CreateUserRequest');

  // ── GET /api/v1/users ──────────────────────────────────────────────────────
  app.get('/api/v1/users', (req, res) => {
    const accept = req.headers['accept'] || '';

    if (accept.includes('application/x-protobuf')) {
      const buffer = UserList.encode(UserList.create({ users: MOCK_USERS })).finish();
      res.set('Content-Type', 'application/x-protobuf');
      res.send(Buffer.from(buffer));
      console.log(`[PROTOBUF] GET  /api/v1/users → ${buffer.length} bytes`);
    } else {
      const json = JSON.stringify({ users: MOCK_USERS });
      res.set('Content-Type', 'application/json');
      res.send(json);
      console.log(`[JSON]     GET  /api/v1/users → ${Buffer.byteLength(json)} bytes`);
    }
  });

  // ── POST /api/v1/users ─────────────────────────────────────────────────────
  // express.raw() lê o body binário apenas para este endpoint.
  app.post('/api/v1/users', express.raw({ type: 'application/x-protobuf' }), (req, res) => {
    const accept = req.headers['accept'] || '';

    try {
      const request = CreateUserRequest.decode(req.body);
      const error   = CreateUserRequest.verify(request);
      if (error) return res.status(400).json({ error });

      const newUser = {
        id:    String(nextId++),
        name:  request.name,
        email: request.email,
        roles: request.roles || [],
      };

      MOCK_USERS.push(newUser);
      console.log(`[CREATE]   POST /api/v1/users → id=${newUser.id} name="${newUser.name}"`);

      if (accept.includes('application/x-protobuf')) {
        const buffer = User.encode(User.create(newUser)).finish();
        res.set('Content-Type', 'application/x-protobuf');
        res.status(201).send(Buffer.from(buffer));
      } else {
        res.status(201).json(newUser);
      }
    } catch (e) {
      console.error('[ERROR] POST /api/v1/users:', e.message);
      res.status(400).json({ error: e.message });
    }
  });

  // ── DELETE /api/v1/users/:id ──────────────────────────────────────────────
  app.delete('/api/v1/users/:id', (req, res) => {
    const accept = req.headers['accept'] || '';
    const index  = MOCK_USERS.findIndex(u => u.id === req.params.id);

    if (index === -1) return res.status(404).json({ error: 'User not found' });

    const [deleted] = MOCK_USERS.splice(index, 1);
    console.log(`[DELETE]   DELETE /api/v1/users/${req.params.id} → name="${deleted.name}"`);

    if (accept.includes('application/x-protobuf')) {
      const buffer = User.encode(User.create(deleted)).finish();
      res.set('Content-Type', 'application/x-protobuf');
      res.send(Buffer.from(buffer));
    } else {
      res.json(deleted);
    }
  });

  app.listen(PORT, () => {
    console.log(`\nServidor rodando em http://localhost:${PORT}`);
    console.log('\nTeste com cURL:');
    console.log(`  GET  (JSON):     curl http://localhost:${PORT}/api/v1/users`);
    console.log(`  GET  (Protobuf): curl -H "Accept: application/x-protobuf" http://localhost:${PORT}/api/v1/users | xxd | head`);
    console.log('\nEndpoint do emulador Android: http://10.0.2.2:3000\n');
  });
}

bootstrap().catch(console.error);
