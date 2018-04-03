package com.auth.action;

import com.auth.service.UserService;
import com.auth.vo.UserVo;
import com.util.common.BaseReturnObj;
import com.util.common.PagerReturnObj;
import com.util.pages.GridPages;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by xiongying on 15/6/19.
 */
@Controller
@RequestMapping(value = "/user")
public class UserAction {

    @Resource(name = "userService")
    private UserService userService;

    @RequestMapping(value = "/list", method = RequestMethod.POST)
    @ResponseBody
    public PagerReturnObj<List<UserVo>> list(String code, String status, GridPages pages) {
        return userService.list(code, status, pages);
    }

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj create(UserVo userVo) {
        return userService.create(userVo);
    }

    @RequestMapping(value = "/update", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj update(UserVo userVo) {
        return userService.update(userVo);
    }

    @RequestMapping(value = "/updateTheme", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj updateTheme(String userCode, String theme) {
        return userService.updateTheme(userCode, theme);
    }

    @RequestMapping(value = "/initUserPwd", method = RequestMethod.POST)
    @ResponseBody
    public BaseReturnObj initUserPwd(String userCode){
        return userService.initPwd(userCode);
    }
}
