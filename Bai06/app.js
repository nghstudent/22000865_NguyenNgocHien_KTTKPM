const http = require('http');
const port = 3000;

const server = http.createServer((req, res) => {
  res.end('Node.js Multi-stage Build Success!\n');
});

server.listen(port, () => {
  console.log(`Server running on port ${port}`);
});