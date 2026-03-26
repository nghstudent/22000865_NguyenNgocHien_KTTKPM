# Sử dụng node:18 làm base image theo yêu cầu
FROM node:18

# Thiết lập thư mục làm việc bên trong container
WORKDIR /usr/src/app

# Copy file mã nguồn vào container
COPY app.js .

# Mở cổng 3000 để truy cập ứng dụng
EXPOSE 3000

# Lệnh để chạy ứng dụng khi container khởi động
CMD [ "node", "app.js" ]