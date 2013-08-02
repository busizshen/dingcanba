package com.dingcan.api.service;

import java.util.TreeMap;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.renren.api.client.RenrenApiInvoker;
import com.renren.api.client.param.Auth;
import com.renren.api.client.services.BaseService;

/**
 * ������
 * @author DuYang (yang.du@renren-inc.com) 2011-12-14
 *
 */
public class RenRenPage extends BaseService {

    public RenRenPage(RenrenApiInvoker invoker) {
        super(invoker);
        // TODO Auto-generated constructor stub
    }

    /**
     * ��ȡ��ǰ�û�������������
     * ע�⣺�˷�����Ҫ�û����� read_user_feed Ȩ��(��OAuth2.0��Ȩ����scope����ָ��)
     * @param type �����µ���𣬶�������Զ��ŷָ�
     *        <ul>
     *        <li>10    ����״̬�������¡�</li>
     *        <li>11  page����״̬�������¡�</li>
     *        <li>20  ������־�������¡�</li>
     *        <li>21  ������־�������¡�</li>
     *        <li>22  page������־�������¡�</li>
     *        <li>23  page������־�������¡�</li>
     *        <li>30  �ϴ���Ƭ�������¡�</li>
     *        <li>31  page�ϴ���Ƭ�������¡�</li>
     *        <li>32  ������Ƭ�������¡�</li>
     *        <li>33  �������������¡�</li>
     *        <li>34  �޸�ͷ��������¡�</li>
     *        <li>35  page�޸�ͷ��������¡�</li>
     *        <li>36  page������Ƭ�������¡�</li>
     *        <li>40  ��Ϊ���ѵ������¡�</li>
     *        <li>41  ��Ϊpage��˿�������¡�</li>
     *        <li>50  ������Ƶ�������¡�</li>
     *        <li>51  �������ӵ������¡�</li>
     *        <li>52  �������ֵ������¡�</li>
     *        <li>53  page������Ƶ�������¡�</li>
     *        <li>54  page�������ӵ������¡�</li>
     *        <li>55  page�������ֵ�������</li>
     *        </ul>
     * @param uid ֧�ִ��뵱ǰ�û���һ������ID,��ʾ��ȡ�˺��ѵ�������
     *        0��ʾ��ȡ��ǰ�û��������� 
     * @param page ֧�ַ�ҳ��ָ��ҳ��ҳ�Ŵ�1��ʼ
     * @param auth Auth�ӿ����ͣ���ʾaccessToken��sessionKey����������ֵ��
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return Feed json ����������
     */
    public JSONArray getFeed(String type, long uid, int page, int count, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "feed.get");
        params.put("type", type);
        if (uid > 0) {
            params.put("uid", String.valueOf(uid));
        }
        params.put("page", String.valueOf(page));
        params.put("count", String.valueOf(count));
        return this.getResultJSONArray(params);
    }

    /**
     * �Ե�ǰ�Ự���û���session_key��Ӧ�û�����ݷ���Ӧ�õ��Զ���������
     * ��Ҫpublic_feedȨ��
     * @param name �����±��� ע�⣺���30���ַ� 
     * @param description �������������� ע�⣺���200���ַ�
     * @param url �����±����ͼƬָ�������
     * @param image ������ͼƬ��ַ 
     * @param caption �����¸����� ע�⣺���20���ַ� 
     * @param action_name �����¶���ģ���İ��� ע�⣺���10���ַ�
     * @param action_link �����¶���ģ������
     * @param message �û�������Զ������ݡ�ע�⣺���200���ַ�
     * @param auth Auth�ӿ����ͣ���ʾaccessToken��sessionKey����������ֵ��
     *        <ul>
     *          <li>new AccessToken(accessToken)</li>
     *          <li>new SessionKey(sessionKey)</li>
     *        </ul>
     * @return ����������id��JSONObject
     */
    public JSONObject publicFeed(String name, String description, String url, String image,
                                 String caption, String action_name, String action_link,
                                 String message, Auth auth) {
        TreeMap<String, String> params = new TreeMap<String, String>();
        params.put(auth.getKey(), auth.getValue());
        params.put("method", "feed.publishFeed");
        params.put("name", name);
        params.put("description", description);
        params.put("url", url);
        params.put("image", image);
        params.put("caption", caption);
        params.put("action_name", action_name);
        params.put("action_link", action_link);
        params.put("message", message);
        return this.getResultJSONObject(params);
    }
}
