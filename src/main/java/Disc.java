import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlAttribute;

@XmlAccessorType(XmlAccessType.FIELD)
public class Disc {
    @XmlAttribute
    String type;
    String storage;

    public Disc(String type, String storage) {
        this.type = type;
        this.storage = storage;
    }

    public Disc() {
    }
}
