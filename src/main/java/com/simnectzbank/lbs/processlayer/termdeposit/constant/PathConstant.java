package com.simnectzbank.lbs.processlayer.termdeposit.constant;

import com.csi.sbs.common.business.constant.CommonConstant;

public class PathConstant {
	
	
	//校验是否支持输入ccy 的地址
    public static final String VALIDATE_TRANSACTIONTYPE = "http://" + CommonConstant.getSYSADMIN() + "/sysadmin/trantype/queryTranType";
	
	//校验是否支持输入ccy 的地址
    public static final String VALIDATE_CCY = "http://" + CommonConstant.getSYSADMIN() + "/sysadmin/currency/isSupportbyccy";
    //获取下一个可用的递增序列号服务地址
    public static final String NEXT_AVAILABLE = "http://SYSADMIN/sysadmin/generate/getNextAvailableNumber/";
    //获取内部服务的调用地址
    public static final String SERVICE_INTERNAL_URL = "http://" + CommonConstant.getSYSADMIN() + "/sysadmin/getServiceInternalURL";
    //更新下一个可用的递增序列号服务地址
    public static final String SAVE_NEXT_AVAILABLE = "http://SYSADMIN/sysadmin/generate/saveNextAvailableNumber";
    //获取股票账号的服务地址(返回多条信息)
    public static final String GET_STOCK = "http://INVESTMENT/investment/stock/getStockAccount";
    //获取股票账号的服务地址(返回单条信息)
    public static final String GET_ONE_STOCK = "http://INVESTMENT/investment/stock/getOneStockAccount";
    //获取基金账号的服务地址(返回多条信息)
    public static final String GET_MUTUAL = "http://INVESTMENT/fund/getMutualAccount";
    //获取基金账号的服务地址(返回单条信息)
    public static final String GET_ONE_MUTUAL = "http://INVESTMENT/fund/getOneMutualAccount";
    //关闭股票账号的服务地址
    public static final String CLOSE_STOCK = "http://INVESTMENT/investment/stock/accountClosure";
    //关闭基金账号的服务地址
    public static final String CLOSE_MUTUAL = "http://INVESTMENT/fund/accountClosure";
    //插入股票账号的服务地址
    public static final String SAVE_ACCOUNT = "http://INVESTMENT/investment/stock/addAccount";
    //插入基金账号的服务地址
    public static final String SAVE_MUTUAL = "http://INVESTMENT/fund/addAccount";
    
    //校验客户是否存在原子服务
    public static final String VAL_EXIST_CUSTOMER = "http://DEPOSIT/deposit/validate/existingCustomer";
    //校验是否存在关联账号原子服务
    public static final String VAL_ASSOCIATED_ACCOUNTS = "http://DEPOSIT/deposit/validate/associatedAccounts";
    //校验账号是否存在原子服务
    public static final String VAL_ACCOUNT_EXIST = "http://DEPOSIT/deposit/validate/accountNumberExists";
    //判断到期日是否是法定节假日
    public static final String IS_HOLIDAY = "http://SYSADMIN/sysadmin/isHoliday";
}
