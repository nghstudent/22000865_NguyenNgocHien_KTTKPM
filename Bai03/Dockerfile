# Sử dụng node:18-alpine làm base image theo yêu cầu
FROM node:18-alpine

# Thiết lập thư mục làm việc bên trong container
WORKDIR /app

# Copy các file quản lý thư viện trước (để tối ưu cache)
COPY package.json package-lock.json ./

# Cài đặt các thư viện (node_modules)
RUN npm install

# Copy toàn bộ mã nguồn còn lại vào container
COPY . .

# Mở cổng 3000 để truy cập React app
EXPOSE 3000

# Lệnh khởi động ứng dụng
CMD ["npm", "start"]