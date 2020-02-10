package com.simple.blog.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.simple.blog.dto.ThirdPartDTO;
import com.simple.blog.repository.UsersRepository;
import com.simple.blog.service.RegisterService;
import com.simple.blog.service.ThirdPartService;
import com.sn.common.util.HttpUtil;
import com.simple.blog.vo.RegisterVO;
import com.simple.blog.vo.ThirdPartVO;
import com.sn.common.dto.CommonDTO;
import com.sn.common.vo.CommonVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author songning
 * @date 2019/12/23
 * description
 */
@Service
public class ThirdPartServiceImpl implements ThirdPartService {

    @Autowired
    private RegisterService registerService;
    @Autowired
    private UsersRepository usersRepository;

    @Override
    public CommonDTO<ThirdPartDTO> getGitHub(CommonVO<ThirdPartVO> commonVO) {
        CommonDTO<ThirdPartDTO> commonDTO = this.gitHub(commonVO);
        Map<String, Object> dataExt = commonDTO.getDataExt();
        String username = "gitHubUn" + dataExt.get("id");
        String user = usersRepository.findUsernameByNameNative(username);
        if (StringUtils.isEmpty(user)) {
            register(dataExt);
        }
        return commonDTO;
    }

    private CommonDTO<ThirdPartDTO> gitHub(CommonVO<ThirdPartVO> commonVO) {
        CommonDTO<ThirdPartDTO> commonDTO = new CommonDTO<>();
        Map<String, Object> params = new HashMap<>(2);
        params.put("client_id", commonVO.getCondition().getClientId());
        params.put("client_secret", commonVO.getCondition().getClientSecret());
        params.put("code", commonVO.getCondition().getCode());
        String result1 = HttpUtil.doPost(commonVO.getCondition().getAccessTokenURL(), params);
        String accessToken = result1.split("&")[0].substring(13);
        String url = commonVO.getCondition().getUserURL() + "?access_token=" + accessToken;
        String result2 = HttpUtil.doGet(url);
        Map<String, Object> result = new HashMap<>(2);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            result = objectMapper.readValue(result2, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
        }
        commonDTO.setDataExt(result);
        return commonDTO;
    }

    private void register(Map<String, Object> dataExt) {
        String author = String.valueOf(dataExt.get("login"));
        String username = "gitHubUn" + dataExt.get("id");
        String password = "gitHubPd" + dataExt.get("id");
        String headPortrait = String.valueOf(dataExt.get("avatar_url"));
        String email = String.valueOf(dataExt.get("email"));
        String realName = String.valueOf(dataExt.get("name"));
        RegisterVO registerVO = new RegisterVO();
        registerVO.setAuthor(author);
        registerVO.setUsername(username);
        registerVO.setPassword(password);
        registerVO.setHeadPortrait(headPortrait);
        registerVO.setEmail(email);
        registerVO.setRealName(realName);
        CommonVO<RegisterVO> cvo = new CommonVO<>();
        cvo.setCondition(registerVO);
        registerService.registerAll(cvo);
    }
}
