package de.vs.unikassel.query.client.callback_interface;

import de.vs.unikassel.query.client.gui.ProgressDialog;
import de.vs.unikassel.query.client.gui.WSCClient;

public class CallBack {
	
	/**
	 * The progress-dialog that is shown until the Composition-System sends the result.
	 */
	private ProgressDialog progressDialog;
	
	/**
	 * The main-gui.
	 */
	private WSCClient wscClient;
	
	/**
	 * Indicates if the service is still waiting for a result.
	 */
	private boolean waitingForResult;
	
	/**
	 * The start-time of the last-query.
	 */
	private long startTimeLastQuery;
	
	/**
	 * The end-time of the last-query.
	 */
	private long endTimeLastQuery;
	
	/**
	 * @return the waitingForResult
	 */
	public boolean isWaitingForResult() {
		return waitingForResult;
	}

	/**
	 * @param waitingForResult the waitingForResult to set
	 */
	public void setWaitingForResult(boolean waitingForResult) {
		this.waitingForResult = waitingForResult;
	}

	/**
	 * 
	 * @return
	 */
	public long getStartTimeLastQuery() {
		return startTimeLastQuery;
	}

	/**
	 * 
	 * @param startTimeLastQuery
	 */
	public void setStartTimeLastQuery(long startTimeLastQuery) {
		this.startTimeLastQuery = startTimeLastQuery;
	}

	/**
	 * 
	 * @return
	 */
	public long getEndTimeLastQuery() {
		return endTimeLastQuery;
	}
	
	/**
	 * 
	 * @return
	 */
	public long getDurationLastQuery() {
		return this.endTimeLastQuery - this.startTimeLastQuery;
	}

	/**
	 * 
	 * @return
	 */
	public ProgressDialog getProgressDialog() {
		return progressDialog;
	}

	/**
	 * 
	 * @param progressDialog
	 */
	public void setProgressDialog(ProgressDialog progressDialog) {
		this.progressDialog = progressDialog;
	}

	public WSCClient getWscClient() {
		return wscClient;
	}

	public void setWscClient(WSCClient wscClient) {
		this.wscClient = wscClient;
	}

	public void setEndTimeLastQuery(long endTimeLastQuery) {
		this.endTimeLastQuery = endTimeLastQuery;
	}
}
