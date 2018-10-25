package com.ncd.xsx.ncd_ygfxy.Defines;

public enum ItemConstData {

    NT_proBNP(0, "NT-proBNP", "N-端脑利钠肽前体", 0, 20, 30000, "pg/mL", "<75岁 <125 pg/mL;>=75岁 <450 pg/mL"),
    CK_MB(1, "CK-MB", "肌酸激酶同工酶", 2, 0.5f, 60000, "ng/mL", "<=3.8 ng/mL"),
    cTnI(2, "cTnI", "肌钙蛋白I", 3, 0.01f, 60000, "ng/mL", "<=0.3 ng/mL"),
    Myo(3, "Myo", "肌红蛋白", 2, 2.5f, 60000, "ng/mL", "<=99.3 ng/mL"),
    D_Dimer(4, "D-Dimer", "D-二聚体", 1, 0.2f, 60000, "mg/L", "<=0.5 mg/L"),
    CRP(5, "CRP", "全程C-反应蛋白", 2, 0.44f, 60000, "ng/L", "<=1.0 mg/L"),
    PCT(6, "PCT", "降钙素原", 2, 0.01f, 60000, "ng/mL", "<=0.5 ng/mL"),
    CysC(7, "CysC", "胱抑素C", 2, 0.46f, 60000, "mg/L", "0.5-1.3 mg/L"),
    β_HCG(8, "β-HCG", "β-人绒毛膜促性腺激素", 2, 2f, 60000, "mIU/mL", "<=5 mIU/mL"),
    NGAL(9, "NGAL", "中性粒细胞明胶酶相关脂质运载蛋白", 2, 10, 60000, "ng/mL", "<=170 ng/mL"),
    PGI(10, "PGI", "胃蛋白酶原I", 2, 8.9f, 60000, "ng/mL", "70-150 ng/mL"),
    PGII(11, "PGII", "胃蛋白酶原II", 2, 5.65f, 60000, "ng/mL", "<27 ng/mL"),
    MPO(12, "MPO", "髓过氧化物酶", 2, 2f, 60000, "ng/mL", "<80 ng/mL"),
    Lp_PLA2(13, "Lp-PLA2", "脂蛋白相关磷脂酶A2", 2, 6f, 60000, "ng/mL", "<150 ng/mL"),
    β2_MG(14, "β2-MG", "β2微球蛋白", 2, 0.19f, 60000, "mg/L", "0.8-1.8 mg/L");

    private int index;                  //跟服务器制作二维码的数组顺序一致
    private String name_en;
    private String name_cn;
    private int point;
    private float lowvalue;
    private float highvalue;
    private String danwei;
    private String cankaozhi;

    public static final ItemConstData[] values = ItemConstData.values();
    public static final int ItemConstDataSize = ItemConstData.values.length;

    private ItemConstData(int index, String name_en, String name_cn, int point, float lowvalue, float highvalue, String danwei, String cankaozhi) {
        this.index = index;
        this.name_en = name_en;
        this.name_cn = name_cn;
        this.point = point;
        this.lowvalue = lowvalue;
        this.highvalue = highvalue;
        this.danwei = danwei;
        this.cankaozhi = cankaozhi;
    }

    public static ItemConstData valueOf(int index) {
        return values[index];
    }

    public static String getName_cnByName_en(String name_en) {
        for (ItemConstData c : ItemConstData.values()) {
            if (c.getName_en().equals(name_en)) {
                return c.name_cn;
            }
        }
        return null;
    }

    public static String getCankaozhiByName_en(String name_en) {
        for (ItemConstData c : ItemConstData.values()) {
            if (c.getName_en().equals(name_en)) {
                return c.cankaozhi;
            }
        }
        return null;
    }

    public static ItemConstData getItemConstDataByName(String name_en) {
        for (ItemConstData c : ItemConstData.values) {
            if (c.getName_en().equals(name_en)) {
                return c;
            }
        }
        return null;
    }

    public int getIndex() {
        return index;
    }

    public String getName_en() {
        return name_en;
    }

    public String getName_cn() {
        return name_cn;
    }

    public int getPoint() {
        return point;
    }

    public float getLowvalue() {
        return lowvalue;
    }

    public float getHighvalue() {
        return highvalue;
    }

    public String getDanwei() {
        return danwei;
    }

    public String getCankaozhi() {
        return cankaozhi;
    }
}
