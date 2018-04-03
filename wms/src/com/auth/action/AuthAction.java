package com.auth.action;

import com.auth.service.AuthService;
import com.util.common.BaseReturnObj;
import com.util.common.ReturnObj;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

/**
 * Created by xiongying on 15/6/19.
 */
@Controller
@RequestMapping(value = "/auth")
public class AuthAction {

    @Resource(name = "authService")
    private AuthService authService;

    @RequestMapping(value = "/getAuths")
    @ResponseBody
    public ReturnObj<List<Map<String, Object>>> getAuths(String code, String node) {
        return authService.getAuths(code, node);
    }

    @RequestMapping(value = "/login")
    @ResponseBody
    public ReturnObj<Map<String, String>> login(HttpSession httpSession, String userCode, String password) {
        ReturnObj<Map<String, String>> returnObj = authService.login(userCode, password);
        if (returnObj.isSuccess()) {
            httpSession.setAttribute("user", returnObj.getRes().get("userCode"));
        }

        return returnObj;
    }

    @RequestMapping(value = "/loginOut")
    @ResponseBody
    public BaseReturnObj loginOut(HttpSession httpSession) {
        BaseReturnObj returnObj = new BaseReturnObj();
        httpSession.removeAttribute("user");
        returnObj.setSuccess(true);
        return returnObj;
    }

    @RequestMapping(value = "/getLoginUser")
    @ResponseBody
    public ReturnObj<Map<String, Object>> getLoginUser(HttpSession httpSession) {
        String user = (String) httpSession.getAttribute("user");
        return authService.getLoginUser(user);
    }

    @RequestMapping(value = "/modifyPassword")
    @ResponseBody
    public BaseReturnObj modifyPassword(String code, String oldPassword, String newPassword) {
        return authService.modifyPassword(code, oldPassword, newPassword);
    }


}
