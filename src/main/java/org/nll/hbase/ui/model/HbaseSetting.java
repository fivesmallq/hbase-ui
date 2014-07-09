/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.nll.hbase.ui.model;

/**
 *
 * @author fivesmallq
 */
public class HbaseSetting {

    private String name;
    /**
     * hbase.master
     */
    private String master;
    /**
     * hbase.zookeeper.property.clientPort
     */
    private String port = "2181";
    /**
     * hbase.zookeeper.quorum
     */
    private String quorum;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMaster() {
        return master;
    }

    public void setMaster(String master) {
        this.master = master;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getQuorum() {
        return quorum;
    }

    public void setQuorum(String quorum) {
        this.quorum = quorum;
    }

}
