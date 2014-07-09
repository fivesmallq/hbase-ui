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

import com.google.common.collect.Lists;
import java.util.List;

/**
 *
 * @author fivesmallq
 */
public class HbaseQuery {

    private String tableName;
    private String prefixRowkey;
    private String startRowkey;
    private String stopRowkey;
    private int pageSize = 10;
    private List<String> families = Lists.newArrayList();

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getPrefixRowkey() {
        return prefixRowkey;
    }

    public void setPrefixRowkey(String prefixRowkey) {
        this.prefixRowkey = prefixRowkey;
    }

    public String getStartRowkey() {
        return startRowkey;
    }

    public void setStartRowkey(String startRowkey) {
        this.startRowkey = startRowkey;
    }

    public String getStopRowkey() {
        return stopRowkey;
    }

    public void setStopRowkey(String stopRowkey) {
        this.stopRowkey = stopRowkey;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public List<String> getFamilies() {
        return families;
    }

    public void setFamilies(List<String> families) {
        this.families = families;
    }

}
