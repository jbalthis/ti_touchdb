package com.obscure.titouchdb;

import org.appcelerator.kroll.KrollFunction;
import org.appcelerator.kroll.KrollObject;
import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;

import com.couchbase.touchdb.TDDatabase;
import com.couchbase.touchdb.TDQueryOptions;

@Kroll.proxy(parentModule = TitouchdbModule.class)
public abstract class CouchQueryProxy extends KrollProxy {

	protected TDDatabase	db;

	private Boolean			descending;

	private KrollObject		endKey;

	private String			endKeyDocID;

	private Integer			groupLevel;

	private KrollObject[]	keys;

	private Integer			limit;

	private Boolean			prefetch;

	private Integer			skip;

	private Integer			stale;

	private KrollObject		startKey;

	private String			startKeyDocID;

	public CouchQueryProxy(TDDatabase db) {
		this.db = db;
	}

	@Kroll.getProperty(name = "endKey")
	public KrollObject getEndKey() {
		return endKey;
	}

	@Kroll.getProperty(name = "endKeyDocID")
	public String getEndKeyDocID() {
		return endKeyDocID;
	}

	@Kroll.getProperty(name = "groupLevel")
	public int getGroupLevel() {
		return groupLevel;
	}

	@Kroll.getProperty(name = "keys")
	public KrollObject[] getKeys() {
		return keys;
	}

	@Kroll.getProperty(name = "limit")
	public int getLimit() {
		return limit;
	}

	@Kroll.getProperty(name = "skip")
	public int getSkip() {
		return skip;
	}

	@Kroll.getProperty(name = "stale")
	public int getStale() {
		return stale;
	}

	@Kroll.getProperty(name = "startKey")
	public KrollObject getStartKey() {
		return startKey;
	}

	@Kroll.getProperty(name = "startKeyDocID")
	public String getStartKeyDocID() {
		return startKeyDocID;
	}

	@Kroll.getProperty(name = "descending")
	public boolean isDescending() {
		return descending;
	}

	@Kroll.getProperty(name = "prefetch")
	public boolean isPrefetch() {
		return prefetch;
	}

	@Kroll.setProperty(name = "descending")
	public void setDescending(boolean descending) {
		this.descending = descending;
	}

	@Kroll.setProperty(name = "endKey")
	public void setEndKey(KrollObject endKey) {
		this.endKey = endKey;
	}

	@Kroll.setProperty(name = "endKeyDocID")
	public void setEndKeyDocID(String endKeyDocID) {
		this.endKeyDocID = endKeyDocID;
	}

	@Kroll.setProperty(name = "groupLevel")
	public void setGroupLevel(int groupLevel) {
		this.groupLevel = groupLevel;
	}

	@Kroll.setProperty(name = "keys")
	public void setKeys(KrollObject[] keys) {
		this.keys = keys;
	}

	@Kroll.setProperty(name = "limit")
	public void setLimit(int limit) {
		this.limit = limit;
	}

	@Kroll.setProperty(name = "prefetch")
	public void setPrefetch(boolean prefetch) {
		this.prefetch = prefetch;
	}

	@Kroll.setProperty(name = "skip")
	public void setSkip(int skip) {
		this.skip = skip;
	}

	@Kroll.setProperty(name = "stale")
	public void setStale(int stale) {
		this.stale = stale;
	}

	@Kroll.setProperty(name = "startKey")
	public void setStartKey(KrollObject startKey) {
		this.startKey = startKey;
	}

	@Kroll.setProperty(name = "startKeyDocID")
	public void setStartKeyDocID(String startKeyDocID) {
		this.startKeyDocID = startKeyDocID;
	}

	public void start(KrollFunction callback) {
		// TODO
	}

	@Kroll.method
	public abstract CouchQueryEnumeratorProxy rows();

	@Kroll.method
	public abstract CouchQueryEnumeratorProxy rowsIfChanged();

	protected TDQueryOptions constructQueryOptions() {
		TDQueryOptions result = new TDQueryOptions();
		if (descending != null) result.setDescending(descending);
		if (endKey != null) result.setEndKey(endKey);
		if (groupLevel != null) result.setGroupLevel(groupLevel);
		if (prefetch != null) result.setIncludeDocs(prefetch);
		if (limit != null) result.setLimit(limit);
		if (skip != null) result.setSkip(skip);
		if (startKey != null) result.setStartKey(startKey);

		// TODO set keys

		// not part of this object:
		// group
		// inclusive_end
		// startkey_docid
		// endkey_docid
		// reduce
		// stale
		// local_seq
		// update_seq

		return result;
	}

}
