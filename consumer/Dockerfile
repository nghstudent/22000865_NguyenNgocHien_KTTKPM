FROM node:20-alpine

WORKDIR /app

COPY package*.json ./
RUN npm install

COPY . .

# Lưu ý: Consumer chạy file consumer.js
CMD ["node", "consumer.js"]