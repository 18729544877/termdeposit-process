package com.simnectzbank.lbs.processlayer.termdeposit.constant;

import java.util.HashMap;
import java.util.Map;

public class SysConstant {

	public static final String PERMANENTRESIDENCESTATUS = "Y";// Yes
	public static final String MARITALSTATUS = "S";// Single
	public static final String CUSTOMERMAPP0 = "M";// male
	public static final String CUSTOMERMAPP1 = "U";// University
	public static final String CUSTOMERMAPP2 = "S";// Self Owned

	// 交易类型
	public static final String TRANSACTION_TYPE_TERM_WITHDRAWAL = "0002";
	public static final String TRANSACTION_TYPE_WITHDRAWAL = "0006";
	public static final String TRANSACITON_TYPE_TERM_DEPOSIT = "0001";
	public static final String TRANSACTION_TYPE_TERM_ADAPTATION = "0003";//adaptation
	public static final String TRANSACTION_TYPE_DEPOSIT = "0004";

	// 账号状态
	public static final String ACCOUNT_STATE1 = "D";// closed
	public static final String ACCOUNT_STATE_AVAILABLE = "A";// account normal
	public static final String ACCOUNT_STATE_CLOSED = "C";// has closed

	public static final String NEXT_AVAILABLE_TDNUMBER = "NextAvailableTDNumber";
	public static final String TIME_FORMAT_NORMAL = "yyyy-MM-dd HH:mm:ss";
	public static final String CREATE_SUCCESS_TIP = "Creation Succeeded";
	// 账号类型
	public static final String ACCOUNT_TYPE_SAVING = "001";//Saving
	public static final String ACCOUNT_TYPE_CURRENT = "002";//Current


	// 渠道类型
	public static final String CHANNEL_TYPE = "API";

	// 借贷标志
	public static final String CR_DR_MAINT_IND_TYPE1 = "D";
	public static final String CR_DR_MAINT_IND_TYPE2 = "C";

	public static Map<String, Object> getCRDRMap() {
		Map<String, Object> map = new HashMap<String, Object>();

		map.put(CR_DR_MAINT_IND_TYPE1, "支出");
		map.put(CR_DR_MAINT_IND_TYPE2, "存入");

		return map;
	}

	// 操作类型
	public static final String OPERATION_CREATE = "create";
	public static final String OPERATION_UPDATE = "update";
	public static final String OPERATION_QUERY = "query";
	public static final String OPERATION_DELETE = "delete";
	public static final String OPERATION_TRANSFER = "transfer";

	// 操作状态
	public static final String OPERATION_SUCCESS = "success";
	public static final String OPERATION_FAIL = "fail";

	public static final String NEXT_AVAILABLE_DEALNUMBER = "NextAvailableDealNumber";
	public static final String NEXT_AVAILABLE_ACCOUNTNUMBER = "NextAvailableAccountNumber";
	public static final String NEXT_AVAILABLE_CUSTOMERNUMBER = "NextAvailableCustomerNumber";

	// 可用CustomerNumber Item
	public static final String NEXT_AVAILABLE_SEQ = "SEQ";
	// 本服务名称
	public static final String LOCAL_SERVICE_NAME = "term deposit";

	// maturity Status
	public static final String MATURITY_STATUS_A = "A";
	public static final String MATURITY_STATUS_D = "D";
}
