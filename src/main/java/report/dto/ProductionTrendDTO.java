package report.dto;

public class ProductionTrendDTO {
    private String label;
    private double producedQty;
    private double lossQty;

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }
    public double getProducedQty() { return producedQty; }
    public void setProducedQty(double producedQty) { this.producedQty = producedQty; }
    public double getLossQty() { return lossQty; }
    public void setLossQty(double lossQty) { this.lossQty = lossQty; }
}
