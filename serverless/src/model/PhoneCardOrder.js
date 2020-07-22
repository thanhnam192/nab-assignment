class PhoneCardOrder {
    constructor(object){
        this.phoneNumber = object.phoneNumber;
        this.orderId = object.orderId;
        this.cardNumber = object.cardNumber;
        this.status = object.status;
        this.message = object.message;
        this.mobileNetwork = object.mobileNetwork;
        this.price = object.price;
        this.mockSpeed = object.mockSpeed
    }
}

module.exports = PhoneCardOrder;