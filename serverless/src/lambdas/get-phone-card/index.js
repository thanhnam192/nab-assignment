
const AWS = require('aws-sdk');
AWS.config.update({region: 'ap-southeast-2'});
const sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const PHONE_CARD_RESULT_SQS_URL = process.env.PHONE_CARD_RESULT_SQS_URL;
const PHONE_CARD_ORDER_SQS_URL = process.env.PHONE_CARD_ORDER_SQS_URL;

exports.handler = async (event) => {
    console.log(event);
    const message = event.Records[0];
    if( !message ) return;

    const body = JSON.parse(message.body);
    // send message to success queue
    const params = {
        MessageBody: "Information about current NY Times fiction bestseller for week of 12/11/2016.",
        QueueUrl: PHONE_CARD_RESULT_SQS_URL
    };

    await  sqs.sendMessage(params).promise();
    //delete message
    const deleteParam = {
        ReceiptHandle: message.receiptHandle,
        QueueUrl: PHONE_CARD_ORDER_SQS_URL
    };
    console.log("deleteParam");
    console.log(JSON.stringify(deleteParam, null, 2));
    await sqs.deleteMessage(deleteParam).promise();
}