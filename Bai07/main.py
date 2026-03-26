import os

# Đọc biến môi trường APP_ENV, nếu không có thì mặc định là 'not set'
app_env = os.getenv('APP_ENV', 'not set')

print(f"----------------------------------------")
print(f"Ứng dụng đang chạy trong môi trường: {app_env}")
print(f"----------------------------------------")