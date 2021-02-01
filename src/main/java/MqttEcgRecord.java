import java.text.SimpleDateFormat;
import java.util.Date;

public class MqttEcgRecord {

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");

    private String dateTime;
    private double ecg;

    public MqttEcgRecord( double ecg) {
        this.dateTime = sdf.format(new Date());
        this.ecg = ecg;
    }

    public static SimpleDateFormat getSdf() {
        return sdf;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public double getEcg() {
        return ecg;
    }

    public void setEcg(double ecg) {
        this.ecg = ecg;
    }
}
