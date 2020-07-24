
const AWS = require('aws-sdk');
AWS.config.update({region: 'ap-southeast-2'});
const sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const AUTH_TIMER_EXPIRED_SQS_URL = process.env.AUTH_TIMER_EXPIRED_SQS_URL;
const AUTH_TIMER_SQS_URL = process.env.AUTH_TIMER_SQS_URL;


exports.handler = async (event) => {
    console.log(event);
    const message = event.Records[0];
    if( !message ) return;

    const body = JSON.parse(message.body);
    console.log("body");
    console.log(JSON.stringify(body, null, 2));

    if( !body.phoneNumber ) {
        console.log("Phone Number is empty. Don't need to start timer");
        return;
    }

    console.log("------- Start Timer :  wait for 60 seconds");
    await new Promise(resolve => setTimeout(resolve, 60 * 1000));
    console.log("------- End Timer -----------------------");

    // send message to AUTH_TIMER_EXPIRED_SQS
    console.log(`send message to AUTH_TIMER_EXPIRED_SQS_URL`);
    const params = {
        MessageBody: JSON.stringify(body),
        QueueUrl: AUTH_TIMER_EXPIRED_SQS_URL
    };
    console.log("---params-----");
    console.log(JSON.stringify(params, null, 2));
    console.log("Send message to success queue");
    await  sqs.sendMessage(params).promise();

    //delete message
    const deleteParam = {
        ReceiptHandle: message.receiptHandle,
        QueueUrl: AUTH_TIMER_SQS_URL
    };
    console.log("deleteParam");
    console.log(JSON.stringify(deleteParam, null, 2));
    await sqs.deleteMessage(deleteParam).promise();
}