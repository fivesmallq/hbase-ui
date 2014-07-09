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
package org.nll.hbase.ui.util;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Closeables;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.hbase.HBaseConfiguration;
import org.apache.hadoop.hbase.HTableDescriptor;
import org.apache.hadoop.hbase.KeyValue;
import org.apache.hadoop.hbase.client.HBaseAdmin;
import org.apache.hadoop.hbase.client.HConnection;
import org.apache.hadoop.hbase.client.HConnectionManager;
import org.apache.hadoop.hbase.client.HTableInterface;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.client.ResultScanner;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.Filter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.PageFilter;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.nll.hbase.ui.model.FamilyData;
import org.nll.hbase.ui.model.HbaseData;
import org.nll.hbase.ui.model.HbaseQuery;
import org.nll.hbase.ui.model.HbaseSchema;
import org.nll.hbase.ui.model.HbaseSetting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HbaseUtil {

    private final static Logger logger = LoggerFactory
            .getLogger(HbaseUtil.class);
    private static HConnection defaultConnection = null;

    public static void setDefaultConnection(HConnection connection) {
        defaultConnection = connection;
    }

    public static Configuration createConf(HbaseSetting hbaseSetting) {
        Configuration conf = HBaseConfiguration.create();
        conf.set("hbase.zookeeper.property.clientPort", hbaseSetting.getPort());
        conf.set("hbase.zookeeper.quorum", hbaseSetting.getQuorum());
        conf.set("hbase.master", hbaseSetting.getMaster());
        return conf;
    }

    public static HConnection createConnection(Configuration configuration) throws Exception {
        HConnection connection = HConnectionManager.createConnection(configuration);
        return connection;
    }

    public static List<HbaseSchema> getTableSchema(HConnection connection) throws IOException {
        HTableDescriptor[] descriptors = connection.listTables();
        List<HbaseSchema> hbaseSchemas = Lists.newLinkedList();
        for (HTableDescriptor descriptor : descriptors) {
            HbaseSchema hbaseSchema = new HbaseSchema();
            hbaseSchema.setTableName(descriptor.getNameAsString());
            List<String> familes = Lists.newLinkedList();
            Set<byte[]> keys = descriptor.getFamiliesKeys();
            for (byte[] key : keys) {
                familes.add(Bytes.toString(key));
            }
            hbaseSchema.setFamilies(familes);
            hbaseSchemas.add(hbaseSchema);
        }
        return hbaseSchemas;
    }

    /**
     * 获取table操作对象
     *
     * @param connection
     * @param tableName
     * @return
     * @throws Exception
     */
    public static HTableInterface getTable(HConnection connection, String tableName) throws Exception {
        HTableInterface table = connection.getTable(tableName);
        return table;
    }

    /**
     * 删除表
     *
     * @param tableName
     * @param configuration
     * @throws IOException
     */
    public static void dropData(String tableName, Configuration configuration) throws IOException {
        // 新建一个数据库管理员
        HBaseAdmin hAdmin;
        hAdmin = new HBaseAdmin(configuration);
        // 关闭一个表
        hAdmin.disableTable(tableName);
        // 删除一个表
        hAdmin.deleteTable(tableName);
        logger.info("drop table success! [{}]", tableName);
    }

    /**
     * scan data
     *
     * @param connection
     * @param query
     * @return
     * @throws Exception
     */
    public static List<HbaseData> scan(HConnection connection, HbaseQuery query)
            throws Exception {
        List<HbaseData> datas = Lists.newLinkedList();
        List<Filter> listForFilters = Lists.newArrayList();

        Scan scan = new Scan();
        if (StringUtils.isNotNullOrEmpty(query.getPrefixRowkey())) {
            listForFilters.add(new PrefixFilter(Bytes.toBytes(query.getPrefixRowkey())));
        }
        //FIXME set 10 but return 20
        PageFilter pageFilter = new PageFilter(query.getPageSize());
        listForFilters.add(pageFilter);
        if (listForFilters.size() == 1) {
            scan.setFilter(listForFilters.get(0));
        } else {
            Filter filterList = new FilterList(FilterList.Operator.MUST_PASS_ALL,
                    listForFilters);
            scan.setFilter(filterList);
        }
        if (StringUtils.isNotNullOrEmpty(query.getStartRowkey())) {
            scan.setStartRow(Bytes.toBytes(query.getStartRowkey()));
        }
        if (StringUtils.isNotNullOrEmpty(query.getStopRowkey())) {
            scan.setStopRow(Bytes.toBytes(query.getStopRowkey()));
        }
        for (String family : query.getFamilies()) {
            scan.addFamily(Bytes.toBytes(family));
        }
        scan.setCaching(100);
        ResultScanner rs = null;
        HTableInterface table = null;
        try {
            table = getTable(connection, query.getTableName());
            rs = table.getScanner(scan);
            int count = 0;
            for (Result r : rs) {
                count++;
                HbaseData data = new HbaseData();
                data.setRowkey(Bytes.toString(r.getRow()));
                Map<String, FamilyData> dataValues = Maps.newLinkedHashMap();
                for (KeyValue kv : r.list()) {
                    FamilyData familyData = new FamilyData();
                    String family = Bytes.toString(kv.getFamily());
                    String key = Bytes.toString(kv.getQualifier());
                    String value = Bytes.toString(kv.getValue());
                    familyData.setFamilyName(family);
                    familyData.setKey(key);
                    familyData.setValue(value);
                    dataValues.put(key, familyData);
                }
                data.setDatas(dataValues);
                datas.add(data);
            }
            logger.info("hbase return data size:{}", count);
        } finally {
            Closeables.close(rs, true);
            Closeables.close(table, true);
        }
        return datas;
    }

    /*
     * 遍历查询hbase表
     *
     * @tableName 表名
     */
    public static void getResultScann(String tableName) throws Exception {
        Scan scan = new Scan();
        ResultScanner rs = null;
        HTableInterface table = getTable(defaultConnection, tableName);
        try {
            rs = table.getScanner(scan);
            for (Result r : rs) {
                for (KeyValue kv : r.list()) {
                    System.out.println("row:" + Bytes.toString(kv.getRow()));
                    System.out.println("family:"
                            + Bytes.toString(kv.getFamily()));
                    System.out.println("qualifier:"
                            + Bytes.toString(kv.getQualifier()));
                    System.out
                            .println("value:" + Bytes.toString(kv.getValue()));
                    System.out.println("timestamp:" + kv.getTimestamp());
                    System.out
                            .println("-------------------------------------------");
                }
            }
        } finally {
            Closeables.close(rs, true);
            Closeables.close(table, true);
        }
    }

    public static void getResultScann(String tableName, String start_rowkey,
            String stop_rowkey) throws Exception {
        Scan scan = new Scan(Bytes.toBytes(start_rowkey));
        ResultScanner rs = null;
        HTableInterface table = getTable(defaultConnection, tableName);
        try {
            rs = table.getScanner(scan);
            for (Result r : rs) {
                for (KeyValue kv : r.list()) {
                    System.out.println("row:" + Bytes.toString(kv.getRow()));
                    System.out.println("family:"
                            + Bytes.toString(kv.getFamily()));
                    System.out.println("qualifier:"
                            + Bytes.toString(kv.getQualifier()));
                    System.out
                            .println("value:" + Bytes.toString(kv.getValue()));
                    System.out.println("timestamp:" + kv.getTimestamp());
                    System.out
                            .println("-------------------------------------------");
                }
            }
        } finally {
            Closeables.close(rs, true);
            Closeables.close(table, true);
        }
    }
}
