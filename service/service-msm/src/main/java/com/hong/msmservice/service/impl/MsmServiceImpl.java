package com.hong.msmservice.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsRequest;
import com.aliyuncs.dysmsapi.model.v20170525.SendSmsResponse;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.aliyuncs.profile.IClientProfile;
import com.hong.msmservice.service.MsmService;
import com.hong.msmservice.utils.ConstantPropertiesUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;

@Service
public class MsmServiceImpl implements MsmService {
    @Override
    public boolean send(String phoneNumbers, Map<String, Object> param) {

        try {
            if(StringUtils.isEmpty(phoneNumbers))return false;
            IClientProfile profile = DefaultProfile.getProfile(ConstantPropertiesUtil.REGION_ID, ConstantPropertiesUtil.KEY_ID, ConstantPropertiesUtil.KEY_SECRET);
            DefaultProfile.addEndpoint(ConstantPropertiesUtil.REGION_ID,
                    ConstantPropertiesUtil.REGION_ID,
                    ConstantPropertiesUtil.PRODUCT, ConstantPropertiesUtil.DOMAIN);
            IAcsClient acsClient = new DefaultAcsClient(profile);
            //组装请求对象
            SendSmsRequest request = new SendSmsRequest();
            //使用post提交
            request.setMethod(MethodType.POST);
            //必填:待发送手机号。支持以逗号分隔的形式进行批量调用，批量上限为1000个手机号码,批量调用相对于单条调用及时性稍有延迟,验证码类型的短信推荐使用单条调用的方式；发送国际/港澳台消息时，接收号码格式为国际区号+号码，如“85200000000”
            request.setPhoneNumbers(phoneNumbers);
            //必填:短信签名-可在短信控制台中找到
            request.setSignName(ConstantPropertiesUtil.SIGNNAME);
            //必填:短信模板-可在短信控制台中找到，发送国际/港澳台消息时，请使用国际/港澳台短信模版
            request.setTemplateCode(ConstantPropertiesUtil.TEMPLATECODE);
            //可选:模板中的变量替换JSON串,如模板内容为"亲爱的${name},您的验证码为${code}"时,此处的值为
            //友情提示:如果JSON中需要带换行符,请参照标准的JSON协议对换行符的要求,比如短信内容中包含\r\n的情况在JSON中需要表示成\\r\\n,否则会导致JSON在服务端解析失败
            //参考：request.setTemplateParam("{\"变量1\":\"值1\",\"变量2\":\"值2\",\"变量3\":\"值3\"}")
            request.setTemplateParam(JSONObject.toJSONString(param));
            //请求失败这里会抛ClientException异常
            SendSmsResponse sendSmsResponse = acsClient.getAcsResponse(request);
            //CommonResponse response = client.getCommonResponse(request);
            System.out.println(sendSmsResponse.getMessage());
            return sendSmsResponse.getCode() != null && sendSmsResponse.getCode().equals("OK");
        }catch (ServerException e) {
            e.printStackTrace();
        }
        catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
