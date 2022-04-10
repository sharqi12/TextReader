import jakarta.xml.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@XmlRootElement
@XmlAccessorType(XmlAccessType.FIELD)
public class Laptops {
    @XmlAttribute
    String moddate;

    @XmlElement(name="laptop")
    List<Laptop> laptopts;

    public Laptops(List<Laptop> laptopts) {
        this.laptopts = laptopts;
    }

    public Laptops() {
    }

    public Laptops(String moddate) {
        this.moddate = moddate;
        laptopts = new ArrayList<Laptop>();
    }

    public List<Laptop> getLaptopts() {
        return laptopts;
    }

    public void setLaptopts(List<Laptop> laptopts) {
        this.laptopts = laptopts;
    }
}
