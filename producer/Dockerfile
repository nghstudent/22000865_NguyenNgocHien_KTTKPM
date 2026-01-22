FROM node:20-alpine

# Tạo thư mục làm việc trong container
WORKDIR /app

# Copy các file quản lý thư viện trước để tối ưu hóa việc build
COPY package*.json ./
RUN npm install

# Copy toàn bộ mã nguồn vào container
COPY . .

# Lệnh khởi chạy ứng dụng
CMD ["node", "producer.js"]