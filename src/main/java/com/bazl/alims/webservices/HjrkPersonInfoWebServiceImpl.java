package com.bazl.alims.webservices;

import com.bazl.alims.model.HjrkPersonInfo;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.jaxws.endpoint.dynamic.JaxWsDynamicClientFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sun on 2018/6/12.
 */
@Service
public class HjrkPersonInfoWebServiceImpl implements HjrkPersonInfoWebService {

    @Value("${hjrkAdress}")
    private String hjrkAdress;

    @Override
    public List<HjrkPersonInfo> selectListByPid(String pid) {
        return null;
    }

    @Override
    public List<HjrkPersonInfo> selectListByPid2(String pid) {

        List<HjrkPersonInfo> result = new ArrayList<>();

        // 创建动态客户端
        JaxWsDynamicClientFactory dcf = JaxWsDynamicClientFactory.newInstance();
        Client client = dcf.createClient(hjrkAdress);
        // 需要密码的情况需要加上用户名和密码
        // client.getOutInterceptors().add(new ClientLoginInterceptor(USER_NAME, PASS_WORD));
        Object[] objects = new Object[0];
        try {
            // invoke("方法名",参数1,参数2,参数3....);
            objects = client.invoke("selectListByPid2", pid);
            if(objects != null && objects.length>0){
                System.out.println("返回数据:" + objects[0]);
                result = (List<HjrkPersonInfo>)objects[0];
            }
        } catch (java.lang.Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
