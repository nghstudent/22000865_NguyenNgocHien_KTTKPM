# Sử dụng redis:latest theo yêu cầu
FROM redis:latest

# Copy file cấu hình vào thư mục /usr/local/etc/redis/
COPY redis.conf /usr/local/etc/redis/redis.conf

# Chạy Redis server với file cấu hình đã copy
CMD [ "redis-server", "/usr/local/etc/redis/redis.conf" ]

# Cổng mặc định của Redis là 6379
EXPOSE 6379