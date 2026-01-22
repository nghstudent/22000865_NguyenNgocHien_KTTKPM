const amqp = require('amqplib');

async function sendOrder() {
    try {
        // 1. Kết nối tới RabbitMQ service trong Docker
        const connection = await amqp.connect('amqp://rabbitmq');
        const channel = await connection.createChannel();

        const queueName = 'order_queue';
        const exchangeDLX = 'order_dlx_exchange';
        const routingKeyDLX = 'order_dlx_routing_key';
        
        const msg = {
            orderId: "DH-" + Math.floor(Math.random() * 1000),
            item: 'Bàn phím cơ AKKO',
            price: 1500, 
            timestamp: new Date()
        };

        // 2. Khai báo queue khớp 100% cấu hình với Consumer (Bắt buộc)
        await channel.assertQueue(queueName, { 
            durable: true,
            arguments: {
                'x-dead-letter-exchange': exchangeDLX,
                'x-dead-letter-routing-key': routingKeyDLX
            }
        });

        // 3. Gửi tin nhắn
        channel.sendToQueue(queueName, Buffer.from(JSON.stringify(msg)), {
            persistent: true 
        });

        console.log(`[v] Đã gửi đơn hàng thành công:`, msg);

        // 4. Đóng kết nối sau khi gửi
        setTimeout(() => {
            connection.close();
            process.exit(0);
        }, 500);

    } catch (error) {
        console.error("Lỗi Producer:", error);
        process.exit(1);
    }
}

sendOrder();