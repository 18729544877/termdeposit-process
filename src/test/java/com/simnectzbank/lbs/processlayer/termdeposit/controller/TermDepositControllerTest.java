package com.simnectzbank.lbs.processlayer.termdeposit.controller;


import static io.restassured.RestAssured.given;

import org.junit.BeforeClass;
import org.junit.Test;


import io.restassured.RestAssured;

public class TermDepositControllerTest{

	static DataVo data = null;
	
	@BeforeClass
    public static void setUp() {
		data = new DataVo();
		data.setToken(
				"eyJhbGciOiJIUzUxMiIsInppcCI6IkRFRiJ9.eNo8y00OwiAQhuG7zNoFkBLUpbqwadI7ADNWEn4aWozGeHchNs7yme99A9KDfJop9xc4gkTaG6XtTQrRCWEPhMKojhnDLVdKwg5sKnHNr3NCqsF1aORJZxenzRhjvKrJOtr73xr5NLk46tBkdqGlZVlToDyWYCj_hmy7Viw64ik9e6wv-HwBAAD__w.alc0ibAbJotnPxSQL2wtt9Qo8h0YYzl4WkxOK65PnGy1fK4SDmNRRVEohqOya_K7qOXJOt5Cjdm10cejK3PViA");
        RestAssured.baseURI = "http://localhost:8115/account-open-process/";
       // jsonschemaemaFactory = JsonSchemaFactory.newBuilder().setValidationConfiguration(ValidationConfiguration.newBuilder().setDefaultVersion(DRAFTV4).freeze()).freeze();
    }

	
	@Test
	public void allTermDeposit() throws Exception {
		given()
        .headers(Util.setHeader(data))
        .body(Util.getObject("/request/term-deposit/allTermDeposit.txt"))
        .post("depsoit/allTermDeposit")
        .then()
        .statusCode(200);
        //.body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
	}
	
	@Test
	public void termDepositApplication() throws Exception {
		given()
        .headers(Util.setHeader(data))
        .body(Util.getObject("/request/term-deposit/termDepositApplication.txt"))
        .post("depsoit/termDepositApplication")
        .then()
        .statusCode(200);
        //.body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
	}
	
	@Test
	public void termDepositDrawDown() throws Exception {
		given()
        .headers(Util.setHeader(data))
        .body(Util.getObject("/request/term-deposit/termDepositDrawDown.txt"))
        .post("depsoit/termDepositDrawDown")
        .then()
        .statusCode(200);
        //.body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
	}
	
	@Test
	public void termDepositRenewal() throws Exception {
		given()
        .headers(Util.setHeader(data))
        .body(Util.getObject("/request/term-deposit/termDepositRenewal.txt"))
        .post("depsoit/termDepositRenewal")
        .then()
        .statusCode(200);
        //.body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
	}
	
	@Test
	public void termDepositEnquiry() throws Exception {
		given()
        .headers(Util.setHeader(data))
        .body(Util.getObject("/request/term-deposit/termDepositEnquiry.txt"))
        .post("depsoit/termDepositEnquiry")
        .then()
        .statusCode(200);
        //.body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
	}
	
	@Test
	public void termDeposit() throws Exception {
		given()
        .headers(Util.setHeader(data))
        .body(Util.getObject("/request/term-deposit/termDeposit.txt"))
        .post("depsoit/termDeposit")
        .then()
        .statusCode(200);
        //.body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
	}
	
	@Test
	public void chequeBookCreation() throws Exception {
		given()
        .headers(Util.setHeader(data))
        .body(Util.getObject("/request/term-deposit/chequeBookCreation.txt"))
        .post("depsoit/chequeBookCreation")
        .then()
        .statusCode(200);
        //.body(matchesJsonSchemaInClasspath("response/deposit/accountCreation").using(jsonschemaemaFactory));
	}

	
	

}
