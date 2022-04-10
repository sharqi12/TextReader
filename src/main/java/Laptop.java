import jakarta.xml.bind.annotation.*;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@XmlAccessorType(XmlAccessType.FIELD)
public class Laptop {
    @XmlAttribute
    @XmlID
    String id;
    String manufacturer;
    String ram;
    String os;
    String disc_reader;
    Screen screen;
    Processor processor;
    Disc disc;
    Graphic_card graphic_card;

    public Laptop(String manufacturer, String ram, String os, String disc_reader, Screen screen, Processor processor, Disc disc, Graphic_card graphic_card) {
        this.manufacturer = manufacturer;
        this.ram = ram;
        this.os = os;
        this.disc_reader = disc_reader;
        this.screen = screen;
        this.processor = processor;
        this.disc = disc;
        this.graphic_card = graphic_card;
    }

    public Laptop() {
    }
}
