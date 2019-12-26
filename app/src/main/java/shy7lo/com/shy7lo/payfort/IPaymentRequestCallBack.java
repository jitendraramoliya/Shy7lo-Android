package shy7lo.com.shy7lo.payfort;

/**
 * Created by JITEN-PC on 02-05-2017.
 */

public interface IPaymentRequestCallBack {
    void onPaymentRequestResponse(int responseType, PayFortData responseData);
}
