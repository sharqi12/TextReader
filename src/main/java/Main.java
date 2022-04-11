import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import jakarta.xml.bind.Unmarshaller;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Main {

    private static final String fileName = "t1_katalog.txt";
    private static final String fileNameXML = "xmlText.xml";

    private static ArrayList<Laptop> loadedLaptops = new ArrayList<Laptop>();
    private static final ArrayList<Laptop> loadedLaptopsTemp = new ArrayList<Laptop>();

    private static boolean isFirstRead = true;

    private static JTable jt = new JTable();

    public static void main(String[] args) throws IOException {



        JFrame f = new JFrame("Integracja Systemów - Maciej Gieroba");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //JPanel panel = new JPanel();

        JButton b2 = new JButton("Zapisz dane do pliku TXT");

        JButton b1 = new JButton("Wczytaj dane z pliku TXT");
        b1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file = new File(fileName);
                BufferedReader br = null;
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }
                String column2Format = "%-25.25s";
                String formatInfo = "%-2.2s" + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format + " " +column2Format;
                String naglowek = new String("nazwa producenta;przekątna ekranu;rozdzielczość ekranu;rodzaj powierzchni ekranu;czy ekran jest dotykowy;nazwa procesora;liczba rdzeni fizycznych;prędkość taktowania MHz;wielkość pamięci RAM;pojemność dysku;rodzaj dysku;nazwa układu graficznego;pamięć układu graficznego;nazwa systemu operacyjnego;rodzaj napędu fizycznego w komputerze ");
                String[] s = naglowek.split(";");
                //System.out.format(formatInfo, s[0],s[1],s[2],s[3],s[4],s[5],s[6],s[7],s[8],s[9],s[10],s[11],s[12],s[13],s[14]);

                String line;
                int row =1;
                String[] column = s;
                HashMap mapa = new HashMap();

                int k=0;
                while (true) {
                    try {
                        k++;
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException ioException) {
                        line = ";;;;;;;;;;;;;;";
                        ioException.printStackTrace();
                    }
                }
                String[][] data = new String[k][16];
                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                while (true) {
                    try {
                        if (!((line = br.readLine()) != null)) break;
                    } catch (IOException ioException) {
                        line = ";;;;;;;;;;;;;;";
                        ioException.printStackTrace();
                    }
                    s = line.split(";", -1);
                    if(mapa.containsKey(s[0])){
                        mapa.put(s[0], (int)mapa.get(s[0])+1);
                    }
                    else{
                        mapa.put(s[0],1);
                    }
                    for(int i=0; i<s.length; i++){
                        data[row-1][i]=s[i];
                    }
                    if(s.length>13 && !(s[0].equals("")  && s[1].equals("") && s[2].equals("") && s[3].equals("") && s[4].equals("") && s[5].equals("") && s[6].equals("") && s[7].equals("") && s[8].equals("") && s[9].equals("") && s[10].equals("") && s[11].equals("") && s[12].equals("") && s[13].equals("") && s[14].equals("")
                     ) && isFirstRead){
                        loadedLaptops.add(new Laptop(s[0], s[8], s[13], s[14], new Screen(s[4],s[1],s[2],s[3]), new Processor(s[5],s[6],s[7]), new Disc(s[10],s[9]), new Graphic_card(s[11], s[12])));
                    }
                    row++;
                }
                if(isFirstRead==false){

                    loadedLaptopsTemp.clear();
                    for(int g=0; g<data.length; g++){
                        Laptop tempLaptop = new Laptop(data[g][0], data[g][8], data[g][13], data[g][14], new Screen(data[g][4],data[g][1],data[g][2],data[g][3]), new Processor(data[g][5],data[g][6],data[g][7]), new Disc(data[g][10],data[g][9]), new Graphic_card(data[g][11], data[g][12]));
                        if((tempLaptop.manufacturer==null || tempLaptop.manufacturer.equals("")) &&
                                (tempLaptop.os==null || tempLaptop.os.equals("")) &&
                                (tempLaptop.ram==null || tempLaptop.ram.equals("")) &&
                                (tempLaptop.disc_reader==null || tempLaptop.disc_reader.equals("")) &&
                                (tempLaptop.disc.storage==null || tempLaptop.disc.storage.equals("")) &&
                                (tempLaptop.disc.type==null || tempLaptop.disc.type.equals("")) &&
                                (tempLaptop.screen.resolution==null || tempLaptop.screen.resolution.equals("")) &&
                                (tempLaptop.screen.type==null || tempLaptop.screen.type.equals("")) &&
                                (tempLaptop.screen.size==null || tempLaptop.screen.size.equals("")) &&
                                (tempLaptop.screen.touch==null || tempLaptop.screen.touch.equals("")) &&
                                (tempLaptop.graphic_card.memory==null || tempLaptop.graphic_card.memory.equals("")) &&
                                (tempLaptop.graphic_card.name==null || tempLaptop.graphic_card.name.equals("")) &&
                                (tempLaptop.processor.name==null || tempLaptop.processor.name.equals("")) &&
                                (tempLaptop.processor.clock_speed==null || tempLaptop.processor.clock_speed.equals("")) &&
                                (tempLaptop.processor.physical_cores==null || tempLaptop.processor.physical_cores.equals(""))){
                            continue;
                        }
                        loadedLaptopsTemp.add(new Laptop(tempLaptop.manufacturer, tempLaptop.ram, tempLaptop.os, tempLaptop.disc_reader, tempLaptop.screen, tempLaptop.processor, tempLaptop.disc, tempLaptop.graphic_card));
                    }

                    //TODO TUTAJ SPRAWDZAĆ DUPLIKATY I NOWE REKORDY
                    System.out.println("Duplikaty: " + howManyDuplicates(loadedLaptops,loadedLaptopsTemp) +" Nowe rekordy: " + howManyNew(loadedLaptops,loadedLaptopsTemp,howManyDuplicates(loadedLaptops,loadedLaptopsTemp)));
                    loadedLaptops.clear();
                    loadedLaptops.addAll(loadedLaptopsTemp);
                }
                isFirstRead=false;


                try {
                    br = new BufferedReader(new FileReader(file));
                } catch (FileNotFoundException fileNotFoundException) {
                    fileNotFoundException.printStackTrace();
                }

                //---------
                jt = new JTable(data,column);

                jt.setCellSelectionEnabled(true);

                JScrollPane sp=new JScrollPane(jt);
                sp.setSize(1500,500);
                JPanel panel2 = new JPanel();
                panel2.add(sp);
                panel2.setSize(sp.getSize());
                panel2.updateUI();
                deletePreviousTable(f, panel2);


                b2.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        //System.out.println(jt.getRowCount() + " " + jt.getColumnCount());
                        //System.out.println(prepareDataToSave(jt.getRowCount(), jt.getColumnCount(), jt));
                        try (
                                final BufferedWriter writer = Files.newBufferedWriter(Paths.get(fileName),
                                StandardCharsets.UTF_8, StandardOpenOption.WRITE);
                        ) {
                            writer.write(prepareDataToSave(jt.getRowCount(), jt.getColumnCount(), jt));
                            writer.flush();
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }
                    }
                });
            }
        });

        JButton b3 = new JButton("Zapisz dane do XML");
        b3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd 'T' HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                Laptops laptops = new Laptops((dtf.format(now)));

                for(int k=0; k< jt.getRowCount(); k++){
                    Laptop laptop = new Laptop((String) jt.getValueAt(k,0),(String) jt.getValueAt(k,8),(String) jt.getValueAt(k,13),
                            (String) jt.getValueAt(k,14), new Screen((String) jt.getValueAt(k,4), (String) jt.getValueAt(k,1),(String) jt.getValueAt(k,2),(String) jt.getValueAt(k,3)),
                            new Processor((String) jt.getValueAt(k,5), (String) jt.getValueAt(k,6), (String) jt.getValueAt(k,7)),
                            new Disc((String) jt.getValueAt(k,10), (String) jt.getValueAt(k,9)),
                            new Graphic_card((String) jt.getValueAt(k,11),(String) jt.getValueAt(k,12)));
                    laptop.id = Integer.toString(k+1);
                    laptops.laptopts.add(laptop);
                }

                jaxbObjectToXML(laptops);
            }
        });

        JButton b4 = new JButton("Wczytaj dane z XML");
        b4.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    Laptops laptopsToInsertToTable = jaxbXMLToObject();
                    String naglowek = new String("nazwa producenta;przekątna ekranu;rozdzielczość ekranu;rodzaj powierzchni ekranu;czy ekran jest dotykowy;nazwa procesora;liczba rdzeni fizycznych;prędkość taktowania MHz;wielkość pamięci RAM;pojemność dysku;rodzaj dysku;nazwa układu graficznego;pamięć układu graficznego;nazwa systemu operacyjnego;rodzaj napędu fizycznego w komputerze ");
                    String[] s = naglowek.split(";");
                    String[] column = s;

                    String[][] data = new String[laptopsToInsertToTable.laptopts.size()][16];
                    data = formatDataFromXmlToObject(laptopsToInsertToTable, data);

                    if(isFirstRead){
                        for(int g=0; g<laptopsToInsertToTable.laptopts.size(); g++){
                            Laptop tempLaptop = laptopsToInsertToTable.laptopts.get(g);
                            if(
                                    (tempLaptop.manufacturer==null || tempLaptop.manufacturer.equals("")) &&
                                            (tempLaptop.os==null || tempLaptop.os.equals("")) &&
                                            (tempLaptop.ram==null || tempLaptop.ram.equals("")) &&
                                            (tempLaptop.disc_reader==null || tempLaptop.disc_reader.equals("")) &&
                                            (tempLaptop.disc.storage==null || tempLaptop.disc.storage.equals("")) &&
                                            (tempLaptop.disc.type==null || tempLaptop.disc.type.equals("")) &&
                                            (tempLaptop.screen.resolution==null || tempLaptop.screen.resolution.equals("")) &&
                                            (tempLaptop.screen.type==null || tempLaptop.screen.type.equals("")) &&
                                            (tempLaptop.screen.size==null || tempLaptop.screen.size.equals("")) &&
                                            (tempLaptop.screen.touch==null || tempLaptop.screen.touch.equals("")) &&
                                            (tempLaptop.graphic_card.memory==null || tempLaptop.graphic_card.memory.equals("")) &&
                                            (tempLaptop.graphic_card.name==null || tempLaptop.graphic_card.name.equals("")) &&
                                            (tempLaptop.processor.name==null || tempLaptop.processor.name.equals("")) &&
                                            (tempLaptop.processor.clock_speed==null || tempLaptop.processor.clock_speed.equals("")) &&
                                            (tempLaptop.processor.physical_cores==null || tempLaptop.processor.physical_cores.equals(""))){
                                continue;
                            }
                            loadedLaptops.add(new Laptop(tempLaptop.manufacturer, tempLaptop.ram, tempLaptop.os, tempLaptop.disc_reader, tempLaptop.screen, tempLaptop.processor, tempLaptop.disc, tempLaptop.graphic_card));
                        }
                    }else {
                        loadedLaptopsTemp.clear();
                        for(int g=0; g<laptopsToInsertToTable.laptopts.size(); g++){
                            Laptop tempLaptop = laptopsToInsertToTable.laptopts.get(g);
                            if((tempLaptop.manufacturer==null || tempLaptop.manufacturer.equals("")) &&
                                    (tempLaptop.os==null || tempLaptop.os.equals("")) &&
                                    (tempLaptop.ram==null || tempLaptop.ram.equals("")) &&
                                    (tempLaptop.disc_reader==null || tempLaptop.disc_reader.equals("")) &&
                                    (tempLaptop.disc.storage==null || tempLaptop.disc.storage.equals("")) &&
                                    (tempLaptop.disc.type==null || tempLaptop.disc.type.equals("")) &&
                                    (tempLaptop.screen.resolution==null || tempLaptop.screen.resolution.equals("")) &&
                                    (tempLaptop.screen.type==null || tempLaptop.screen.type.equals("")) &&
                                    (tempLaptop.screen.size==null || tempLaptop.screen.size.equals("")) &&
                                    (tempLaptop.screen.touch==null || tempLaptop.screen.touch.equals("")) &&
                                    (tempLaptop.graphic_card.memory==null || tempLaptop.graphic_card.memory.equals("")) &&
                                    (tempLaptop.graphic_card.name==null || tempLaptop.graphic_card.name.equals("")) &&
                                    (tempLaptop.processor.name==null || tempLaptop.processor.name.equals("")) &&
                                    (tempLaptop.processor.clock_speed==null || tempLaptop.processor.clock_speed.equals("")) &&
                                    (tempLaptop.processor.physical_cores==null || tempLaptop.processor.physical_cores.equals(""))){
                                continue;
                            }
                            loadedLaptopsTemp.add(new Laptop(tempLaptop.manufacturer, tempLaptop.ram, tempLaptop.os, tempLaptop.disc_reader, tempLaptop.screen, tempLaptop.processor, tempLaptop.disc, tempLaptop.graphic_card));
                        }

                        //TODO TUTAJ SPRAWDZAĆ DUPLIKATY I NOWE REKORDY
                        System.out.println("Duplikaty: " + howManyDuplicates(loadedLaptops,loadedLaptopsTemp) +" Nowe rekordy: " + howManyNew(loadedLaptops,loadedLaptopsTemp,howManyDuplicates(loadedLaptops,loadedLaptopsTemp)));
                        loadedLaptops.clear();
                        loadedLaptops.addAll(loadedLaptopsTemp);
                    }
                    isFirstRead=false;

                    jt = new JTable(data, column);

                    jt.setCellSelectionEnabled(true);

                    JScrollPane sp=new JScrollPane(jt);
                    sp.setSize(1500,500);
                    JPanel panel2 = new JPanel();
                    panel2.add(sp);
                    panel2.setSize(sp.getSize());
                    panel2.updateUI();
                    deletePreviousTable(f,panel2);


                } catch (JAXBException jaxbException) {
                    jaxbException.printStackTrace();
                }
            }
        });

        JButton b5 = new JButton("Import danych bazy danych");
        b5.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        JButton b6 = new JButton("Eksport danych do bazy danych");
        b6.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

            }
        });

        setToolbar(b1, b2, b3, b4, b5, b6, f);


        f.setSize(1600, 900);
        f.setVisible(true);
    }

    private static void jaxbObjectToXML(Laptops laptops)
    {

        try
        {
            //Create JAXB Context
            JAXBContext jaxbContext = JAXBContext.newInstance(Laptops.class);

            //Create Marshaller
            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

            //Required formatting??
            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);

            //Print XML String to Console
            StringWriter sw = new StringWriter();


            //Write XML to StringWriter
            jaxbMarshaller.marshal(laptops, new File(fileNameXML));

            //Verify XML Content
            String xmlContent = sw.toString();
            System.out.println( xmlContent );

        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    private static Laptops jaxbXMLToObject() throws JAXBException {
        JAXBContext jaxbContext = JAXBContext.newInstance(Laptops.class);
        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
        File xmlFile = new File(fileNameXML);

        Laptops laptops = (Laptops) jaxbUnmarshaller.unmarshal(xmlFile);
        return laptops;
    }

    private static void setToolbar(JButton b1, JButton b2, JButton b3, JButton b4, JButton b5, JButton b6, JFrame f){
        JToolBar toolbar = new JToolBar();
        toolbar.setRollover(true);
        toolbar.add(b1);
        toolbar.addSeparator();
        toolbar.add(b2);
        toolbar.addSeparator();
        toolbar.add(b3);
        toolbar.addSeparator();
        toolbar.add(b4);
        toolbar.addSeparator();
        toolbar.add(b5);
        toolbar.addSeparator();
        toolbar.add(b6);
        Container contentPane = f.getContentPane();
        contentPane.add(toolbar, BorderLayout.SOUTH);
    }

    private static String prepareDataToSave(int rowCount, int columnCount, JTable table){
        String textToSave = "";
        boolean endOfFile=false;

        for(int k=0; k<rowCount; k++){
            for(int i=0; i<columnCount; i++){
                //System.out.println(table.getValueAt(k,i));
                if(table.getValueAt(k,i) == null){
                    endOfFile=true;
                    break;
                }
                textToSave = textToSave + table.getValueAt(k,i)+";";
            }
            if(!endOfFile){
                textToSave = textToSave +"\n";
            }

        }
        //System.out.println(textToSave);
        return textToSave;
    }

    private static String[][] formatDataFromXmlToObject(Laptops dataFromXml, String[][] data){
        for(int y=0; y<dataFromXml.laptopts.size()-1; y++){
            data[y][0]=dataFromXml.laptopts.get(y).manufacturer;
            data[y][1]=dataFromXml.laptopts.get(y).screen.size;
            data[y][2]=dataFromXml.laptopts.get(y).screen.resolution;
            data[y][3]=dataFromXml.laptopts.get(y).screen.type;
            data[y][4]=dataFromXml.laptopts.get(y).screen.touch;
            data[y][5]=dataFromXml.laptopts.get(y).processor.name;
            data[y][6]=dataFromXml.laptopts.get(y).processor.physical_cores;
            data[y][7]=dataFromXml.laptopts.get(y).processor.clock_speed;
            data[y][8]=dataFromXml.laptopts.get(y).ram;
            data[y][9]=dataFromXml.laptopts.get(y).disc.storage;
            data[y][10]=dataFromXml.laptopts.get(y).disc.type;
            data[y][11]=dataFromXml.laptopts.get(y).graphic_card.name;
            data[y][12]=dataFromXml.laptopts.get(y).graphic_card.memory;
            data[y][13]=dataFromXml.laptopts.get(y).os;
            data[y][14]=dataFromXml.laptopts.get(y).disc_reader;
        }
        return data;
    }

    private static void deletePreviousTable(JFrame f, JPanel panel2){
        if(f.getContentPane().getComponentCount()>1){
            f.getContentPane().remove(1);
        }
        f.getContentPane().add(panel2, BorderLayout.NORTH);
    }

    private static int howManyDuplicates(ArrayList<Laptop> loadedLaptops, ArrayList<Laptop> loadedLaptopsTemp){
        int numberOfDuplicates = 0;
        for (int h=0; h<loadedLaptopsTemp.size();h++){
            if(h+1>loadedLaptops.size()) continue;
            else if(loadedLaptops.get(h).manufacturer.equals(loadedLaptopsTemp.get(h).manufacturer) &&
                    loadedLaptops.get(h).os.equals(loadedLaptopsTemp.get(h).os) &&
                    loadedLaptops.get(h).ram.equals(loadedLaptopsTemp.get(h).ram) &&
                    loadedLaptops.get(h).disc_reader.equals(loadedLaptopsTemp.get(h).disc_reader) &&
                    loadedLaptops.get(h).screen.resolution.equals(loadedLaptopsTemp.get(h).screen.resolution) &&
                    loadedLaptops.get(h).screen.size.equals(loadedLaptopsTemp.get(h).screen.size) &&
                    loadedLaptops.get(h).screen.touch.equals(loadedLaptopsTemp.get(h).screen.touch) &&
                    loadedLaptops.get(h).screen.type.equals(loadedLaptopsTemp.get(h).screen.type) &&
                    loadedLaptops.get(h).graphic_card.memory.equals(loadedLaptopsTemp.get(h).graphic_card.memory) &&
                    loadedLaptops.get(h).graphic_card.name.equals(loadedLaptopsTemp.get(h).graphic_card.name) &&
                    loadedLaptops.get(h).processor.name.equals(loadedLaptopsTemp.get(h).processor.name) &&
                    loadedLaptops.get(h).processor.clock_speed.equals(loadedLaptopsTemp.get(h).processor.clock_speed) &&
                    loadedLaptops.get(h).processor.physical_cores.equals(loadedLaptopsTemp.get(h).processor.physical_cores)){
                numberOfDuplicates++;
            }
            }
        return numberOfDuplicates;
    }

    private static int howManyNew(ArrayList<Laptop> loadedLaptops, ArrayList<Laptop> loadedLaptopsTemp, int numberOfDuplicates){
        int numberOfNew = 0;
        numberOfNew = loadedLaptopsTemp.size()-numberOfDuplicates;
        return numberOfNew;
    }

}
