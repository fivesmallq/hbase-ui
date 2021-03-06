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
package org.nll.hbase.ui.service;

import java.util.List;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.client.HConnection;
import org.nll.hbase.ui.core.HbaseContext;
import org.nll.hbase.ui.model.HbaseSchema;
import org.nll.hbase.ui.model.HbaseSetting;
import org.nll.hbase.ui.util.HbaseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author fivesmallq
 */
public class HbaseDataService {

    private final static Logger logger = LoggerFactory
            .getLogger(HbaseUtil.class);

    public void connect(HbaseSetting hbaseSetting) throws Exception {
        Configuration configuration = HbaseUtil.createConf(hbaseSetting);
        HConnection connection = HbaseUtil.createConnection(configuration);
        HbaseContext.addConn(hbaseSetting.getName(), connection);
        List<HbaseSchema> hbaseSchemas = HbaseUtil.getTableSchema(connection);
        HbaseContext.addSchema(hbaseSetting.getName(), hbaseSchemas);
        logger.info("schemas:{}", hbaseSchemas);
        logger.info("added connection by setting:{}", hbaseSetting);
    }
}
