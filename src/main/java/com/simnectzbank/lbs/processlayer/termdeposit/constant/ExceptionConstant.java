package com.simnectzbank.lbs.processlayer.termdeposit.constant;

public class ExceptionConstant {

	//return 
	public static final int ERROR_CODE202001 = 202001;//账号状态不是Active的
	public static final String INSERT_TRANSACTION_FAILED = "insert.transaction.failed";//"Insert Transaction Failed"
	public static final String TRANSACTION_NOT_MATURED_FOR_RENEWAL = "transaction.not.matured.for.renewal";//"The transaction is not matured for renewal"
	public static final String TD_ACCOUNT_NUMBER_NOT_FOUND = "td.account.number.not.found"; //"TdAccountNumber Not Found-Please check whether the TdAccountNumber is correct";
	public static final String ACCOUNT_NOT_ACTIVE = "active.not.active";
	public static final String SEARCH_SUCCESS = "Search Success";
	public static final String NOT_A_CURRENT_ACCOUNT = "not.a.current.account";//not a current account
	public static final String DEBIT_ACCOUNT_NUMBER_NOT_FOUND = "debit.account.number.not.found";//"debitAccountNumber Not Found";
	public static final String TD_NUMBER_NOT_EXIST = "td.number.not.exist";//"Tdnumber does not exist"
	public static final String ACCOUNT_NUMBER_NOT_FOUND = "account.number.not.found";//"Account Number Not Found"
	public static final String INSUFFICIENT_FUND = "insufficient.fund";//"Insufficient Fund";
	public static final String THE_AMOUNT_NOT_SUPPORT_TERM_DEPOSIT ="amount.not.support.termdeposit" ;//"This amount is not supported for termdeposit"
	public static final String UNSUPPORTED_COTRACT_PERIOD = "notsupported.contract.period";//"Unsupported Contract Period"
	public static final String TD_RECORD_HAS_BEEN_DRAWN_DOWN = "td.record.has.drawn.down";//"TD record has been drawn down"
	public static final String ACCOUNT_NUMBER_CANNOT_SAME_WITH_REF_ACCOUNT_NUMBER = "accountnumber.cannot.same.with.refaccountnumer";//"accountnumber can't be same with refaccountnumber"
	public static final int ERROR_CODE404015 = 404015;//tdnumber不存在
	public static final int ERROR_CODE404006 = 404006;//定存账号不存在
	public static final int ERROR_CODE404001 = 404001;//账号不存在
	public static final int ERROR_CODE404007 = 404007;//debitAccountNumber不存在
	public static final int ERROR_CODE202002 = 202002;//账号余额不足
	public static final int ERROR_CODE202009 = 202009;//定存金额有误
	public static final int ERROR_CODE500007 = 500007;//交易记录accountnumber与refaccountnumber相同
	public static final int ERROR_CODE202014 = 202014;//系统不支持此周期定存
	public static final int ERROR_CODE202011 = 202011;//定存还没有到期
	public static final int ERROR_CODE202010 = 202010;//定存到期已取走
	public static final int ERROR_CODE202015 = 202015;//定存还没有到期不能延期
	public static final int ERROR_CODE500006 = 500006;//插入交易记录失败

	public static final int ERROR_CODE201003 = 201003;//not a currency account
    public static final int SUCCESS_CODE200 = 200;//operate success

    
    
}
