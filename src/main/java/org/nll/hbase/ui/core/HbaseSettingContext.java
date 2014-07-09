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
package org.nll.hbase.ui.core;

import com.google.common.collect.Maps;
import java.util.Map;
import org.apache.hadoop.hbase.client.HConnection;
import org.nll.hbase.ui.model.HbaseSetting;

/**
 *
 * @author fivesmallq
 */
public class HbaseSettingContext {

    private static Map<String, HbaseSetting> settingMap = Maps.newHashMap();
    private static Map<String, HConnection> connectionMap = Maps.newHashMap();

    public static void saveSetting(HbaseSetting hbaseSetting) {
        settingMap.put(hbaseSetting.getName(), hbaseSetting);
    }

    public static void addConn(String name, HConnection connection) {
        connectionMap.put(name, connection);
    }
}