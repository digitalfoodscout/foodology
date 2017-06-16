public class OccuresByResult {
    private String[] often;
    private String[] seldom;

    public OccuresByResult() {
    }

    public OccuresByResult(String[] often, String[] seldom) {
        this.often = often;
        this.seldom = seldom;
    }

    public String[] getOften() {
        return often;
    }

    public void setOften(String[] often) {
        this.often = often;
    }

    public String[] getSeldom() {
        return seldom;
    }

    public void setSeldom(String[] seldom) {
        this.seldom = seldom;
    }
}
