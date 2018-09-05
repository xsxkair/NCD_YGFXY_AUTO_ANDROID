package com.ncd.xsx.ncd_ygfxy.Databases.Entity;


import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Timestamp;


@DatabaseTable(tableName = "TestData")
public class TestData extends BaseEntity{

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private Long id;

	@DatabaseField(columnName = "card", canBeNull = false, foreign = true, foreignAutoRefresh = true, foreignAutoCreate = true)
	private Card card;

	@DatabaseField(columnName = "item", canBeNull = false)
	private String item;

	@DatabaseField(width = 10)
	private String cardnum;
	
	@DatabaseField(columnName = "tester", width = 20)
	private String tester;

	@DatabaseField(columnName = "sampleid", width = 30)
	private String sampleid;

	@DatabaseField(columnName = "testtime")
	private java.sql.Timestamp testtime;

	@DatabaseField
	private Integer overtime;

	@DatabaseField
	private Integer cline;

	@DatabaseField
	private Integer cparm;

	@DatabaseField
	private Integer bline;

	@DatabaseField
	private Integer tline;

	@DatabaseField
	private Float t_cv;

	@DatabaseField
	private Float c_cv;
	
	@DatabaseField(width = 2000)
	private String series;

	@DatabaseField
	private Float t_c_v;

	@DatabaseField
	private Float t_tc_v;

	@DatabaseField
	private Float testv;
	
	@DatabaseField(columnDefinition="bit(1)")
	private Boolean resultok;

	@DatabaseField(columnDefinition="bit(1)")
	private Boolean check;

	@DatabaseField(defaultValue = "false", columnDefinition="bit(1)", canBeNull = false)
	private Boolean userup;

	@DatabaseField(defaultValue = "false", columnDefinition="bit(1)", canBeNull = false)
	private Boolean ncdup;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Card getCard() {
		return card;
	}

	public void setCard(Card card) {
		this.card = card;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public String getCardnum() {
		return cardnum;
	}

	public void setCardnum(String cardnum) {
		this.cardnum = cardnum;
	}

	public String getTester() {
		return tester;
	}

	public void setTester(String tester) {
		this.tester = tester;
	}

	public String getSampleid() {
		return sampleid;
	}

	public void setSampleid(String sampleid) {
		this.sampleid = sampleid;
	}

	public Timestamp getTesttime() {
		return testtime;
	}

	public void setTesttime(Timestamp testtime) {
		this.testtime = testtime;
	}

	public Integer getOvertime() {
		return overtime;
	}

	public void setOvertime(Integer overtime) {
		this.overtime = overtime;
	}

	public Integer getCline() {
		return cline;
	}

	public void setCline(Integer cline) {
		this.cline = cline;
	}

	public Integer getCparm() {
		return cparm;
	}

	public void setCparm(Integer cparm) {
		this.cparm = cparm;
	}

	public Integer getBline() {
		return bline;
	}

	public void setBline(Integer bline) {
		this.bline = bline;
	}

	public Integer getTline() {
		return tline;
	}

	public void setTline(Integer tline) {
		this.tline = tline;
	}

	public Float getT_cv() {
		return t_cv;
	}

	public void setT_cv(Float t_cv) {
		this.t_cv = t_cv;
	}

	public Float getC_cv() {
		return c_cv;
	}

	public void setC_cv(Float c_cv) {
		this.c_cv = c_cv;
	}

	public String getSeries() {
		return series;
	}

	public void setSeries(String series) {
		this.series = series;
	}

	public Float getT_c_v() {
		return t_c_v;
	}

	public void setT_c_v(Float t_c_v) {
		this.t_c_v = t_c_v;
	}

	public Float getT_tc_v() {
		return t_tc_v;
	}

	public void setT_tc_v(Float t_tc_v) {
		this.t_tc_v = t_tc_v;
	}

	public Float getTestv() {
		return testv;
	}

	public void setTestv(Float testv) {
		this.testv = testv;
	}

	public Boolean getResultok() {
		return resultok;
	}

	public void setResultok(Boolean resultok) {
		this.resultok = resultok;
	}

	public Boolean getCheck() {
		return check;
	}

	public void setCheck(Boolean check) {
		this.check = check;
	}

	public Boolean getUserup() {
		return userup;
	}

	public void setUserup(Boolean userup) {
		this.userup = userup;
	}

	public Boolean getNcdup() {
		return ncdup;
	}

	public void setNcdup(Boolean ncdup) {
		this.ncdup = ncdup;
	}

	@Override
	public String toString() {
		return "TestData{" +
				"id=" + id +
				", card=" + card +
				", tester='" + tester + '\'' +
				", sampleid='" + sampleid + '\'' +
				", testtime=" + testtime +
				", testv=" + testv +
				", resultok=" + resultok +
				'}';
	}
}
