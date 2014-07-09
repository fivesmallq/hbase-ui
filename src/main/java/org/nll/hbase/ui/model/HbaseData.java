/*
 * Copyright 2014 fivesmallq.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.nll.hbase.ui.model;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import java.util.Map;

/**
 *
 * @author fivesmallq
 */
public class HbaseData {

    private String rowkey;
    private Map<String, FamilyData> datas = Maps.newLinkedHashMap();

    public String getRowkey() {
        return rowkey;
    }

    public void setRowkey(String rowkey) {
        this.rowkey = rowkey;
    }

    public Map<String, FamilyData> getDatas() {
        return datas;
    }

    public void setDatas(Map<String, FamilyData> datas) {
        this.datas = datas;
    }

    @Override
    public String toString() {
        return JSON.toJSONString(this); //To change body of generated methods, choose Tools | Templates.
    }

}
