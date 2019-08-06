package com.simnectzbank.lbs.processlayer.termdeposit.constant;

import java.util.HashMap;
import java.util.Map;

public class ExceptionConstant {

	/**
     * 错误码
     */
	public static final int ERROR_CODE202001 = 202001;//账号状态不是Active的
	public static final int ERROR_CODE202002 = 202002;//账号余额不足
	public static final int ERROR_CODE202003 = 202003;//转入账号状态不是Active的
	public static final int ERROR_CODE202004 = 202004;//转出账号状态不是Active的
	public static final int ERROR_CODE202005 = 202005;//转出转入账号ccy不一致
	public static final int ERROR_CODE202006 = 202006;//账号余额不为0
	public static final int ERROR_CODE202007 = 202007;//账号不能关闭，有关联账号
	public static final int ERROR_CODE202008 = 202008;//客户存在
	public static final int ERROR_CODE202009 = 202009;//定存金额有误
	public static final int ERROR_CODE202010 = 202010;//定存到期已取走
	public static final int ERROR_CODE202011 = 202011;//定存还没有到期
	public static final int ERROR_CODE202012 = 202012;//不支持创建此账号类型的账户
	public static final int ERROR_CODE202013 = 202013;//账号类型和传入ccy不一致
	public static final int ERROR_CODE202014 = 202014;//系统不支持此周期定存
	public static final int ERROR_CODE202015 = 202015;//定存还没有到期不能延期
	public static final int ERROR_CODE202016 = 202016;//无效order type
	public static final int ERROR_CODE202017 = 202017;//无效expired date
	public static final int ERROR_CODE202018 = 202018;//wrong lot size
	public static final int ERROR_CODE202019 = 202019;//无效价格
	public static final int ERROR_CODE202020 = 202020;//no stock in stock holding portfolio
	public static final int ERROR_CODE202021 = 202021;//卖出股份大于持有股份
	public static final int ERROR_CODE202022 = 202022;//无效交易
	public static final int ERROR_CODE202023 = 202023;//no fund in fund holding portfolio
	public static final int ERROR_CODE202024 = 202024;//请将关联账号留空
	public static final int ERROR_CODE202025 = 202025;//身份证号格式有误
	public static final int ERROR_CODE202026 = 202026;//邮箱格式有误
	public static final int ERROR_CODE202027 = 202027;//电话格式有误
	public static final int ERROR_CODE202028 = 202028;//金额格式错误
	public static final int ERROR_CODE202029 = 202029;
	public static final int ERROR_CODE202030 = 202030;
	public static final int ERROR_CODE202031 = 202031;
	public static final int ERROR_CODE202032 = 202032;
	
	
	public static final int ERROR_CODE400001 = 400001;//必填字段不全
	public static final int ERROR_CODE400002 = 400002;//请求body格式有误
	
    public static final int ERROR_CODE404001 = 404001;//账号不存在
    public static final int ERROR_CODE404002 = 404002;//客户不存在
    public static final int ERROR_CODE404003 = 404003;//没有交易记录
    public static final int ERROR_CODE404004 = 404004;//转入账号不存在
    public static final int ERROR_CODE404005 = 404005;//转出账号不存在
    public static final int ERROR_CODE404006 = 404006;//定存账号不存在
    public static final int ERROR_CODE404007 = 404007;//debitAccountNumber不存在
    public static final int ERROR_CODE404008 = 404008;//没有找到利率
    public static final int ERROR_CODE404009 = 404009;//关联账号不存在
    public static final int ERROR_CODE404010 = 404010;//账号不存在
    public static final int ERROR_CODE404011 = 404011;//股票number不存在
    public static final int ERROR_CODE404012 = 404012;//没有股票持有信息
    public static final int ERROR_CODE404013 = 404013;//基金code不存在
    public static final int ERROR_CODE404014 = 404014;//没有基金持有信息
    public static final int ERROR_CODE404015 = 404015;//tdnumber不存在
    
    public static final int ERROR_CODE403002 = 403002;//无权限
    
    public static final int ERROR_CODE201001 = 201001;//不是一个Sav 或者 Current 账号
    public static final int ERROR_CODE201002 = 201002;//不是一个Sav 账号
    public static final int ERROR_CODE201003 = 201003;//不是一个Current 账号
    public static final int ERROR_CODE201004 = 201004;//不是一个外汇账号
    public static final int ERROR_CODE201005 = 201005;//不是一个定存账号
    public static final int ERROR_CODE201006 = 201006;//不是一个贵金属账号
    public static final int ERROR_CODE201007 = 201007;//不是一个基金账号
    public static final int ERROR_CODE201008 = 201008;//不是一个股票账号
    public static final int ERROR_CODE201009 = 201009;//不是一个按揭贷款账号
    
    public static final int ERROR_CODE500001 = 500001;//不支持此currency
    public static final int ERROR_CODE500002 = 500002;//调用服务接口地址失败
    public static final int ERROR_CODE500003 = 500003;//调用系统参数失败
    
    public static final int ERROR_CODE500004 = 500004;//交易记录时间格式不正确
    public static final int ERROR_CODE500005 = 500005;//交易记录时间格式不正确
    public static final int ERROR_CODE500006 = 500006;//插入交易记录失败
    public static final int ERROR_CODE500007 = 500007;//交易记录accountnumber与refaccountnumber相同
    public static final int ERROR_CODE500008 = 500008;//插入失败
    public static final int ERROR_CODE500009 = 500009;//交易类型不支持
    public static final int ERROR_CODE500010 = 5000010;//交易类型不支持
    public static final int ERROR_CODE500012 = 500012;//操作失败
    
    public static Map<Integer,String> getExceptionMap(){
    	Map<Integer,String> map = new HashMap<Integer,String>();
    	map.put(ERROR_CODE202001, "Account is not active");//账号状态不是Active的
    	map.put(ERROR_CODE202002, "Insufficient Fund");//账号余额不足
    	map.put(ERROR_CODE202003, "TransferInAccountNumber is not active");//转入账号状态不是Active的
    	map.put(ERROR_CODE202004, "TransferOutAccountNumber is not active");//转出账号状态不是Active的
    	map.put(ERROR_CODE202005, "TransferOutAccount and TransferInAccount ccy is inconsistent");//转出转入账号ccy不一致
    	map.put(ERROR_CODE202006, "Account Balance Not Zero");//账号余额不为0
    	map.put(ERROR_CODE202007, "Can Not Close-The associated account is open");//账号不能关闭，有关联账号
    	map.put(ERROR_CODE202008, "The client already exists");//客户存在
    	map.put(ERROR_CODE202009, "This amount is not supported for termdeposit");//定存金额有误
    	map.put(ERROR_CODE202010, "TD record has been drawn down");//定存到期已取走
    	map.put(ERROR_CODE202011, "Transaction is not matured for drawdown");//定存还没有到期
    	map.put(ERROR_CODE202012, "Creating an account of this account type is not supported");//不支持创建此账号类型的账户
    	map.put(ERROR_CODE202013, "The inputted currency code is not supported in this account type");//账号类型和传入ccy不一致
    	map.put(ERROR_CODE202014, "Unsupported Contract Period");//系统不支持此周期定存
    	map.put(ERROR_CODE202015, "The transaction is not matured for renewal");//定存还没有到期不能延期
    	map.put(ERROR_CODE202016, "Invalid Order Type");//无效order type
    	map.put(ERROR_CODE202017, "Invalid expiry date");//无效order type
    	map.put(ERROR_CODE202018, "wrong lot size");//无效order type
    	map.put(ERROR_CODE202019, "Invalid Price");//无效  价格
    	map.put(ERROR_CODE202020, "No stock in stock holding portfolio");//no stock in stock holding portfolio
    	map.put(ERROR_CODE202021, "Input no. of sharings is bigger than holding no. of sharings");//no stock in stock holding portfolio
    	map.put(ERROR_CODE202022, "Invalid Transaction");//无效交易
    	map.put(ERROR_CODE202023, "No fund in fund holding portfolio");
    	map.put(ERROR_CODE202024, "Please leave the relaccountnumber blank");//请将关联账号留空
    	map.put(ERROR_CODE202025, "Wrong ID numbe format");//身份证号格式有误
    	map.put(ERROR_CODE202026, "Invalid email format");//邮箱格式有误
    	map.put(ERROR_CODE202027, "Invalid phone format");//电话格式有误
    	map.put(ERROR_CODE202028, "Invalid amount format");//金额格式有误
    	map.put(ERROR_CODE202029, "Invalid date range");
    	map.put(ERROR_CODE202030, "Invalid time format");
    	map.put(ERROR_CODE202031, "Invalid date format");
    	map.put(ERROR_CODE202032, "Invalid account number");
    	
    	map.put(ERROR_CODE400001, "Required field incomplete");//必填字段不全
    	map.put(ERROR_CODE400002, "Incorrect requesting format");//请求body格式有误
    	
    	map.put(ERROR_CODE404001, "Account Number Not Found");//账号不存在
    	map.put(ERROR_CODE404002, "Customer Not Found");//客户不存在
    	map.put(ERROR_CODE404003, "Not Found Transaction Detail");//没有交易记录
    	map.put(ERROR_CODE404004, "TransferInAccountNumber Not Found");//转入账号不存在
    	map.put(ERROR_CODE404005, "TransferOutAccountNumber Not Found");//转出账号不存在
    	map.put(ERROR_CODE404006, "TdAccountNumber Not Found-Please check whether the TdAccountNumber is correct");//定存账号不存在
    	map.put(ERROR_CODE404007, "debitAccountNumber Not Found");
    	map.put(ERROR_CODE404008, "No interest rate retrieved");
    	map.put(ERROR_CODE404009, "refaccountnumber Not Found");
    	map.put(ERROR_CODE404010, "Record Not Found");
    	map.put(ERROR_CODE404011, "Stock number does not exist");
    	map.put(ERROR_CODE404012, "No Stock Holding Infomation");
    	map.put(ERROR_CODE404013, "Fund Code does not exist");
    	map.put(ERROR_CODE404014, "No Mutual Fund Holding Infomation");
    	map.put(ERROR_CODE404015, "Tdnumber does not exist");//tdnumber 不存在

//    	map.put(ERROR_CODE404007, "debitAccountNumber Not Found");//debitAccountNumber不存在
//    	map.put(ERROR_CODE404008, "No interest rate retrieved");//没有找到利率
    	
    	map.put(ERROR_CODE403002, "access forbidden");//无权限
    	
    	map.put(ERROR_CODE201001, "Not a SAV or a Current Account type");//不是一个Sav 或者 Current 账号
    	map.put(ERROR_CODE201002, "Not a SAV Account type");//不是一个Sav账号
    	map.put(ERROR_CODE201003, "Not a Current Account type");//不是一个Current账号
    	map.put(ERROR_CODE201004, "Not a Fex Account type");//不是一个外汇账户
    	map.put(ERROR_CODE201005, "Not a TermDeposit Account type");//不是一个TermDeposit账户
    	map.put(ERROR_CODE201006, "Not a Precious Metal Account type");//不是一个贵金属账号
    	map.put(ERROR_CODE201007, "Not a Mutual Fund Account type");//不是一个贵金属账号
    	map.put(ERROR_CODE201008, "Not a Stock Account type");//不是一个贵金属账号
    	map.put(ERROR_CODE201009, "Not a Mortgage Loan Account type");//不是一个贵金属账号
    	
    	map.put(ERROR_CODE500001, "Currency Not Supported");//不支持此currency
    	map.put(ERROR_CODE500002, "Failed to call service interface address");//调用服务接口地址失败
    	map.put(ERROR_CODE500003, "Failed to call system parameters");//调用系统参数失败
    	map.put(ERROR_CODE500004, "The Format of transFromDate is Not Correct");//交易记录时间格式不正确
    	map.put(ERROR_CODE500005, "The Format of transToDate is Not Correct");//交易记录时间格式不正确
    	//map.put(ERROR_CODE500003, "调用服务接口地址失败");//交易记录时间格式不正确
    	map.put(ERROR_CODE500006, "Insert Transaction Failed");//插入交易记录失败
    	map.put(ERROR_CODE500007, "accountnumber can't be same with refaccountnumber");//
    	map.put(ERROR_CODE500008, "Insert Failed");//
    	map.put(ERROR_CODE500012, "Operation failed");//操作失败
    	map.put(ERROR_CODE500009, "Transaction Type Not Support");
    	map.put(ERROR_CODE500010, "Validation failed");
    	return map;
    }
    
    //执行成功码
    public static final int SUCCESS_CODE200 = 200;//执行成功
    public static Map<Integer,String> getSuccessMap(){
    	Map<Integer,String> map = new HashMap<Integer,String>();
    	map.put(SUCCESS_CODE200, "Operation Successed");//执行成功
    	
    	return map;
    }
    
    
}
