package com.bazl.alims.controller.delegate;

import com.bazl.alims.model.LoaRoleRelation;
import com.bazl.alims.service.LoaRoleRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

/**
 * Created by Administrator on 2018/12/21.
 */
@Controller
@RequestMapping("/LoaRoleRelation")
public class LoaRoleRelationController {
    @Autowired
    private LoaRoleRelationService loaRoleRelationService;

}
