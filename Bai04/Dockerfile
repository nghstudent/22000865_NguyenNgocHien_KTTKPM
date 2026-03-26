# Sử dụng nginx:latest làm base image theo yêu cầu
FROM nginx:latest

# Copy file index.html từ máy vào thư mục mặc định của Nginx trong container
# Đường dẫn mặc định của Nginx để chứa file web là /usr/share/nginx/html
COPY index.html /usr/share/nginx/html/index.html

# Nginx mặc định chạy trên cổng 80
EXPOSE 80