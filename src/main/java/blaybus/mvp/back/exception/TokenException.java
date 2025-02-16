package blaybus.mvp.back.exception;

public class TokenException extends CustomException {

    public TokenException(ErrorDefine errorCode) {
        super(errorCode);
    }
}
