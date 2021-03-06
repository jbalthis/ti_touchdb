package com.obscure.titouchdb;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.titanium.TiBlob;

import com.couchbase.cblite.CBLAttachment;
import com.couchbase.cblite.CBLRevision;
import com.couchbase.cblite.CBLStatus;

@Kroll.proxy(parentModule = TitouchdbModule.class)
public class NewRevisionProxy extends AbstractRevisionProxy {

    private static final String LCAT = "NewRevisionProxy";
    

    private CBLRevision         parentRevision;

    private Map<String, Object> properties = new HashMap<String, Object>();
    
    private Set<AttachmentProxy> attachmentsToRemove = new HashSet<AttachmentProxy>();

    private Set<String> attachmentsToSave = new HashSet<String>();

    public NewRevisionProxy(DocumentProxy document, CBLRevision parentRevision) {
        super(document);
        assert document != null;

        this.parentRevision = parentRevision;
        if (parentRevision != null && parentRevision.getProperties() != null) {
            properties.putAll(parentRevision.getProperties());
        }
    }

    @Kroll.method
    public AttachmentProxy addAttachment(String name, String contentType, TiBlob content) {
        assert name != null;
        assert contentType != null;
        assert content != null;

        CBLAttachment att = new CBLAttachment(content.getInputStream(), contentType);
        AttachmentProxy proxy = new AttachmentProxy(document, name, att, content.getLength());
        getAttachmentProxies().put(name,  proxy);
        attachmentsToSave.add(name);
        return proxy;
    }

    @Kroll.getProperty(name = "parentRevision")
    public RevisionProxy getParentRevision() {
        return parentRevision != null ? new RevisionProxy(document, parentRevision) : null;
    }

    @Kroll.getProperty(name = "parentRevisionID")
    public String getParentRevisionID() {
        return parentRevision != null ? parentRevision.getRevId() : null;
    }

    @Override
    @Kroll.getProperty(name = "properties")
    public KrollDict getRevisionProperties() {
        return new KrollDict(properties);
    }

    @Kroll.method
    public void removeAttachment(String name) {
        AttachmentProxy proxy = getAttachmentProxies().remove(name);
        if (proxy != null) {
            attachmentsToRemove.add(proxy);
        }
    }

    @Kroll.method
    @SuppressWarnings("unchecked")
    public RevisionProxy save() {
        String prevRevId = null;

        if (parentRevision != null) {
            prevRevId = parentRevision.getRevId();
        }

        // TODO convert attachments to atts dictionary
        Map<String,Object> atts = properties.containsKey("_attachments") ? (Map<String,Object>) properties.get("_attachments") : new HashMap<String,Object>();
        for (AttachmentProxy proxy : attachmentsToRemove) {
            atts.remove(proxy.getName());
        }
        attachmentsToRemove.clear();
        
        for (String name : attachmentsToSave) {
            AttachmentProxy proxy = attachmentNamed(name);
            if (proxy != null) {
                atts.put(proxy.getName(), proxy.toAttachmentDictionary());
            }
        }
        attachmentsToSave.clear();
        
        properties.put("_attachments", atts);
        
        CBLStatus status = new CBLStatus();
        CBLRevision revision = new CBLRevision(properties);
        
        CBLRevision updated = document.putRevision(revision, prevRevId, false, status);

        // TODO reselect the revision?
        
        return updated != null ? new RevisionProxy(document, updated) : null;
    }

    @Kroll.setProperty(name = "isDeleted")
    public void setIsDeleted(boolean isDeleted) {
        properties.put("_deleted", true);
    }

    @Kroll.method
    public void setProperties(Map<String, Object> properties) {
        assert properties != null;
        this.properties = properties;
    }

    @Kroll.method
    public void setPropertyForKey(String key, Object value) {
        properties.put(key, value);
    }
}
