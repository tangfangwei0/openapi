package example;

/**
 * Created by tangfw on 2017/5/11.
 */
public class ResponseVO {
    private String return_code;
    private String return_desc;
    private Object data;

    public String getReturn_code() {
        return return_code;
    }

    public void setReturn_code(String return_code) {
        this.return_code = return_code;
    }

    public String getReturn_desc() {
        return return_desc;
    }

    public void setReturn_desc(String return_desc) {
        this.return_desc = return_desc;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
