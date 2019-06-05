package server;

public enum StatusCode {

    SUCCESS(200, "OK"),
    MOVED_PERMANENTLY(301, "Moved Permanently"),
    MOVED_TEMPORARILY(302, "Moved Temporarily"),
    NOT_MODIFIED(304, "Not Modified"),
    NOT_FOUND(404, "Not Found"),
    METHOD_NOT_ALLOWED(405, "Methed Not Allowed"),
    INTERNAL_SERVER_ERROR(500,"Internal Server Error");

    public final int code;
    public final String description;

    StatusCode(int code, String description){
        this.code = code;
        this.description = description;
    }
    StatusCode(){
        code = 0;
        description = null;
    }

    //通过状态码寻找与其状态码相同的枚举类（code, description）
    public StatusCode selectFromCode(int code){
        for(StatusCode statusCode: StatusCode.values()){
            if(statusCode.code == code) return statusCode;
        }
        return null;
    }
}
