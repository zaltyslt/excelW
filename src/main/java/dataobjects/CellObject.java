package dataobjects;

public class CellObject {
    private String index;
    private String type;
    private boolean processed;
    private String value;
    private int intValue;
    private double floatValue;
    private boolean booleanValue;
    private String referenceValue;
    private String formulaValue;

    public CellObject(String value) {
        this.value = value;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isProcessed() {
        return processed;
    }

    public void setProcessed(boolean processed) {
        this.processed = processed;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getIntValue() {
        return intValue;
    }

    public void setIntValue(int intValue) {
        this.intValue = intValue;
    }

    public double getFloatValue() {
        return floatValue;
    }

    public void setFloatValue(double floatValue) {
        this.floatValue = floatValue;
    }

    public boolean getBooleanValue() {
        return booleanValue;
    }

    public void setBooleanValue(boolean booleanValue) {
        this.booleanValue = booleanValue;
    }

    public String getReferenceValue() {
        return referenceValue;
    }

    public void setReferenceValue(String referenceValue) {
        this.referenceValue = referenceValue;
    }

    public String getFormulaValue() {
        return formulaValue;
    }

    public void setFormulaValue(String formulaValue) {
        this.formulaValue = formulaValue;
    }

    @Override
    public String toString() {
        return "CellObject{" +
                "index='" + index + '\'' +
                ", type='" + type + '\'' +
                ", processed=" + processed +
                ", value='" + value + '\'' +
                ", intValue=" + intValue +
                ", floatValue=" + floatValue +
                ", booleanValue=" + booleanValue +
                ", referenceValue='" + referenceValue + '\'' +
                ", formulaValue='" + formulaValue + '\'' +
                '}';
    }
}
