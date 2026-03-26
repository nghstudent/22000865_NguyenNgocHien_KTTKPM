# Sử dụng PostgreSQL phiên bản 15 theo yêu cầu
FROM postgres:15

# Thiết lập các biến môi trường bắt buộc cho Postgres
ENV POSTGRES_DB=mydb
ENV POSTGRES_USER=myuser
ENV POSTGRES_PASSWORD=mypassword

# Copy file SQL vào thư mục đặc biệt của Postgres
# Mọi file .sql trong thư mục này sẽ được thực thi khi container chạy lần đầu
COPY init.sql /docker-entrypoint-initdb.d/