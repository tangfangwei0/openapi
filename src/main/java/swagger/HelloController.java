package swagger;

import com.wordnik.swagger.annotations.ApiImplicitParam;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by tangfw on 2017/4/23.
 */
@RestController
public class HelloController {

//    @RequestMapping(method = RequestMethod.POST, path = "/hello")
//    public String index1(HttpServletRequest request, HttpServletResponse response, @RequestBody Map<String, String> body){
//        return "hello POST \n";
//    }
//
//    @RequestMapping(method = RequestMethod.GET, path = "/hello")
//    public String index(HttpServletRequest request, HttpServletResponse response){
//        return "hello GET \n";
//    }

    @RestController
    public class SampleController {
        @RequestMapping(value = "/hello")
        @ApiOperation(value = "添加用户",httpMethod ="POST", response = UserBean.class,notes = "HelloWorld")
        public UserBean hello(@ApiParam(required = true,name = "paramData",value = "用户信息 json 数据") String paramData){
            UserBean userBean = new UserBean();
            userBean.setUsername("liwei");
            userBean.setPassword("123456");
            return userBean;
        }
    }

}
