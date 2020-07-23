const AWS = require('aws-sdk');

AWS.config.update({region: 'ap-southeast-2'});
const sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const SMS_SQS_URL = process.env.SMS_SQS_URL;
const TWILIO_ACCOUNT_AUTH = process.env.TWILIO_ACCOUNT_AUTH;
const TWILIO_ACCOUNT_SID = process.env.TWILIO_ACCOUNT_SID;
const TWILIO_PHONE_NUMBER = process.env.TWILIO_PHONE_NUMBER;
const client = require('twilio')(TWILIO_ACCOUNT_SID, TWILIO_ACCOUNT_AUTH);

exports.handler = async (event) => {
    console.log(event);
    const message = event.Records[0];
    if( !message ) return;

    const body = JSON.parse(message.body);
    console.log("body");
    console.log(JSON.stringify(body, null, 2));

    const params ={
        body: body.message,
        from: TWILIO_PHONE_NUMBER,
        to: body.phoneNumber,
    };


    console.log("params");
    console.log(JSON.stringify(params));
    try{
        console.log("Sending Message")
        const data = await client.messages.create(params);
        console.log("MessageID is " + data.sid);

        //delete message
        const deleteParam = {
            ReceiptHandle: message.receiptHandle,
            QueueUrl: SMS_SQS_URL
        };
        console.log("deleteParam");
        console.log(JSON.stringify(deleteParam, null, 2));
        await sqs.deleteMessage(deleteParam).promise();

    } catch (err) {
        console.error(JSON.stringify(err, null, 2));
    }
}
