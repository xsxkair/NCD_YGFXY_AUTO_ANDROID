package com.ncd.xsx.ncd_ygfxy.Databases.Entity;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

import java.sql.Date;


@DatabaseTable(tableName = "Card")
public class Card extends BaseEntity{

	@DatabaseField(generatedId = true, allowGeneratedIdInsert = true)
	private Integer id;

	@DatabaseField(width = 20)
	private String pihao;

	@DatabaseField(width = 20)
	private String item;

	@DatabaseField
	private Integer pointnum;

	@DatabaseField
	private float lowestresult;

	@DatabaseField
	private float highestresult;

	@DatabaseField(width = 100)
	private String normalresult;

	@DatabaseField(width = 20)
	private String measure;

	@DatabaseField
	private Integer channel;

	@DatabaseField
	private Integer calmode;

	@DatabaseField
	private Integer t_l;

	@DatabaseField
	private Integer waitt;

	@DatabaseField
	private Integer c_l;

	@DatabaseField
	private java.sql.Date outdate;

	@DatabaseField
	private String fend1;

	@DatabaseField
	private String fend2;

	@DatabaseField
	private Boolean qu1ise;

	@DatabaseField
	private Boolean qu2ise;

	@DatabaseField
	private Boolean qu3ise;

	@DatabaseField
	private String qu1_a;

	@DatabaseField
	private String qu1_b;

	@DatabaseField
	private String qu1_c;

	@DatabaseField
	private String qu1_d;

	@DatabaseField
	private String qu2_a;

	@DatabaseField
	private String qu2_b;

	@DatabaseField
	private String qu2_c;

	@DatabaseField
	private String qu2_d;

	@DatabaseField
	private String qu3_a;

	@DatabaseField
	private String qu3_b;

	@DatabaseField
	private String qu3_c;

	@DatabaseField
	private String qu3_d;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getPihao() {
		return pihao;
	}

	public void setPihao(String pihao) {
		this.pihao = pihao;
	}

	public String getItem() {
		return item;
	}

	public void setItem(String item) {
		this.item = item;
	}

	public Integer getPointnum() {
		return pointnum;
	}

	public void setPointnum(Integer pointnum) {
		this.pointnum = pointnum;
	}

	public float getLowestresult() {
		return lowestresult;
	}

	public void setLowestresult(float lowestresult) {
		this.lowestresult = lowestresult;
	}

	public float getHighestresult() {
		return highestresult;
	}

	public void setHighestresult(float highestresult) {
		this.highestresult = highestresult;
	}

	public String getNormalresult() {
		return normalresult;
	}

	public void setNormalresult(String normalresult) {
		this.normalresult = normalresult;
	}

	public String getMeasure() {
		return measure;
	}

	public void setMeasure(String measure) {
		this.measure = measure;
	}

	public Integer getChannel() {
		return channel;
	}

	public void setChannel(Integer channel) {
		this.channel = channel;
	}

	public Integer getCalmode() {
		return calmode;
	}

	public void setCalmode(Integer calmode) {
		this.calmode = calmode;
	}

	public Integer getT_l() {
		return t_l;
	}

	public void setT_l(Integer t_l) {
		this.t_l = t_l;
	}

	public Integer getWaitt() {
		return waitt;
	}

	public void setWaitt(Integer waitt) {
		this.waitt = waitt;
	}

	public Integer getC_l() {
		return c_l;
	}

	public void setC_l(Integer c_l) {
		this.c_l = c_l;
	}

	public Date getOutdate() {
		return outdate;
	}

	public void setOutdate(Date outdate) {
		this.outdate = outdate;
	}

	public String getFend1() {
		return fend1;
	}

	public void setFend1(String fend1) {
		this.fend1 = fend1;
	}

	public String getFend2() {
		return fend2;
	}

	public void setFend2(String fend2) {
		this.fend2 = fend2;
	}

	public Boolean getQu1ise() {
		return qu1ise;
	}

	public void setQu1ise(Boolean qu1ise) {
		this.qu1ise = qu1ise;
	}

	public Boolean getQu2ise() {
		return qu2ise;
	}

	public void setQu2ise(Boolean qu2ise) {
		this.qu2ise = qu2ise;
	}

	public Boolean getQu3ise() {
		return qu3ise;
	}

	public void setQu3ise(Boolean qu3ise) {
		this.qu3ise = qu3ise;
	}

	public String getQu1_a() {
		return qu1_a;
	}

	public void setQu1_a(String qu1_a) {
		this.qu1_a = qu1_a;
	}

	public String getQu1_b() {
		return qu1_b;
	}

	public void setQu1_b(String qu1_b) {
		this.qu1_b = qu1_b;
	}

	public String getQu1_c() {
		return qu1_c;
	}

	public void setQu1_c(String qu1_c) {
		this.qu1_c = qu1_c;
	}

	public String getQu1_d() {
		return qu1_d;
	}

	public void setQu1_d(String qu1_d) {
		this.qu1_d = qu1_d;
	}

	public String getQu2_a() {
		return qu2_a;
	}

	public void setQu2_a(String qu2_a) {
		this.qu2_a = qu2_a;
	}

	public String getQu2_b() {
		return qu2_b;
	}

	public void setQu2_b(String qu2_b) {
		this.qu2_b = qu2_b;
	}

	public String getQu2_c() {
		return qu2_c;
	}

	public void setQu2_c(String qu2_c) {
		this.qu2_c = qu2_c;
	}

	public String getQu2_d() {
		return qu2_d;
	}

	public void setQu2_d(String qu2_d) {
		this.qu2_d = qu2_d;
	}

	public String getQu3_a() {
		return qu3_a;
	}

	public void setQu3_a(String qu3_a) {
		this.qu3_a = qu3_a;
	}

	public String getQu3_b() {
		return qu3_b;
	}

	public void setQu3_b(String qu3_b) {
		this.qu3_b = qu3_b;
	}

	public String getQu3_c() {
		return qu3_c;
	}

	public void setQu3_c(String qu3_c) {
		this.qu3_c = qu3_c;
	}

	public String getQu3_d() {
		return qu3_d;
	}

	public void setQu3_d(String qu3_d) {
		this.qu3_d = qu3_d;
	}

	@Override
	public String toString() {
		return "Card{" +
				"id=" + id +
				", item='" + item + '\'' +
				", lowestresult=" + lowestresult +
				", measure='" + measure + '\'' +
				'}';
	}
}
