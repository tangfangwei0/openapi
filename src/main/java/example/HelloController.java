package example;

import org.springframework.http.HttpRequest;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

/**
 * Created by tangfw on 2017/4/23.
 */
@RestController
public class HelloController {

    private static int i = 1;

    @RequestMapping(method = {RequestMethod.GET,RequestMethod.POST}, path = "/hello")
    public String hello(){
        return "hello";
    }

    @RequestMapping(method = RequestMethod.GET, path = "/vehicle.bus.aaa.hello")
    @ResponseBody
    public ResponseVO index(String username, HttpServletRequest request, HttpServletResponse response){
        ResponseVO result = new ResponseVO();
        result.setReturn_code("000000");
        result.setReturn_desc("success");
        result.setData(new User().setUsername(username));

        return  result;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/vehicle.bus.aaa.hello")
    @ResponseBody
    public ResponseVO index1(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> body){

        ResponseVO result = new ResponseVO();
        result.setReturn_code("000000");
        result.setReturn_desc("success");
        result.setData(new User().setUsername(body.get("username")));

        return  result;
    }

    @RequestMapping(method = RequestMethod.POST, path = "/log")
    @ResponseBody
    public ResponseVO receiveLog(HttpServletRequest request, HttpServletResponse response, @RequestBody Object body){

        ResponseVO result = new ResponseVO();
        result.setReturn_code("000000");
        result.setReturn_desc("success");

        System.err.println("request:"+ i++);
        System.out.println(body.toString());

        return  result;
    }


}
