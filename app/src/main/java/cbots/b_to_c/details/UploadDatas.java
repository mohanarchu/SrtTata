package cbots.b_to_c.details;

public class UploadDatas {

    String date,proof,chekedDate;
    boolean checked,collected;
    public UploadDatas(){

    }

    public String getDate() {
        return date;
    }

    public boolean getChecked() {
        return checked;
    }

    public boolean getCollected() {
        return collected;
    }

    public String getProof() {
        return proof;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public void setCollected(boolean collected) {
        this.collected = collected;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setProof(String proof) {
        this.proof = proof;
    }

    public String getChekedDate() {
        return chekedDate;
    }

    public void setChekedDate(String chekedDate) {
        this.chekedDate = chekedDate;
    }
}
