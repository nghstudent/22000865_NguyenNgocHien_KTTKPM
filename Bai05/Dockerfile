# GIAI ĐOẠN 1: Build (Sử dụng image đầy đủ công cụ build)
FROM golang:1.21-alpine AS builder

WORKDIR /app

# Copy mã nguồn vào
COPY main.go .

# Biên dịch ứng dụng Go thành một file thực thi tên là 'myapp'
RUN go build -o myapp main.go

# GIAI ĐOẠN 2: Run 
FROM alpine:latest

WORKDIR /root/

# Chỉ copy file thực thi từ giai đoạn 'builder' sang
COPY --from=builder /app/myapp .

# Mở cổng 8080
EXPOSE 8080

# Chạy ứng dụng
CMD ["./myapp"]