package com.campus.sport.net;

import com.android.volley.VolleyError;

import org.json.JSONObject;

/**
 * Created by Administrator on 2019/5/21 0021.
 */

public interface VolleyLo {
    void onResponse(JSONObject jsonObject);
    void onErrorResponse(VolleyError volleyError);
}
