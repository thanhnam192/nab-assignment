const AWS = require('aws-sdk');

AWS.config.update({region: 'ap-southeast-2'});
const sqs = new AWS.SQS({apiVersion: '2012-11-05'});
const SMS_SQS_URL = process.env.SMS_SQS_URL;

exports.handler = async (event) => {
    console.log(event);
    const message = event.Records[0];
    if( !message ) return;

    const body = JSON.parse(message.body);
    console.log("body");
    console.log(JSON.stringify(body, null, 2));

    const params = {
        Message: body.message,
        PhoneNumber: body.phoneNumber,
    };
    console.log("params");
    console.log(JSON.stringify(params));
    try{
        console.log("Sending Message")
        const data = await  new AWS.SNS({apiVersion: '2010-03-31'}).publish(params).promise();
        console.log("MessageID is " + data.MessageId);

        //delete message
        const deleteParam = {
            ReceiptHandle: message.receiptHandle,
            QueueUrl: SMS_SQS_URL
        };
        console.log("deleteParam");
        console.log(JSON.stringify(deleteParam, null, 2));
        await sqs.deleteMessage(deleteParam).promise();

    } catch (err) {
        console.error(err, err.stack);
    }
}
