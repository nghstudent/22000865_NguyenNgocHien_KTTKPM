const amqp = require('amqplib');

async function startDLQConsumer() {
    try {
        const connection = await amqp.connect('amqp://rabbitmq');
        const channel = await connection.createChannel();

        const exchangeDLX = 'order_dlx_exchange';
        const queueDLQ = 'order_dlq';
        const routingKeyDLX = 'order_dlx_routing_key';

        // 1. Khai báo Exchange và Queue cho DLQ
        await channel.assertExchange(exchangeDLX, 'direct', { durable: true });
        await channel.assertQueue(queueDLQ, { durable: true });

        // 2. Ràng buộc (Bind) Queue vào Exchange với Routing Key
        await channel.bindQueue(queueDLQ, exchangeDLX, routingKeyDLX);

        console.log(`[!] Hệ thống giám sát lỗi (DLQ) đã sẵn sàng tại: ${queueDLQ}`);

        channel.consume(queueDLQ, (msg) => {
            if (msg !== null) {
                const content = msg.content.toString();
                console.log(`[CẢNH BÁO] Tin nhắn lỗi cần kiểm tra lại: ${content}`);
                
                // Ở đây bạn có thể code thêm việc lưu lỗi vào database hoặc gửi email báo admin
                channel.ack(msg); 
            }
        });
    } catch (error) {
        console.error("Lỗi kết nối DLQ Consumer:", error);
    }
}

startDLQConsumer();