
const AWS = require('aws-sdk');
AWS.config.update({region: 'ap-southeast-2'});
const sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const VOUCHER_RESULT_SQS_URL = process.env.VOUCHER_RESULT_SQS_URL;
const VOUCHER_ORDER_SQS_URL = process.env.VOUCHER_ORDER_SQS_URL;
const VoucherOrder = require('../../model/VoucherOrder');
const OrderStatus = require('../../model/OrderStatusEnum');

exports.handler = async (event) => {
    console.log(event);
    const message = event.Records[0];
    if( !message ) return;

    const body = JSON.parse(message.body);
    console.log("body");
    console.log(JSON.stringify(body, null, 2));

    const voucherOrder = new VoucherOrder(body);

     if (  voucherOrder.mockSpeed && voucherOrder.mockSpeed.toLowerCase() === "slow"  ) {
         console.log("Mock Speed: SLOW");
         console.log("Simulate case 3rd party process order very slow (30 ~ 120 seconds)");

        await new Promise(resolve => setTimeout(resolve, 35 * 1000));
        voucherOrder.voucherCode = Math.floor(100000000 + Math.random() * 900000000);
        voucherOrder.status = OrderStatus.FINISH;
        voucherOrder.message = "Your Voucher Order is successfully - Slow Case";

    } else if ( voucherOrder.mockSpeed && voucherOrder.mockSpeed.toLowerCase() === "error" ) {
         console.log("Mock Speed: ERROR");
         console.log("Simulate case 3rd got error when processing order");
        voucherOrder.voucherCode = "";
        voucherOrder.status = OrderStatus.ERROR;
        voucherOrder.message = "3rd party failed when processing order - Error Case";
    } else {
         console.log("Mock Speed: FAST/NORMAL");
         console.log("Simulate case 3rd process order fast/normal");
        voucherOrder.voucherCode = Math.floor(100000000 + Math.random() * 900000000);
        voucherOrder.status = OrderStatus.FINISH;
        voucherOrder.message = "Your Voucher Order is successfully - Fast Case";
    }



    // send message to success queue
    console.log("voucherOrder");
    console.log(JSON.stringify(voucherOrder, null, 2));
    const params = {
        MessageBody: JSON.stringify(voucherOrder),
        QueueUrl: VOUCHER_RESULT_SQS_URL
    };
    console.log("Send message to success queue");
    await  sqs.sendMessage(params).promise();
    //delete message
    const deleteParam = {
        ReceiptHandle: message.receiptHandle,
        QueueUrl: VOUCHER_ORDER_SQS_URL
    };
    console.log("deleteParam");
    console.log(JSON.stringify(deleteParam, null, 2));
    await sqs.deleteMessage(deleteParam).promise();
}
