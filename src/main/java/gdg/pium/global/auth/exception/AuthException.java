package gdg.pium.global.auth.exception;


public class AuthException extends CustomException {
    public AuthException(AuthExceptionDetails authExceptionDetails){
        super(authExceptionDetails);
    }
}
