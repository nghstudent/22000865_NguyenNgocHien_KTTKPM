const amqp = require('amqplib');

async function sendToDLQ() {
    try {
        const connection = await amqp.connect('amqp://rabbitmq');
        const channel = await connection.createChannel();

        const exchangeDLX = 'order_dlx_exchange';
        const routingKeyDLX = 'order_dlx_routing_key';

        const errorMsg = {
            originalMsg: { id: 999, note: "Dữ liệu rác" },
            reason: "Chủ động đẩy vào DLQ do định dạng sai từ nguồn",
            sender: "Producer_DLQ_Service"
        };

        // Gửi trực tiếp vào Exchange của DLQ với Routing Key tương ứng
        channel.publish(exchangeDLX, routingKeyDLX, Buffer.from(JSON.stringify(errorMsg)));

        console.log(`[!] Đã đẩy tin nhắn lỗi trực tiếp vào DLQ:`, errorMsg);

        setTimeout(() => {
            connection.close();
            process.exit(0);
        }, 500);

    } catch (error) {
        console.error("Lỗi Producer DLQ:", error);
    }
}

sendToDLQ();