import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class Processor {
    String name;
    String physical_cores;
    String clock_speed;

    public Processor(String name, String physical_cores, String clock_speed) {
        this.name = name;
        this.physical_cores = physical_cores;
        this.clock_speed = clock_speed;
    }

    public Processor() {
    }
}
