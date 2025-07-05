// server.js
const express = require('express');
const fs = require('fs');
const path = require('path');

const app = express();
const PORT = 3000;

// Priečinok s tvojimi JSON stromami
const stromyDir = path.join(__dirname, 'stromy');

// 1) Slúži statické súbory (HTML, JS) z public/
app.use(express.static(path.join(__dirname, 'public')));

// 2) Endpoint: vráti zoznam všetkých strom*.json
app.get('/list-json', (req, res) => {
  fs.readdir(stromyDir, (err, files) => {
    if (err) return res.status(500).json([]);
    const jsonFiles = files.filter(f => f.endsWith('.json'));
    res.json(jsonFiles);
  });
});

// 3) Endpoint: vráti obsah konkrétneho JSON stromu
app.get('/stromy/:name', (req, res) => {
  const filePath = path.join(stromyDir, req.params.name);
  if (!fs.existsSync(filePath)) {
    return res.status(404).send('Not found');
  }
  res.sendFile(filePath);
});

app.listen(PORT, () => {
  console.log(`Server beží na http://localhost:${PORT}`);
});