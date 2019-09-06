package com.shiyi.meng.controller;

import com.shiyi.meng.service.AServiceL;
import com.shiyi.meng.util.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@CrossOrigin
@RestController
@RequestMapping("/admin")
public class AControllerL {
    @Autowired
    AServiceL aServiceL;
    @Autowired
    BaseResponse jr;
}
