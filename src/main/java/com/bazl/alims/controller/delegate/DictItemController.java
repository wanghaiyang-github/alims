package com.bazl.alims.controller.delegate;

import com.bazl.alims.service.DictItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by Administrator on 2018/12/25.
 */
@Controller
@RequestMapping("/DictItemController")
public class DictItemController {
    @Autowired
    private DictItemService dictItemService;
}