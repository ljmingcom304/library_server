package com.ljming.webrtc;

import com.ljming.webrtc.bean.UserBean;
import com.ljming.webrtc.project.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Title:UserControllerTest
 * <p>
 * Description:单元测试
 * </p >
 * Author Jming.L
 * Date 2022/3/14 15:30
 */
@SpringBootTest(classes = WebRTCApplication.class)
public class UserControllerTest {

    @Autowired
    private UserService userService;

    @Test
    public void getUserBeanById() throws Exception {
        UserBean userBean = userService.getUserBean("001");
        System.out.println("查询结果：" + userBean.getAvatar());
    }

}
