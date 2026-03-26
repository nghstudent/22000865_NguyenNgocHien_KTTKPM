# Sử dụng python:3.9-slim cho nhẹ
FROM python:3.9-slim

# Thiết lập thư mục làm việc
WORKDIR /app

# YÊU CẦU ĐỀ BÀI: Thiết lập biến môi trường mặc định
ENV APP_ENV=development

# Copy mã nguồn vào
COPY main.py .

# Lệnh chạy ứng dụng
CMD ["python", "main.py"]