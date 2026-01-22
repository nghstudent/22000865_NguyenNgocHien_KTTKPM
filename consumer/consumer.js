const amqp = require('amqplib');

// Hàm xử lý hàng đợi chính
async function startConsumer() {
    try {
        const connection = await amqp.connect('amqp://rabbitmq');
        const channel = await connection.createChannel();

        const exchangeDLX = 'order_dlx_exchange';
        const routingKeyDLX = 'order_dlx_routing_key';
        const queueMain = 'order_queue';

        await channel.assertExchange(exchangeDLX, 'direct', { durable: true });

        await channel.assertQueue(queueMain, {
            durable: true,
            arguments: {
                'x-dead-letter-exchange': exchangeDLX,
                'x-dead-letter-routing-key': routingKeyDLX
            }
        });

        console.log(`[*] Đang đợi tin nhắn tại ${queueMain}...`);

        channel.consume(queueMain, (msg) => {
            if (msg !== null) {
                try {
                    const content = JSON.parse(msg.content.toString());
                    console.log(`[x] Đang xử lý đơn hàng:`, content);

                    if (content.price < 0) {
                        throw new Error("Giá không hợp lệ!");
                    }

                    console.log(`[V] Xử lý thành công!`);
                    channel.ack(msg);
                } catch (error) {
                    console.error(`[X] Lỗi xử lý, chuyển sang DLQ: ${error.message}`);
                    channel.nack(msg, false, false);
                }
            }
        });
    } catch (error) {
        console.error("Lỗi kết nối Consumer:", error);
    }
}

// Hàm xử lý hàng đợi lỗi (DLQ)
async function startDLQConsumer() {
    try {
        const connection = await amqp.connect('amqp://rabbitmq');
        const channel = await connection.createChannel();

        const exchangeDLX = 'order_dlx_exchange';
        const queueDLQ = 'order_dlq';
        const routingKeyDLX = 'order_dlx_routing_key';

        await channel.assertExchange(exchangeDLX, 'direct', { durable: true });
        await channel.assertQueue(queueDLQ, { durable: true });
        await channel.bindQueue(queueDLQ, exchangeDLX, routingKeyDLX);

        console.log(`[!] Hệ thống DLQ đã sẵn sàng tại: ${queueDLQ}`);

        channel.consume(queueDLQ, (msg) => {
            if (msg !== null) {
                const content = msg.content.toString();
                console.log(`[CẢNH BÁO] Tin nhắn lỗi: ${content}`);
                channel.ack(msg); 
            }
        });
    } catch (error) {
        console.error("Lỗi kết nối DLQ Consumer:", error);
    }
}

// Chạy cả hai
startConsumer();
startDLQConsumer();