/*
 * File:      $Id: ProcessDaoImpl.java 1220 2006-04-27 20:03:24Z mbs $
 * Copyright: (C) 1999-2005 FiveSight Technologies Inc.
 *
 */
package org.apache.ode.bpel.memdao;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.ode.bpel.common.CorrelationKey;
import org.apache.ode.bpel.dao.*;

import javax.xml.namespace.QName;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * A very simple, in-memory implementation of the {@link ProcessDAO} interface.
 */
class ProcessDaoImpl extends DaoBaseImpl implements ProcessDAO {
    private static final Log __log = LogFactory.getLog(ProcessDaoImpl.class);

    private QName _processId;
    private QName _type;
    private long _version;
    final Map<String, CorrelatorDaoImpl> _correlators = new ConcurrentHashMap<String, CorrelatorDaoImpl>();
    protected final Map<Long, ProcessInstanceDAO> _instances = new ConcurrentHashMap<Long, ProcessInstanceDAO>();
    protected final Map<Integer, PartnerLinkDAO> _plinks = new ConcurrentHashMap<Integer, PartnerLinkDAO>();
    private Map<QName, ProcessDaoImpl> _store;
    private BpelDAOConnectionImpl _conn;

    private String _guid;

    public ProcessDaoImpl(BpelDAOConnectionImpl conn, Map<QName, ProcessDaoImpl> store,
                          QName processId, QName type, String guid, long version) {
        if (__log.isDebugEnabled()) {
            __log.debug("Creating ProcessDao object for process \"" + processId + "\".");
        }

        _guid = guid;
        _conn = conn;
        _store = store;
        _processId = processId;
        _type = type;
        _version = version;
    }

    public QName getProcessId() {
        return _processId;
    }

    public CorrelatorDAO getCorrelator(String cid) {
        CorrelatorDAO ret = _correlators.get(cid);
        if (ret == null) {
            throw new IllegalArgumentException("no such correlator: " + cid);
        }
        return ret;
    }

    public Collection<CorrelatorDAO> getCorrelators() {
        // Note: _correlators.values() is a Collection<CorrealatorDaoImpl>. We can't just return this object
        // since Collection<CorrelatorDAO> is /not/ assignment compatible with Collection<CorrelatorDaoImpl>. 
        // However, a immutable Collection<CorrelationDAO> is assignment compatible with Collection<CorrelatorDaoImpl>,
        // but.... we need to introduce some ambiguity into the type hierarchy so that Java will infer the correct type.
        
        // Make an ambiguous collection.
        Collection<? extends CorrelatorDAO> foo =  _correlators.values();

        // In order to get a collection of the super-type from a sub-type we must make the collection read-only. 
        return Collections.unmodifiableCollection(foo);
    }
    
    public void removeRoutes(String routeId, ProcessInstanceDAO target) {
        for (CorrelatorDAO correlatorDAO : _correlators.values()) {
            correlatorDAO.removeRoutes(routeId, target);
        }
    }

    public ProcessInstanceDAO createInstance(CorrelatorDAO correlator) {
        ProcessInstanceDaoImpl newInstance = new ProcessInstanceDaoImpl(_conn, this, correlator);
        _instances.put(newInstance.getInstanceId(), newInstance);
        return newInstance;
    }

    public ProcessInstanceDAO getInstance(Long instanceId) {
        return _instances.get(instanceId);
    }


    public Collection<ProcessInstanceDAO> findInstance(CorrelationKey key) {
        ArrayList<ProcessInstanceDAO> result = new ArrayList<ProcessInstanceDAO>();
        for (ProcessInstanceDAO instance : _instances.values()) {
            for (CorrelationSetDAO corrSet : instance.getCorrelationSets()) {
                if (corrSet.getValue().equals(key)) result.add(instance);
            }
        }
        return result;
    }

    public void instanceCompleted(ProcessInstanceDAO instance) {
    }

    public void delete() {
        _store.remove(_processId);
    }

    public long getVersion() {
        return _version;
    }

    public String getDeployer() {
        return "nobody";
    }

    public QName getType() {
        return _type;
    }

    public void addCorrelator(String correlator) {
        CorrelatorDaoImpl corr = new CorrelatorDaoImpl(correlator);
        _correlators.put(corr.getCorrelatorId(), corr);
    }

    /**
     * Nothing to do.
     */
    public void update() {
        //TODO Check requirement for persisting.
    }

    public int getNumInstances() {
        return _instances.size();
    }

    public ProcessInstanceDAO getInstanceWithLock(Long iid) {
        return getInstance(iid);
    }

    public int getActivityFailureCount() {
        return 0;  
    }

    public Date getActivityFailureDateTime() {
        return null;
    }

    public String getGuid() {
        return _guid;
    }
    
    public void setGuid(String guid) {
        _guid = guid;
    }
}
